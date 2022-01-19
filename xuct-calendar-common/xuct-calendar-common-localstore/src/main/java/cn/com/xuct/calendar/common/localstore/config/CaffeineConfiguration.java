/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: CaffeineConfiguration
 * Author:   Derek Xu
 * Date:     2021/11/18 8:48
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.localstore.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/18
 * @since 1.0.0
 */
@EnableCaching
@Configuration
public class CaffeineConfiguration {


    @Bean
    public Caffeine caffeineConfig() {
        return Caffeine.newBuilder().initialCapacity(100)//初始大小
                .maximumSize(200)//最大数量
                .expireAfterWrite(60, TimeUnit.MINUTES);
    }

    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }

}