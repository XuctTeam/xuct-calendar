/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ReidsCacheManager
 * Author:   Derek Xu
 * Date:     2022/9/2 14:20
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot.config;


import cn.com.xuct.calendar.common.redis.config.JacksonMapper;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;


/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/9/2
 * @since 1.0.0
 */
@Configuration
public class RedisCacheConfiguration {

    /**
     * 配置cacheManager
     *
     */
    @Bean(name = "cacheManager")
    public RedisCacheManager  cacheManager(RedisConnectionFactory factory) {
        //默认的redisCahce配置，此处我们没用，而是自己配置
        //RedisCacheConfiguration.defaultCacheConfig();

        org.springframework.data.redis.cache.RedisCacheConfiguration config = org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()
                // 设置缓存的默认过期时间
                .entryTtl(Duration.ofSeconds(180))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(this.jackson2JsonRedisSerializer()))
                // 不缓存空值
                .disableCachingNullValues()
                ;
        //根据redis缓存配置和reid连接工厂生成redis缓存管理器
        RedisCacheManager  redisCacheConfiguration = RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
        return redisCacheConfiguration;
    }

    /***
     * 配置jackson2JsonRedisSerializer
     * @return
     */
    private Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(JacksonMapper.mappers());
        return jackson2JsonRedisSerializer;
    }


    /**
     * 重新定义KeyGenerator.
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }
}