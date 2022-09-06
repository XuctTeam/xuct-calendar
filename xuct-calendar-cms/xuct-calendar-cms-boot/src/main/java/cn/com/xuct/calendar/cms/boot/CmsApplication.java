/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: DavApplication
 * Author:   Derek Xu
 * Date:     2021/11/24 15:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot;

import cn.com.xuct.calendar.cms.api.feign.UmsMemberFeignClient;
import cn.com.xuct.calendar.common.db.dao.config.MybatisPlusConfig;
import cn.com.xuct.calendar.common.fdfs.annotation.EnableFdfsAutoConfiguration;
import cn.com.xuct.calendar.common.http.annotation.EnableOkFeignAutoConfiguration;
import cn.com.xuct.calendar.common.security.annotation.EnableOAuthResourceServer;
import cn.com.xuct.calendar.common.swagger.annotation.EnableSwagger2AutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/24
 * @since 1.0.0
 */
@EnableAsync
@ComponentScan(basePackages = {
        "cn.com.xuct.calendar.common.web",
        "cn.com.xuct.calendar.cms.queue",
        "cn.com.xuct.calendar.cms.boot"})
@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2AutoConfiguration
@EnableFdfsAutoConfiguration
@EnableTransactionManagement
@Import(MybatisPlusConfig.class)
@EnableOkFeignAutoConfiguration
@EnableOAuthResourceServer
public class CmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsApplication.class, args);
    }
}