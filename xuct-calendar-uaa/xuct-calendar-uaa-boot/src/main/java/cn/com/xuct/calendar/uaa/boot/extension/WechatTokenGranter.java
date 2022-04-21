/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: WechatTokenGranter
 * Author:   Derek Xu
 * Date:     2021/11/15 11:09
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot.extension;

import cn.com.xuct.calendar.common.core.enums.OAuth2ErrorEnum;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/15
 * @since 1.0.0
 */
public class WechatTokenGranter extends AbstractTokenGranter {

    /**
     * 声明授权者 CaptchaTokenGranter 支持授权模式 wechat
     * 根据接口传值 grant_type = wechat 的值匹配到此授权者
     * 匹配逻辑详见下面的两个方法
     *
     * @see CompositeTokenGranter#grant(String, TokenRequest)
     * @see AbstractTokenGranter#grant(String, TokenRequest)
     */
    private static final String GRANT_TYPE = "wechat";
    private final AuthenticationManager authenticationManager;

    public WechatTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, AuthenticationManager authenticationManager) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

        Map<String, String> parameters = new LinkedHashMap(tokenRequest.getRequestParameters());
        String code = parameters.get("code");
        String iv = parameters.get("iv");
        String encryptedData = parameters.get("encryptedData");
        // 过河拆桥，移除后续无用参数
        parameters.remove("code");
        parameters.remove("iv");
        parameters.remove("encryptedData");

        Authentication userAuth = new WechatAuthenticationToken(code, iv, encryptedData); // 未认证状态
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = this.authenticationManager.authenticate(userAuth); // 认证中
        } catch (BadCredentialsException e) {
            throw new InvalidGrantException(OAuth2ErrorEnum.invalid_wechat_code.getErrorCode(), e);
        }
        if (userAuth != null && userAuth.isAuthenticated()) { // 认证成功
            OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(storedOAuth2Request, userAuth);
        } else { // 认证失败
            throw new InvalidGrantException("Could not authenticate code: " + code);
        }
    }
}