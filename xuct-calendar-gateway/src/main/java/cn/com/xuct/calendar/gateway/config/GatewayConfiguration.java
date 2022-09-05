package cn.com.xuct.calendar.gateway.config;

import cn.com.xuct.calendar.gateway.filter.PasswordGatewayFilter;
import cn.com.xuct.calendar.gateway.filter.RequestGlobalFilter;
import cn.com.xuct.calendar.gateway.handler.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关配置
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(GatewayConfigProperties.class)
public class GatewayConfiguration {

	@Bean
	public PasswordGatewayFilter passwordGatewayFilter(GatewayConfigProperties configProperties) {
		return new PasswordGatewayFilter(configProperties);
	}

	@Bean
	public RequestGlobalFilter pigRequestGlobalFilter() {
		return new RequestGlobalFilter();
	}



	@Bean
	public GlobalExceptionHandler globalExceptionHandler(ObjectMapper objectMapper) {
		return new GlobalExceptionHandler(objectMapper);
	}
}
