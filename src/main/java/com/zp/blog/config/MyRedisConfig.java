package com.zp.blog.config;

import cn.hutool.json.JSON;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;

@Configuration
public class MyRedisConfig {

    private final Logger logger = LoggerFactory.getLogger (this.getClass ());

    //邮箱验证码存储在redis中保存一天
    @Bean
    public RedisCacheManager codeCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig ();
        //过期时间设为一分钟
        RedisCacheConfiguration codeRedisCacheConfiguration = redisCacheConfiguration.entryTtl (Duration.ofDays (1));

        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder (redisConnectionFactory).cacheDefaults (codeRedisCacheConfiguration);

        return builder.build ();
    }

    //自定义缓存Key的生成策略
    @Bean("myKeyGenerator")
    public KeyGenerator keyGenerator() {
        return new KeyGenerator () {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                // This will generate a unique key of the class name, the method name
                //and all method parameters appended.
                StringBuilder sb = new StringBuilder ();
                sb.append (target.getClass ().getName ());
                sb.append (method.getName ());
                for (Object obj : params) {
                    sb.append (obj.toString ());
                }
                logger.info ("keyGenerator=" + sb.toString ());
                return sb.toString ();
            }
        };
    }

    //所有类型 Object 值都是转为json存储机制 存储一天
    @Bean("objCacheManager")
    @Primary
    public RedisCacheManager objCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig ();

        //传入你想设置的序列化规则  设置了值序列化规则 json
        Jackson2JsonRedisSerializer<Object> objSerializer = new Jackson2JsonRedisSerializer<> (Object.class);

        //解决查询缓存转换异常的问题 （这个会在缓存中缓存类的全类名和值） 没有这个会出现转换异常
        // 值在底层被封装成LinkedHashMap类型 java.util.LinkedHashMap cannot be cast to com.zp.springboot.bean.Employee
        ObjectMapper om = new ObjectMapper ();
        om.setVisibility (PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping (ObjectMapper.DefaultTyping.NON_FINAL);
        objSerializer.setObjectMapper (om);

        RedisSerializationContext.SerializationPair<Object> objSerializationPair = RedisSerializationContext.SerializationPair.fromSerializer (objSerializer);
        RedisCacheConfiguration objRedisCacheConfiguration = redisCacheConfiguration.serializeValuesWith (objSerializationPair).entryTtl (Duration.ofDays (1));

        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder (redisConnectionFactory).cacheDefaults (objRedisCacheConfiguration);

        return builder.build ();
    }

}
