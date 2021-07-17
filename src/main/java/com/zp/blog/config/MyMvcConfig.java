package com.zp.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String property = System.getProperty ("user.dir");
        /*property = property + "\\upload";
        File file = new File (property);*/
        registry.addResourceHandler ("/upload/**").addResourceLocations ("file:" + property + "/upload/");
    }
}
