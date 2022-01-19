/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: FeignConfiguration
 * Author:   Derek Xu
 * Date:     2021/11/16 9:23
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.web.web;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/16
 * @since 1.0.0
 */
@Configuration
public class FeignConfiguration{


    @Bean
    public FeignBasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new FeignBasicAuthRequestInterceptor();
    }


    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}