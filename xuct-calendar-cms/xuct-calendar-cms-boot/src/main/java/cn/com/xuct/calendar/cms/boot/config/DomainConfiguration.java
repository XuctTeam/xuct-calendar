/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: DomainConfiguration
 * Author:   Derek Xu
 * Date:     2022/5/5 17:56
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/5/5
 * @since 1.0.0
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "domain")
public class DomainConfiguration {

    private List<Short> shortList;

    private String images;


    @Data
    public static class Short{

        private String domain;

        private String notFound;

        private String type;
    }
}

