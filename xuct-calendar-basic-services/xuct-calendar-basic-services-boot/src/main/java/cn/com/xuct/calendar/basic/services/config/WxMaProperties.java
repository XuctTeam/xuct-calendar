/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: WxMaProperties
 * Author:   Derek Xu
 * Date:     2021/11/9 16:07
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.basic.services.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/9
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxMaProperties {

    private List<Config> configs;

    @Data
    public static class Config {
        /**
         * 设置微信小程序的appid
         */
        private String appid;

        /**
         * 设置微信小程序的Secret
         */
        private String secret;

        /**
         * 设置微信小程序消息服务器配置的token
         */
        private String token;

        /**
         * 设置微信小程序消息服务器配置的EncodingAESKey
         */
        private String aesKey;

        /**
         * 消息格式，XML或者JSON
         */
        private String msgDataFormat;
    }

    /**
     * 功能描述: <br>
     * 〈获取默认appId〉
     *
     * @param
     * @return:java.lang.String
     * @since: 1.0.0
     * @Author:
     * @Date: 2021/11/9 16:13
     */
    public String getAppId() {

        if (CollectionUtils.isEmpty(configs)) {
            return null;
        }
        return this.configs.get(0).getAppid();
    }
}