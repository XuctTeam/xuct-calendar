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

import cn.com.xuct.calendar.common.db.dao.config.MybatisPlusConfig;
import cn.com.xuct.calendar.common.http.annotation.EnableOkFeignAutoConfiguration;
import cn.com.xuct.calendar.common.redis.annotation.EnableRedisAutoConfiguration;
import cn.com.xuct.calendar.common.security.annotation.EnableOAuthResourceServer;
import cn.com.xuct.calendar.common.smms.annotation.EnableSmmsClient;
import cn.com.xuct.calendar.common.swagger.annotation.EnableSwagger2AutoConfiguration;
import cn.com.xuct.calendar.common.web.utils.SpringContextHolder;
import cn.com.xuct.calendar.ums.api.feign.CalendarFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/9
 * @since 1.0.0
 */
@EnableRedisAutoConfiguration
@EnableSwagger2AutoConfiguration
@EnableOkFeignAutoConfiguration
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableTransactionManagement
@EnableSmmsClient
@EnableAsync
@EnableOAuthResourceServer
@Import({MybatisPlusConfig.class, SpringContextHolder.class})
@EnableFeignClients(basePackageClasses = {CalendarFeignClient.class})
public class UmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UmsApplication.class, args);
    }
}