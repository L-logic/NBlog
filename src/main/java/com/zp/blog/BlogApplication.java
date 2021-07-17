package com.zp.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.zp.blog.dao")
@EnableTransactionManagement
@SpringBootApplication
@EnableCaching //开启基于注解的缓存
public class BlogApplication {

    public static void main(String[] args) {

        SpringApplication.run (BlogApplication.class, args);
    }

}
