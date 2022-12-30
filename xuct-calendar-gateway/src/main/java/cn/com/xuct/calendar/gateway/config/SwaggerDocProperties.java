/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: SwaggerDocProperties
 * Author:   Derek Xu
 * Date:     2022/12/30 18:27
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/12/30
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties("swagger")
public class SwaggerDocProperties {

    private Map<String, String> services;

    /**
     * 认证参数
     */
    private SwaggerBasic basic = new SwaggerBasic();

    @Data
    public class SwaggerBasic {

        /**
         * 是否开启 basic 认证
         */
        private Boolean enabled;

        /**
         * 用户名
         */
        private String username;

        /**
         * 密码
         */
        private String password;

    }
}