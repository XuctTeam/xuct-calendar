/**
 * Copyright (C), 2015-2023, 楚恬商场
 * FileName: WxMaConfiguration
 * Author:   Derek Xu
 * Date:     2023/2/4 15:33
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
 * @create 2023/2/4
 * @since 1.0.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wx.ma")
public class WxMaConfiguration {


    private String envVersion;

    private Share share;

    private String qrWidth;
    @Data
    public static class Share {

        private String calendarPage;

        private String componentPage;
    }
}