/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: WxApplication
 * Author:   Derek Xu
 * Date:     2021/11/9 15:41
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot;

import cn.com.xuct.calendar.common.http.annotation.EnableAutoOkFeign;
import cn.com.xuct.calendar.common.redis.annotation.EnableAutoRedis;
import cn.com.xuct.calendar.common.smms.annotation.EnableSmmsClient;
import cn.com.xuct.calendar.common.swagger.annotation.EnableAutoSwagger2;
import cn.com.xuct.calendar.ums.api.feign.CalendarFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/9
 * @since 1.0.0
 */
@EnableAutoRedis
@EnableAutoSwagger2
@EnableAutoOkFeign
@ComponentScan(basePackages = {"cn.com.xuct.calendar.dao", "cn.com.xuct.calendar.service", "cn.com.xuct.calendar.ums.boot"})
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableTransactionManagement
@EnableSmmsClient
@EnableFeignClients(basePackageClasses = {CalendarFeignClient.class})
public class UmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UmsApplication.class, args);
    }
}