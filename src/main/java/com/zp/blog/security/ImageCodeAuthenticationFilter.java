package com.zp.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ImageCodeAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //判断当前请求是否是登录请求
        if (request.getRequestURI ().contains ("/securityLogin")) {
            try {
                //获取用户输入的验证码
                String imageCode = request.getParameter ("vercode");

                //获取系统生成的验证码
                String key = (String) request.getSession ().getAttribute ("code");

                if (StringUtils.isEmpty (imageCode)) {
                    throw new ImageCodeException ("验证码不能为空！");
                }

                if (!imageCode.equalsIgnoreCase (key)) {
                    throw new ImageCodeException ("验证码输入错误！");
                }

                String email = request.getParameter ("email");
                if (StringUtils.isEmpty (email)) {
                    throw new ImageCodeException ("邮箱不能为空！");
                }

                String password = request.getParameter ("password");
                if (StringUtils.isEmpty (password)) {
                    throw new ImageCodeException ("密码不能为空！");
                }

            } catch (AuthenticationException e) {
                authenticationFailureHandler.onAuthenticationFailure (request, response, e);
                return;
            }

        }

        filterChain.doFilter (request, response);

    }
}
