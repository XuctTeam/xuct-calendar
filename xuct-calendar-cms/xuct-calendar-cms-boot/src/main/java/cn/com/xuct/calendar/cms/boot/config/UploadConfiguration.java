/**
 * Copyright (C), 2000-2022, 263企业通信
 * FileName: UploadConfiguration
 * Author:   Derek Xu
 * Date:     2022/5/30 10:13
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/5/30
 * @since 1.0.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "upload")
public class UploadConfiguration {

    private int maxNumber;
}