package com.zp.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zp.blog.domain.UserInfo;
import com.zp.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    //ObjectMapper：Jackson框架的工具类，用于将对象转为json字符串
    private ObjectMapper mapper = new ObjectMapper ();

    @Autowired
    private UserService userService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map map = new HashMap ();
        map.put ("login_info", true);
        response.setContentType ("application/json;charset=utf-8");

        //登录成功，将用户保存到session
        //获取认证成功保存的user
        SecurityContext context = SecurityContextHolder.getContext ();
        UserInfo user = (UserInfo) context.getAuthentication ().getPrincipal ();
        /*String email = user.getUsername ();
        UserInfo userInfo = userService.findByEmail (email);
        request.getSession ().setAttribute ("user", userInfo);*/

        boolean flag = false;
        Collection<GrantedAuthority> authorities = user.getAuthorities ();
        for (GrantedAuthority authority : authorities) {
            //判断是不是管理员
            if (authority.getAuthority ().equals ("ROLE_ADMIN")) {
                flag = true;
                break;
            }
        }

        if (flag){
            map.put ("admin",true);
        } else {
            map.put ("admin",false);
        }

        mapper.writeValue (response.getOutputStream (), map);
    }
}
