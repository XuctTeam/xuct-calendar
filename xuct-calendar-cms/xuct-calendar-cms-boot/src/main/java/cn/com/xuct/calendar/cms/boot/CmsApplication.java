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

import cn.com.xuct.calendar.common.db.dao.config.MybatisPlusConfig;
import cn.com.xuct.calendar.common.http.annotation.EnableOkFeignAutoConfiguration;
import cn.com.xuct.calendar.common.redis.annotation.EnableRedisAutoConfiguration;
import cn.com.xuct.calendar.common.security.annotation.EnableOAuthResourceServer;
import cn.com.xuct.calendar.common.swagger.annotation.EnableApiDoc;
import cn.com.xuct.calendar.common.web.utils.SpringContextHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
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
@EnableRedisAutoConfiguration
@EnableApiDoc
@EnableOkFeignAutoConfiguration
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableTransactionManagement
@EnableAsync
@EnableOAuthResourceServer
@ComponentScan(basePackages = {"cn.com.xuct.calendar.cms.boot" , "cn.com.xuct.calendar.cms.queue"})
@Import({MybatisPlusConfig.class, SpringContextHolder.class})
public class CmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsApplication.class, args);
    }
}