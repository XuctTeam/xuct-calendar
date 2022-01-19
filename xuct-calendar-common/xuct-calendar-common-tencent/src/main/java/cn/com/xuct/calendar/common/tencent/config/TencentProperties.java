/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: TencentProperties
 * Author:   Derek Xu
 * Date:     2021/11/29 15:46
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.tencent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/29
 * @since 1.0.0
 */
@Data
@ConfigurationProperties("tencent")
public class TencentProperties {

    private String secretId;

    private String secretKey;

    /* 短信 */
    private Sms sms;


    @Data
    public static class Sms {

        private String appId;

        private String sign;

        private Map<String, String> templateId;
    }
}