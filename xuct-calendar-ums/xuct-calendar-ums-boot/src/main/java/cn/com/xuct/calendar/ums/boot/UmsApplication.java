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
import cn.com.xuct.calendar.common.http.annotation.EnableAutoFeignClients;
import cn.com.xuct.calendar.common.redis.annotation.EnableRedisAutoConfiguration;
import cn.com.xuct.calendar.common.security.annotation.EnableOAuthResourceServer;
import cn.com.xuct.calendar.common.imgurl.annotation.EnableImgUrlClient;
import cn.com.xuct.calendar.common.swagger.annotation.EnableApiDoc;
import cn.com.xuct.calendar.common.core.utils.SpringContextHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
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
@EnableApiDoc
@EnableAutoFeignClients
@SpringBootApplication
@ServletComponentScan
@EnableDiscoveryClient
@EnableCaching
@EnableTransactionManagement
@EnableImgUrlClient
@EnableAsync
@EnableOAuthResourceServer
@Import({MybatisPlusConfig.class, SpringContextHolder.class})
public class UmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UmsApplication.class, args);
    }
}