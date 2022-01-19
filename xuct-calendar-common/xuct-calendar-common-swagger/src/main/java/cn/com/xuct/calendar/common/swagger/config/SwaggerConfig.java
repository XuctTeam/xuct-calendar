/**
 * Copyright (C), 2021-2021, 263
 * FileName: SwaggerConfig
 * Author:   Derek xu
 * Date:     2021/6/9 12:58
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.swagger.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Administrator
 * @create 2021/6/9
 * @since 1.0.0
 */
@Data
@ConfigurationProperties("swagger")
public class SwaggerConfig {

    /**
     * swagger会解析的包路径
     **/
    private String basePackages = "";


    private String path = "";

    /**
     * 标题
     **/
    private String title = "";

    /**
     * 描述
     **/
    private String description = "";

    /**
     * 版本
     **/
    private String version = "";

    /**
     * 许可证
     **/
    private String license = "";

    /**
     * 许可证URL
     **/
    private String licenseUrl = "";

    /**
     * 服务条款URL
     **/
    private String termsOfServiceUrl = "";

    /**
     * host信息
     **/
    private String host = "";


    private Oauth2 oauth2 = new Oauth2();

    /**
     * 联系人信息
     */
    private Contact contact = new Contact();


    @Data
    @NoArgsConstructor
    public static class Contact {

        /**
         * 联系人
         **/
        private String name = "";

        /**
         * 联系人url
         **/
        private String url = "";

        /**
         * 联系人email
         **/
        private String email = "";

    }

    @Data
    @NoArgsConstructor
    public static class Oauth2 {

        private boolean enable = false;

        private String passwdUrl;
    }
}