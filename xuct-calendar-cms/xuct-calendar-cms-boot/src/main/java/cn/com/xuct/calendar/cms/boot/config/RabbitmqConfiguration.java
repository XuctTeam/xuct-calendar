/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: RabbitmqConfiguration
 * Author:   Administrator
 * Date:     2022/1/20 15:45
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/20
 * @since 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitmqConfiguration {


    private Long maxDelay;
}