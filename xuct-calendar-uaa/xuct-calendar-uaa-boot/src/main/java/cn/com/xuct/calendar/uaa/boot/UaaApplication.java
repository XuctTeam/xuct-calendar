/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: AuthApplication
 * Author:   Derek Xu
 * Date:     2021/11/15 11:50
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot;

import cn.com.xuct.calendar.common.http.annotation.EnableOkFeignAutoConfiguration;
import cn.com.xuct.calendar.common.redis.annotation.EnableRedisAutoConfiguration;
import cn.com.xuct.calendar.uaa.boot.support.feign.ClientDetailsFeignClient;
import cn.com.xuct.calendar.uaa.boot.support.feign.MemberFeignClient;
import cn.com.xuct.calendar.common.swagger.annotation.EnableSwagger2AutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/15
 * @since 1.0.0
 */
@EnableRedisAutoConfiguration
@EnableSwagger2AutoConfiguration
@EnableOkFeignAutoConfiguration
@EnableCaching
@SpringBootApplication(scanBasePackages = {"cn.com.xuct.calendar.uaa.boot", "cn.com.xuct.calendar.common.web"})
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = {ClientDetailsFeignClient.class , MemberFeignClient.class})
public class UaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(UaaApplication.class, args);
    }
}