package cn.com.xuct.calendar.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lengleng
 * @date 2022/3/26
 * <p>
 * swagger 3.0 展示
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class SpringDocConfiguration implements InitializingBean {

    private final SwaggerUiConfigProperties swaggerUiConfigProperties;

    private final SwaggerDocProperties swaggerProperties;

    // TODO 从配置文件读取或者从gateway路由中读取
    @Override
    public void afterPropertiesSet() {
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> swaggerUrlSet = new HashSet<>();
        for (String value : swaggerProperties.getServices().values()) {
            AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl();
            swaggerUrl.setName(value);
            swaggerUrl.setUrl(value.concat("/v3/api-docs"));
            swaggerUrlSet.add(swaggerUrl);
        }
        swaggerUiConfigProperties.setUrls(swaggerUrlSet);
    }
}
