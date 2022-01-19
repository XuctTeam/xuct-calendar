/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: RedisProperties
 * Author:   Derek Xu
 * Date:     2021/11/9 16:32
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.redis.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/9
 * @since 1.0.0
 */
@Data
@ConfigurationProperties("spring.redis")
public class RedisProperties {

    private String host;

    private Integer port;

    private String password;

    private Integer database;

    private int timeout;
}