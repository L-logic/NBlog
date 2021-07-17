package com.zp.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    //ObjectMapper：Jackson框架的工具类，用于将对象转为json字符串
    private ObjectMapper mapper = new ObjectMapper ();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        Map map = new HashMap ();
        map.put ("login_info",false);

        String message = e.getMessage ();
        if (message.equals ("User is disabled")){
            message = "账户未激活，请去邮箱激活！";
        }
        if (message.equals ("Bad credentials")){
            message = "用户名或密码错误";
        }
        map.put ("errorMsg",message);


        response.setContentType ("application/json;charset=utf-8");
        mapper.writeValue (response.getOutputStream (),map);
    }
}
