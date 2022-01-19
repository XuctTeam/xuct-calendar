/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: GatewayApplication
 * Author:   Derek Xu
 * Date:     2021/11/12 13:44
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.gateway;

import cn.com.xuct.calendar.common.redis.annotation.EnableAutoRedis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/12
 * @since 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoRedis
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}