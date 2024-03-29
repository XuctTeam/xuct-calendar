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
import cn.com.xuct.calendar.uaa.boot.support.core.CustomizerOAuth2TokenCustomizer;
import cn.com.xuct.calendar.uaa.boot.support.core.FormIdentityLoginConfigurer;
import cn.com.xuct.calendar.uaa.boot.support.core.OAuthDaoAuthenticationProvider;
import cn.com.xuct.calendar.uaa.boot.support.password.BaseOauth2ResourceOwnerPasswordAuthenticationConverter;
import cn.com.xuct.calendar.uaa.boot.support.password.BaseOauth2ResourceOwnerPasswordAuthenticationProvider;
import cn.com.xuct.calendar.uaa.boot.support.phone.BaseOauth2ResourceOwnerPhoneAuthenticationConverter;
import cn.com.xuct.calendar.uaa.boot.support.phone.BaseOauth2ResourceOwnerPhoneAuthenticationProvider;
import cn.com.xuct.calendar.uaa.boot.support.wx.BaseOauth2ResourceOwnerWxAuthenticationConverter;
import cn.com.xuct.calendar.uaa.boot.support.wx.BaseOauth2ResourceOwnerWxAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
public class AuthorizationServerConfiguration {

    private final OAuth2AuthorizationService authorizationService;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        http.apply(authorizationServerConfigurer.tokenEndpoint((tokenEndpoint) -> {// 个性化认证授权端点
                    tokenEndpoint.accessTokenRequestConverter(accessTokenRequestConverter()) // 注入自定义的授权认证Converter
                            .accessTokenResponseHandler(new AuthenticationSuccessEventHandler()) // 登录成功处理器
                            .errorResponseHandler(new AuthenticationFailureEventHandler());// 登录失败处理器
                }).clientAuthentication(oAuth2ClientAuthenticationConfigurer -> // 个性化客户端认证
                        oAuth2ClientAuthenticationConfigurer.errorResponseHandler(new AuthenticationFailureEventHandler()))// 处理客户端认证异常
                .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint// 授权码端点个性化confirm页面
                        .consentPage(SecurityConstants.CUSTOM_CONSENT_PAGE_URI)));

        AntPathRequestMatcher[] requestMatchers = new AntPathRequestMatcher[]{
                AntPathRequestMatcher.antMatcher("/token/**"), AntPathRequestMatcher.antMatcher("/actuator/**"),
                AntPathRequestMatcher.antMatcher("/css/**"), AntPathRequestMatcher.antMatcher("/error")};

        http.authorizeHttpRequests(authorizeRequests -> {
                    // 自定义接口、端点暴露
                    authorizeRequests.requestMatchers(requestMatchers).permitAll();
                    authorizeRequests.anyRequest().authenticated();
                })
                .apply(authorizationServerConfigurer.authorizationService(authorizationService)// redis存储token的实现
                        .authorizationServerSettings(
                                AuthorizationServerSettings.builder().issuer(SecurityConstants.PROJECT_LICENSE).build()));
        http.apply(new FormIdentityLoginConfigurer());
        DefaultSecurityFilterChain securityFilterChain = http.build();

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
                new BaseOauth2ResourceOwnerPasswordAuthenticationConverter(),
                new BaseOauth2ResourceOwnerPhoneAuthenticationConverter(),
                new BaseOauth2ResourceOwnerWxAuthenticationConverter(),
                new OAuth2RefreshTokenAuthenticationConverter(),
                new OAuth2ClientCredentialsAuthenticationConverter(),
                new OAuth2AuthorizationCodeAuthenticationConverter(),
                new OAuth2AuthorizationCodeRequestAuthenticationConverter()));
    }

    /**
     * 令牌生成规则实现 </br>
     * client:username:uuid
     *
     * @return OAuth2TokenGenerator
     */
    @Bean
    public OAuth2TokenGenerator oAuth2TokenGenerator() {
        CustomeOAuth2AccessTokenGenerator accessTokenGenerator = new CustomeOAuth2AccessTokenGenerator();
        // 注入Token 增加关联用户信息
        accessTokenGenerator.setAccessTokenCustomizer(new CustomizerOAuth2TokenCustomizer());
        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, new OAuth2RefreshTokenGenerator());
    }


    /**
     * 注入授权模式实现提供方
     * <p>
     * 1. 密码模式 </br>
     * 2. 短信登录 </br>
     */
    @SuppressWarnings("unchecked")
    private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

        BaseOauth2ResourceOwnerPasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider = new BaseOauth2ResourceOwnerPasswordAuthenticationProvider(authenticationManager, authorizationService, oAuth2TokenGenerator());

        BaseOauth2ResourceOwnerPhoneAuthenticationProvider resourceOwnerPhoneAuthenticationProvider = new BaseOauth2ResourceOwnerPhoneAuthenticationProvider(authenticationManager, authorizationService, oAuth2TokenGenerator());

        BaseOauth2ResourceOwnerWxAuthenticationProvider resourceOwnerWxAuthenticationProvider = new BaseOauth2ResourceOwnerWxAuthenticationProvider(authenticationManager, authorizationService, oAuth2TokenGenerator());
        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new OAuthDaoAuthenticationProvider());
        // 处理 OAuth2ResourceOwnerPasswordAuthenticationToken
        http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);
        // 处理 OAuth2ResourceOwnerSmsAuthenticationToken
        http.authenticationProvider(resourceOwnerPhoneAuthenticationProvider);
        // 处理 OAuth2ResourceOwnerWxAuthenticationToken
        http.authenticationProvider(resourceOwnerWxAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoderFactories() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}