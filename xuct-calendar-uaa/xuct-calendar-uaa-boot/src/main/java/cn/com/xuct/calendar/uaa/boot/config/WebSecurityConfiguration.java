/**
 * Copyright (C), 2015-2023, 263企业通信
 * FileName: WebSecurityConfiguration
 * Author:   Derek Xu
 * Date:     2023/10/8 17:56
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot.config;


import cn.com.xuct.calendar.uaa.boot.support.core.FormIdentityLoginConfigurer;
import cn.com.xuct.calendar.uaa.boot.support.core.OAuthDaoAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.web.SecurityFilterChain;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2023/10/8
 * @since 1.0.0
 */
@EnableWebSecurity
public class WebSecurityConfiguration {

    /**
     * spring security 默认的安全策略
     *
     * @param http security注入点
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/token/*")
                .permitAll()// 开放自定义的部分端点
                .anyRequest()
                .authenticated()).headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)// 避免iframe同源无法登录许iframe
        ).apply(new FormIdentityLoginConfigurer()); // 表单登录个性化
        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new OAuthDaoAuthenticationProvider());
        return http.build();
    }

    /**
     * 暴露静态资源
     * <p>
     * https://github.com/spring-projects/spring-security/issues/10938
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
        http.securityMatchers((matchers) -> matchers.requestMatchers("/actuator/**", "/css/**", "/error"))
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
                .requestCache(RequestCacheConfigurer::disable)
                .securityContext(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable);
        return http.build();
    }
}