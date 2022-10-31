package cn.com.xuct.calendar.gateway.config;

import cn.com.xuct.calendar.gateway.filter.PasswordDecoderFilter;
import cn.com.xuct.calendar.gateway.filter.RequestGlobalFilter;
import cn.com.xuct.calendar.gateway.filter.SwaggerBasicGatewayFilter;
import cn.com.xuct.calendar.gateway.filter.ValidateCodeGatewayFilter;
import cn.com.xuct.calendar.gateway.handler.GlobalExceptionHandler;
import cn.com.xuct.calendar.gateway.handler.ImageCodeHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 网关配置
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(GatewayConfigProperties.class)
public class GatewayConfiguration {

	@Bean
	public PasswordDecoderFilter passwordGatewayFilter(GatewayConfigProperties configProperties) {
		return new PasswordDecoderFilter(configProperties);
	}

	@Bean
	@ConditionalOnProperty(name = "swagger.basic.enabled")
	public SwaggerBasicGatewayFilter swaggerBasicGatewayFilter(
			SpringDocConfiguration.SwaggerDocProperties swaggerProperties) {
		return new SwaggerBasicGatewayFilter(swaggerProperties);
	}

	@Bean
	public RequestGlobalFilter requestGlobalFilter() {
		return new RequestGlobalFilter();
	}


	@Bean
	public GlobalExceptionHandler globalExceptionHandler(ObjectMapper objectMapper) {
		return new GlobalExceptionHandler(objectMapper);
	}

	@Bean
	public ImageCodeHandler imageCodeHandler(RedisTemplate redisTemplate) {
		return new ImageCodeHandler(redisTemplate);
	}

	@Bean
	public ValidateCodeGatewayFilter validateCodeGatewayFilter(GatewayConfigProperties configProperties,
                                                               ObjectMapper objectMapper, RedisTemplate redisTemplate) {
		return new ValidateCodeGatewayFilter(configProperties, objectMapper, redisTemplate);
	}
}
