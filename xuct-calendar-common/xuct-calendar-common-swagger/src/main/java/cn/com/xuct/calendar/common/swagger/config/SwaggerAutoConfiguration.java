/**
 * Copyright (C), 2021-2021, 263
 * FileName: SwaggerAutoConfiguration
 * Author:   Derek xu
 * Date:     2021/6/9 13:00
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.swagger.config;

import cn.com.xuct.calendar.common.swagger.support.SwaggerProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Administrator
 * @create 2021/6/9
 * @since 1.0.0
 */
@RequiredArgsConstructor
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
@ConditionalOnMissingClass("org.springframework.cloud.gateway.config.GatewayAutoConfiguration")
public class SwaggerAutoConfiguration {
    private final SwaggerProperties swaggerProperties;

    private final ServiceInstance serviceInstance;

    @Bean
    public OpenAPI springOpenAPI() {
        OpenAPI openAPI = new OpenAPI().info(new Info().title(swaggerProperties.getTitle()));
        // oauth2.0 password
        openAPI.schemaRequirement(HttpHeaders.AUTHORIZATION, this.securityScheme());
        // servers
        List<Server> serverList = new ArrayList<>();
        String path = swaggerProperties.getServices().get(serviceInstance.getServiceId());
        serverList.add(new Server().url(swaggerProperties.getGateway() + "/" + path));
        openAPI.servers(serverList);
        return openAPI;
    }

    private SecurityScheme securityScheme() {
        OAuthFlow clientCredential = new OAuthFlow();
        clientCredential.setTokenUrl(swaggerProperties.getTokenUrl());
        clientCredential.setScopes(new Scopes().addString(swaggerProperties.getScope(), swaggerProperties.getScope()));
        OAuthFlows oauthFlows = new OAuthFlows();
        oauthFlows.password(clientCredential);
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.setType(SecurityScheme.Type.OAUTH2);
        securityScheme.setFlows(oauthFlows);
        return securityScheme;
    }
}