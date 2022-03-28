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
package cn.com.xuct.calendar.inner.services;

import cn.com.xuct.calendar.common.swagger.annotation.EnableAutoSwagger2;
import cn.com.xuct.calendar.common.tencent.annotation.EnableAutoTencentSdk;
import com.sankuai.inf.leaf.plugin.annotation.EnableLeafServer;
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
@EnableLeafServer
@EnableAutoSwagger2
@EnableAutoTencentSdk
public class InnerServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(InnerServicesApplication.class, args);
    }
}