/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: AuthorizationServerConfiguration
 * Author:   Derek Xu
 * Date:     2021/11/12 17:10
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot.config;

import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.uaa.boot.handler.AuthenticationFailureEventHandler;
import cn.com.xuct.calendar.uaa.boot.handler.AuthenticationSuccessEventHandler;
import cn.com.xuct.calendar.uaa.boot.support.CustomeOAuth2AccessTokenGenerator;
import cn.com.xuct.calendar.uaa.boot.support.core.CustomeOAuth2TokenCustomizer;
import cn.com.xuct.calendar.uaa.boot.support.core.FormIdentityLoginConfigurer;
import cn.com.xuct.calendar.uaa.boot.support.core.OAuthDaoAuthenticationProvider;
import cn.com.xuct.calendar.uaa.boot.support.password.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import cn.com.xuct.calendar.uaa.boot.support.password.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import cn.com.xuct.calendar.uaa.boot.support.phone.OAuth2ResourceOwnerPhoneAuthenticationConverter;
import cn.com.xuct.calendar.uaa.boot.support.phone.OAuth2ResourceOwnerPhoneAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/12
 * @since 1.0.0
 */
@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfiguration  {

    private final OAuth2AuthorizationService authorizationService;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();

        http.apply(authorizationServerConfigurer
                .tokenEndpoint((tokenEndpoint) -> {// 个性化认证授权端点
                tokenEndpoint.accessTokenRequestConverter(accessTokenRequestConverter()) // 注入自定义的授权认证Converter
                    .accessTokenResponseHandler(new AuthenticationSuccessEventHandler()) // 登录成功处理器
                    .errorResponseHandler(new AuthenticationFailureEventHandler());// 登录失败处理器
        }).clientAuthentication(oAuth2ClientAuthenticationConfigurer -> // 个性化客户端认证
                        oAuth2ClientAuthenticationConfigurer.errorResponseHandler(new AuthenticationFailureEventHandler()))// 处理客户端认证异常
                .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint// 授权码端点个性化confirm页面
                        .consentPage(SecurityConstants.CUSTOM_CONSENT_PAGE_URI)));

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        DefaultSecurityFilterChain securityFilterChain = http.requestMatcher(endpointsMatcher)
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                )
                .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .apply(authorizationServerConfigurer.authorizationService(authorizationService)// redis存储token的实现
                        .providerSettings(ProviderSettings.builder().issuer(SecurityConstants.PROJECT_LICENSE).build()))
                // 授权码登录的登录页个性化
                .and().apply(new FormIdentityLoginConfigurer()).and().build();

        // 注入自定义授权模式实现
        addCustomOAuth2GrantAuthenticationProvider(http);
        return securityFilterChain;
    }


    /**
     * request -> xToken 注入请求转换器
     *
     * @return DelegatingAuthenticationConverter
     */
    private AuthenticationConverter accessTokenRequestConverter() {
        return new DelegatingAuthenticationConverter(Arrays.asList(
                new OAuth2ResourceOwnerPasswordAuthenticationConverter(),
                new OAuth2ResourceOwnerPhoneAuthenticationConverter(),
                new OAuth2RefreshTokenAuthenticationConverter(),
                new OAuth2ClientCredentialsAuthenticationConverter(),
                new OAuth2AuthorizationCodeAuthenticationConverter(),
                new OAuth2AuthorizationCodeRequestAuthenticationConverter()));
    }


    /**
     * 令牌生成规则实现 </br>
     * client:username:uuid
     * @return OAuth2TokenGenerator
     */
    @Bean
    public OAuth2TokenGenerator oAuth2TokenGenerator() {
        CustomeOAuth2AccessTokenGenerator accessTokenGenerator = new CustomeOAuth2AccessTokenGenerator();
        // 注入Token 增加关联用户信息
        accessTokenGenerator.setAccessTokenCustomizer(new CustomeOAuth2TokenCustomizer());
        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, new OAuth2RefreshTokenGenerator());
    }

    /**
     * 注入授权模式实现提供方
     *
     * 1. 密码模式 </br>
     * 2. 短信登录 </br>
     *
     */
    @SuppressWarnings("unchecked")
    private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

        OAuth2ResourceOwnerPasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider = new OAuth2ResourceOwnerPasswordAuthenticationProvider(
                authenticationManager, authorizationService, oAuth2TokenGenerator());

        OAuth2ResourceOwnerPhoneAuthenticationProvider resourceOwnerSmsAuthenticationProvider = new OAuth2ResourceOwnerPhoneAuthenticationProvider(
                authenticationManager, authorizationService, oAuth2TokenGenerator());

        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new OAuthDaoAuthenticationProvider());
        // 处理 OAuth2ResourceOwnerPasswordAuthenticationToken
        http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);
        // 处理 OAuth2ResourceOwnerSmsAuthenticationToken
        http.authenticationProvider(resourceOwnerSmsAuthenticationProvider);
    }
}