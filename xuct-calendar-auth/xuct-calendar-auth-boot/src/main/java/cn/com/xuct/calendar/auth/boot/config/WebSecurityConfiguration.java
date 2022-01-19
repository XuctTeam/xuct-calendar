package cn.com.xuct.calendar.auth.boot.config;

import cn.com.xuct.calendar.auth.boot.core.userdetails.membner.MemberUserDetailsService;
import cn.com.xuct.calendar.auth.boot.core.userdetails.user.SysUserDetailsService;
import cn.com.xuct.calendar.auth.boot.extension.PhoneAuthenticationProvider;
import cn.com.xuct.calendar.auth.boot.extension.WechatAuthenticationProvider;
import cn.com.xuct.calendar.auth.boot.handler.FormAuthenticationFailureHandler;
import cn.com.xuct.calendar.common.core.constant.AuthConstants;
import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.common.web.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletResponse;

/**
 * 安全配置类
 *
 * @author ys
 * @date 2020/4/22 13:52
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final MemberUserDetailsService memberUserDetailsService;

    private final StringRedisTemplate stringRedisTemplate;

    private final PasswordEncoder passwordEncoder;

    private final String[] ignoreUrls = {"/webjars/**", "/doc.html", "/swagger-resources/**", "/v2/api-docs"};

    /**
     * 手机验证码认证授权提供者
     *
     * @return
     */
    @Bean
    public PhoneAuthenticationProvider phoneAuthenticationProvider() {
        PhoneAuthenticationProvider provider = new PhoneAuthenticationProvider();
        provider.setUserDetailsService(memberUserDetailsService);
        provider.setStringRedisTemplate(stringRedisTemplate);
        return provider;
    }

    /**
     * 微信认证授权提供者
     *
     * @return
     */
    @Bean
    public WechatAuthenticationProvider wechatAuthenticationProvider() {
        WechatAuthenticationProvider provider = new WechatAuthenticationProvider();
        provider.setUserDetailsService(memberUserDetailsService);
        return provider;
    }


    /**
     * 用户名密码认证授权提供者
     *
     * @return
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(memberUserDetailsService);
        provider.setHideUserNotFoundExceptions(false);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
        auth.authenticationProvider(wechatAuthenticationProvider());
        auth.authenticationProvider(phoneAuthenticationProvider());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf()
                .disable()
                .formLogin().loginPage("/authentication/login")
                .loginProcessingUrl("/authentication/form").failureHandler(authenticationFailureHandler())
                .passwordParameter(SecurityConstants.USER_PASSWORD_KEY)
                .usernameParameter(SecurityConstants.USER_NAME_KEY)
                .and()
                //开启授权认证
                .authorizeRequests()
                .antMatchers("/oauth/**", "/sms/**", AuthConstants.LOGIN_PAGE, AuthConstants.REDIRECT_URL).permitAll()
                .antMatchers(ignoreUrls).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                //登陆配置
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
//
//                .httpBasic()
//                //密码错误返回在这里配置，用户名在自定义userDetail里配置
//                .authenticationEntryPoint((request, response, authException) -> {
//                    this.sendResponse(response, authException);
//                })
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint((request, response, authException) -> {
//                    this.sendResponse(response, authException);
//                })
//                .accessDeniedHandler((request, response, accessDeniedException) -> {
//                    this.sendResponse(response, accessDeniedException);
//                })
    }

    @Bean // 密码模式需要此bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**");
    }

    @Bean
    AuthenticationFailureHandler authenticationFailureHandler() {
        return new FormAuthenticationFailureHandler();
    }


    @SuppressWarnings("all")
    private void sendResponse(HttpServletResponse response, Exception ee) {
        log.info("basic commence AuthenticationException：{}", ee.getMessage());
        ResponseUtils.write(response, ee);
    }
}
