/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: InnerServicesApplication
 * Author:   Derek Xu
 * Date:     2022/3/28 11:19
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.basic.services;

import cn.com.xuct.calendar.common.redis.annotation.EnableRedisAutoConfiguration;
import cn.com.xuct.calendar.common.swagger.annotation.EnableSwagger2AutoConfiguration;
import cn.com.xuct.calendar.common.tencent.annotation.EnableAutoTencentSdk;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/28
 * @since 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2AutoConfiguration
@EnableRedisAutoConfiguration
@EnableAutoTencentSdk
public class BasicServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicServicesApplication.class, args);
    }

}