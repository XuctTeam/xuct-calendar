/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: SmmsProperties
 * Author:   Derek Xu
 * Date:     2022/2/13 8:55
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.imgurl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/13
 * @since 1.0.0
 */
@Data
@Configuration
@ConfigurationProperties("smms")
public class ImgURLProperties {

    private String token;

    private String url;
}