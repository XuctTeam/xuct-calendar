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
import cn.com.xuct.calendar.uaa.boot.support.core.OauthDaoAuthenticationProvider;
import cn.com.xuct.calendar.uaa.boot.support.password.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import cn.com.xuct.calendar.uaa.boot.support.password.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import cn.com.xuct.calendar.uaa.boot.support.phone.OAuth2ResourceOwnerPhoneAuthenticationConverter;
import cn.com.xuct.calendar.uaa.boot.support.phone.OAuth2ResourceOwnerPhoneAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.sql.DataSource;
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

//    private final AuthenticationManager authenticationManager;
//
//    private final DataSource dataSource;
//
//    private final SysUserDetailsService sysUserDetailsService;
//
//    private final MemberUserDetailsService memberUserDetailsService;
//
//    private final StringRedisTemplate stringRedisTemplate;
//
//    private final PasswordEncoder passwordEncoder;

    private final OAuth2AuthorizationService authorizationService;



    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();

        http.apply(authorizationServerConfigurer.tokenEndpoint((tokenEndpoint) -> {// 个性化认证授权端点
            tokenEndpoint.accessTokenRequestConverter(accessTokenRequestConverter()) // 注入自定义的授权认证Converter
                    .accessTokenResponseHandler(new AuthenticationSuccessEventHandler()) // 登录成功处理器
                    .errorResponseHandler(new AuthenticationFailureEventHandler());// 登录失败处理器
        }).clientAuthentication(oAuth2ClientAuthenticationConfigurer -> // 个性化客户端认证
                        oAuth2ClientAuthenticationConfigurer.errorResponseHandler(new AuthenticationFailureEventHandler()))// 处理客户端认证异常
                .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint// 授权码端点个性化confirm页面
                        .consentPage(SecurityConstants.CUSTOM_CONSENT_PAGE_URI)));

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        DefaultSecurityFilterChain securityFilterChain = http.requestMatcher(endpointsMatcher)
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
        http.authenticationProvider(new OauthDaoAuthenticationProvider());
        // 处理 OAuth2ResourceOwnerPasswordAuthenticationToken
        http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);
        // 处理 OAuth2ResourceOwnerSmsAuthenticationToken
        http.authenticationProvider(resourceOwnerSmsAuthenticationProvider);
    }



//    /**
//     * 配置允许访问此认证服务器的客户端信息
//     * 1、内存方式
//     * 2、数据库方式
//     *
//     * @param clients
//     * @throws Exception
//     */
//    @SneakyThrows
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) {
//        //JDBC管理客户端
//        clients.withClientDetails(authClientDetailsService());
//    }
//
//
//    /**
//     * 关于认证服务器端点配置
//     *
//     * @param endpoints
//     * @throws Exception
//     */
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        // 获取原有默认授权模式(授权码模式、密码模式、客户端模式、简化模式)的授权者
//        List<TokenGranter> granterList = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));
//
//        // 添加授权码模式
//        granterList.add(new AuthorizationCodeTokenGranter(endpoints.getTokenServices(), endpoints.getAuthorizationCodeServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
//        // 添加刷新令牌的模式
//        granterList.add(new RefreshTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
//        // 添加隐式授权模式
//        granterList.add(new ImplicitTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
//        // 添加客户端模式
//        granterList.add(new ClientCredentialsTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
//        // 添加验证码授权模式授权者
//        granterList.add(new CaptchaTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), authenticationManager, stringRedisTemplate));
//        // 添加手机短信验证码授权模式的授权者
//        granterList.add(new PhoneTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), authenticationManager));
//        // 添加微信授权模式的授权者
//        granterList.add(new WechatTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), authenticationManager));
//
//        CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(granterList);
//
//        //密码模式需要这个实例
//        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
//                .authenticationManager(authenticationManager)
//                .accessTokenConverter(jwtAccessTokenConverter())
//                .tokenGranter(compositeTokenGranter)
//                .tokenEnhancer(tokenEnhancer())
//                .tokenServices(tokenServices(endpoints))
//                /** refresh token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
//                 *  1 重复使用：access token过期刷新时， refresh token过期时间未改变，仍以初次生成的时间为准
//                 *  2 非重复使用：access token过期刷新时， refresh token过期时间延续，在refresh token有效期内刷新便永不失效达到无需再次登录的目的
//                 */
//                .reuseRefreshTokens(true)
//                .exceptionTranslator(new AuthWebResponseExceptionTranslator());
//    }
//
//    /**
//     * 令牌安全端点配置
//     *
//     * @param security
//     * @throws Exception
//     */
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) {
//        security.passwordEncoder(passwordEncoder);
//        // 开启/oauth/check_token验证端口认证权限访问
//        security.checkTokenAccess("isAuthenticated()");
//        // 开启/oauth/token_key验证端口无权限访问
//        security.tokenKeyAccess("permitAll()");
//        AuthBasicAuthenticationFilter authBasicAuthenticationFilter = new AuthBasicAuthenticationFilter(authClientDetailsService());
//        authBasicAuthenticationFilter.setPasswordEncoder(passwordEncoder);
//        /*
//         *
//         * 主要是让/oauth/token支持client_id和client_secret做登陆认证
//         * 如果开启了allowFormAuthenticationForClients，那么就在BasicAuthenticationFilter之前
//         * 添加ClientCredentialsTokenEndpointFilter,使用ClientDetailsUserDetailsService来进行登陆认证
//         *
//         * 前后分离返回统一json不开启
//         */
//        security.addTokenEndpointAuthenticationFilter(authBasicAuthenticationFilter);
//    }
//
//    /**
//     * 功能描述: <br>
//     * 〈〉
//     *
//     * @param
//     * @return:org.springframework.security.oauth2.provider.ClientDetailsService
//     * @since: 1.0.0
//     * @Author:Derek Xu
//     * @Date: 2021/11/15 9:06
//     */
//    @Bean
//    public ClientDetailsService authClientDetailsService() {
//        AuthClientDetailsService clientDetailsService = new AuthClientDetailsService(dataSource);
//        clientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
//        clientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
//        clientDetailsService.setPasswordEncoder(passwordEncoder);
//        return clientDetailsService;
//    }
//
//
//    /**
//     * JWT加密
//     *
//     * @return
//     */
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
//        jwtAccessTokenConverter.setKeyPair(keyPair());
//        return jwtAccessTokenConverter;
//    }
//
//
//    /**
//     * B
//     * 密钥库中获取密钥对(公钥+私钥)
//     */
//    @Bean
//    public KeyPair keyPair() {
//        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("oauth2.jks"), "oauth2".toCharArray());
//        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("oauth2");
//        return keyPair;
//    }
//
//
//    @Bean
//    public TokenEnhancer tokenEnhancer() {
//        return (accessToken, authentication) -> {
//            Map<String, Object> additionalInfo = new HashMap<>();
//            Object principal = authentication.getUserAuthentication().getPrincipal();
//            if (principal instanceof SysUserDetails) {
//                SysUserDetails sysUserDetails = (SysUserDetails) principal;
//                additionalInfo.put(SecurityConstants.USER_ID_KEY, sysUserDetails.getUserId());
//                additionalInfo.put(SecurityConstants.USER_NAME_KEY, sysUserDetails.getUsername());
//                if (StrUtil.isNotBlank(sysUserDetails.getAuthenticationMethod())) {
//                    additionalInfo.put("authenticationMethod", sysUserDetails.getAuthenticationMethod());
//                }
//            } else if (principal instanceof MemberUserDetails) {
//                MemberUserDetails memberUserDetails = (MemberUserDetails) principal;
//                additionalInfo.put(SecurityConstants.USER_ID_KEY, memberUserDetails.getUserId());
//                additionalInfo.put(SecurityConstants.USER_NAME_KEY, memberUserDetails.getUsername());
//                additionalInfo.put(SecurityConstants.TIME_ZONE, memberUserDetails.getTimeZone());
//                if (StrUtil.isNotBlank(memberUserDetails.getAuthenticationMethod())) {
//                    additionalInfo.put("authenticationMethod", memberUserDetails.getAuthenticationMethod());
//                }
//            }
//            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
//            return accessToken;
//        };
//    }
//
//
//    private DefaultTokenServices tokenServices(AuthorizationServerEndpointsConfigurer endpoints) {
//        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
//        tokenEnhancers.add(tokenEnhancer());
//        tokenEnhancers.add(jwtAccessTokenConverter());
//        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
//
//        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        tokenServices.setTokenStore(endpoints.getTokenStore());
//        tokenServices.setSupportRefreshToken(true);
//        tokenServices.setTokenEnhancer(tokenEnhancerChain);
//        //tokenServices.setAccessTokenValiditySeconds(30);
//
//        // 多用户体系下，刷新token再次认证客户端ID和 UserDetailService 的映射Map
//        Map<String, UserDetailsService> clientUserDetailsServiceMap = new HashMap<>();
//        clientUserDetailsServiceMap.put(SecurityConstants.ADMIN_CLIENT_ID, sysUserDetailsService); // 系统管理客户端
//        clientUserDetailsServiceMap.put(SecurityConstants.APP_CLIENT_ID, memberUserDetailsService); // Android、IOS、H5 移动客户端
//        clientUserDetailsServiceMap.put(SecurityConstants.WEAPP_CLIENT_ID, memberUserDetailsService); // 微信小程序客户端
//
//        // 刷新token模式下，重写预认证提供者替换其AuthenticationManager，可自定义根据客户端ID和认证方式区分用户体系获取认证用户信息
//        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
//        provider.setPreAuthenticatedUserDetailsService(new PreAuthenticatedUserDetailsService<>(clientUserDetailsServiceMap));
//        tokenServices.setAuthenticationManager(new ProviderManager(Arrays.asList(provider)));
//        return tokenServices;
//    }
}