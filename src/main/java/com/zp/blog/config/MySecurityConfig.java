package com.zp.blog.config;

import com.zp.blog.dao.UserInfoMapper;
import com.zp.blog.security.*;
import com.zp.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.server.ui.LoginPageGeneratingWebFilter;

import javax.sql.DataSource;

@EnableWebSecurity
@ComponentScan("com.zp.blog.security")
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder ();
    }


    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler; //认证成功处理器

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;  //认证失败处理器

    @Autowired
    private ImageCodeAuthenticationFilter imageCodeAuthenticationFilter;  //验证码拦截器

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler; //注销成功处理器

    /**
     * rememberMe操作类(cookie实现的)
     * 		 持久化token
     * 		 Security中，默认是使用PersistentTokenRepository的子类InMemoryTokenRepositoryImpl，将token放在内存中
     * 		 如果使用JdbcTokenRepositoryImpl，会创建表persistent_logins，将token持久化到数据库（每次都会，所有创建好表就注释掉）
     */
    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl ();
        jdbcTokenRepository.setDataSource (dataSource);
//        jdbcTokenRepository.setCreateTableOnStartup (true); //会创建表persistent_logins，将token持久化到数据库（每次都会，所有创建好表就注释掉）
        return jdbcTokenRepository;
    }

    /*
      授权请求
         我们的示例仅要求对用户进行身份验证，并且对应用程序中的每个URL都进行了身份验证。
         我们可以通过向我们的http.authorizeRequests()方法添加多个子级来为URL指定自定义要求。
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests ().antMatchers ("/admin/**").hasRole ("ADMIN")
                .antMatchers ("/front/comment/addComment").hasAnyRole ("ADMIN","USER")
                .antMatchers ("/user/toUpload").hasAnyRole ("ADMIN","USER")
                .antMatchers ("/user/upload").hasAnyRole ("ADMIN","USER")
                .antMatchers ("*").permitAll ()
                .and ()
                .formLogin ().loginPage ("/user/login")
                .usernameParameter ("email")
                .loginProcessingUrl ("/securityLogin")
                .successHandler (myAuthenticationSuccessHandler )
                .failureHandler (myAuthenticationFailureHandler )
                .and ()
                .addFilterBefore (imageCodeAuthenticationFilter , UsernamePasswordAuthenticationFilter.class)
                //springSecurity  是会对 post请求进行验证的,导致会403，关闭csrf即可
                .csrf ().disable ()
                //注销
                .logout ()
                .logoutUrl ("/logout").logoutSuccessHandler (myLogoutSuccessHandler)
                .and ()
                //记住我一天
                .rememberMe ()
                .rememberMeParameter ("remember")
                .tokenRepository (jdbcTokenRepository ()).tokenValiditySeconds (864000)
                //UserDetailsService的作用就是获取用户信息进行校验，记住我功能需要使用浏览器Cookie中的Token进行用户校验：
                //不添加有时会报Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack.错误
                .userDetailsService (userService);




        /*http.authorizeRequests ().antMatchers ("/").permitAll ()
                .antMatchers ("/level1/**").hasRole ("VIP1")
                .antMatchers ("/level2/**").hasRole ("VIP2")
                .antMatchers ("/level3/**").hasRole ("VIP3")
                .and ()
                //开启自动配置规则
                // 设置自定义登录页面的 username和password的name属性，和指定登录页路径
                // 登录页路径GET为去登录页面 POST为登录请求
                .formLogin ().usernameParameter ("user").passwordParameter ("pwd").loginPage ("/userlogin");
        //  1、/login来到登录页
        //  2、重定向到 /login?error
        //  3、更多详细规则

        //开启自动配置的注销功能
        //默认设置是访问URL /logout
        //使HTTP会话无效 清空session
        //清理配置的所有RememberMe身份验证
        //清除 SecurityContextHolder
        //注销成功会重定向到 /login?logout
        http.logout ().logoutSuccessUrl ("/"); //注销成功以后来到首页

        //开启记住我功能
        http.rememberMe ().rememberMeParameter ("remember");
        //登录成功以后，将cookie发给浏览器保存，以后访问页面带上这个cookie，只要通过检查就可以免登录
        //点击注销会删除cookie*/
    }


    @Autowired
    private UserService userService;

    //配置认证管理器
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService (userService).passwordEncoder (passwordEncoder());
    }


    //定义认证规则 4.0
   /* @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication ().withUser ("admin").password ("123456").roles ("VIP1", "VIP2");
    }*/

   /*//定义认证规则 5.0
    @Bean
    public UserDetailsService userDetailsService() {
        // ensure the passwords are encoded properly  内存中
        User.UserBuilder users = User.withDefaultPasswordEncoder ();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager ();
        manager.createUser (users.username ("admin1").password ("123456").roles ("VIP1","VIP2").build ());
        manager.createUser (users.username ("admin2").password ("123456").roles ("VIP2", "VIP3").build ());
        manager.createUser (users.username ("admin3").password ("123456").roles ("VIP1", "VIP3").build ());
        return manager;
    }*/
}
