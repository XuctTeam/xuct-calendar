/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: AuthBasicAuthenticationFilter
 * Author:   Administrator
 * Date:     2021/11/21 20:23
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.auth.boot.handler;

import cn.com.xuct.calendar.auth.boot.core.exception.Auth2Exception;
import cn.com.xuct.calendar.common.core.res.AuthResCode;
import cn.com.xuct.calendar.common.web.utils.ResponseUtils;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.shaded.com.google.common.net.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


/**
 * 〈一句话功能简述〉<br>
 * 〈basic auth 方式client认证过滤器〉
 * <p>
 * * 置于{@link org.springframework.security.web.authentication.www.BasicAuthenticationFilter}之前，
 * <p>
 * * 以实现客户端信息不全、认证失败时返回自定义响应信息
 *
 * @author Derek Xu
 * @create 2021/11/21
 * @since 1.0.0
 */
@Slf4j
public class AuthBasicAuthenticationFilter extends OncePerRequestFilter {

    private final ClientDetailsService clientDetailsService;

    private PasswordEncoder passwordEncoder;

    public AuthBasicAuthenticationFilter(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().contains("/oauth/token")) {
            filterChain.doFilter(request, response);
            return;
        }
        String[] clientDetails = null;
        try {
            clientDetails = this.convert(request);
        } catch (Exception ee) {
            this.sendResponse(response, ee);
            return;
        }
        try {
            this.handle(request, response, clientDetails, filterChain);
        } catch (Exception ee) {
            this.sendResponse(response, ee);
        }
    }

    private void handle(HttpServletRequest request, HttpServletResponse response, String[] clientDetails, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }
        ClientDetails details = null;
        try {
            details = clientDetailsService.loadClientByClientId(clientDetails[0]);
        } catch (ClientRegistrationException e) {
            log.info("client认证失败，{},{}", e.getMessage(), clientDetails[0]);
            throw new Auth2Exception(AuthResCode.CLIENT_AUTHENTICATION_FAILED.getMessage());
        }
        if (details == null) {
            log.info("client认证失败，{}", clientDetails[0]);
            throw new Auth2Exception(AuthResCode.CLIENT_AUTHENTICATION_FAILED.getMessage());
        }
        if (passwordEncoder != null && !this.passwordEncoder.matches(clientDetails[1], details.getClientSecret())) {
            log.info("client secret is error : {}", clientDetails[0]);
            throw new Auth2Exception(AuthResCode.CLIENT_AUTHENTICATION_FAILED.getMessage());
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(details.getClientId(), details.getClientSecret(), details.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
        filterChain.doFilter(request, response);
    }


    private String[] convert(HttpServletRequest request) {
        //不开启form，因为其不走该filter，采用basic
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            log.info("Basic Authentication Authorization header not found");
            throw new Auth2Exception(AuthResCode.CLIENT_AUTHENTICATION_FAILED.getMessage());
        }
        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, BasicAuthenticationConverter.AUTHENTICATION_SCHEME_BASIC)) {
            log.info("Basic Authentication Authorization header not found");
            throw new Auth2Exception(AuthResCode.CLIENT_AUTHENTICATION_FAILED.getMessage());
        }
        if (header.equalsIgnoreCase(BasicAuthenticationConverter.AUTHENTICATION_SCHEME_BASIC)) {
            log.info("Empty basic authentication token");
            throw new Auth2Exception(AuthResCode.CLIENT_AUTHENTICATION_FAILED.getMessage());
        }
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded;
        decoded = Base64.getDecoder().decode(base64Token);
        String token = new String(decoded, StandardCharsets.UTF_8);
        int delimit = token.indexOf(":");
        if (delimit == -1) {
            throw new Auth2Exception(AuthResCode.CLIENT_AUTHENTICATION_FAILED.getMessage());
        }
        String clientId = token.substring(0, delimit);
        String clientSecret = token.substring(delimit + 1);
        if (StrUtil.isBlank(clientId) || StrUtil.isBlank(clientSecret)) {
            log.info("client_id or client_secret not found in form or basic");
            throw new Auth2Exception(AuthResCode.CLIENT_AUTHENTICATION_FAILED.getMessage());
        }
        return new String[]{clientId, clientSecret};
    }

    @SuppressWarnings("all")
    private void sendResponse(HttpServletResponse response, Exception ee) {
        log.info("AuthBasicAuthentication filter :: ee = {}", ee.getMessage());
        ResponseUtils.write(response, ee);
    }
}