/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: PhoneTokenGranter
 * Author:   Derek Xu
 * Date:     2021/11/15 11:11
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.auth.boot.extension;

import cn.com.xuct.calendar.common.core.enums.OAuth2ErrorEnum;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
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
public class PhoneTokenGranter extends AbstractTokenGranter {

    /**
     * 声明授权者 PhoneTokenGranter 支持授权模式 phone
     * 根据接口传值 grant_type = phone 的值匹配到此授权者
     * 匹配逻辑详见下面的两个方法
     *
     * @see CompositeTokenGranter#grant(String, TokenRequest)
     * @see AbstractTokenGranter#grant(String, TokenRequest)
     */
    private static final String GRANT_TYPE = "phone";
    private final AuthenticationManager authenticationManager;

    public PhoneTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
                             OAuth2RequestFactory requestFactory, AuthenticationManager authenticationManager) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
    }

    @Override
    @SuppressWarnings("all")
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

        Map<String, String> parameters = new LinkedHashMap(tokenRequest.getRequestParameters());

        String mobile = parameters.get("mobile"); // 手机号
        String code = parameters.get("code"); // 短信验证码
        parameters.remove("code");
        parameters.remove("mobile");

        Authentication userAuth = new PhoneAuthenticationToken(mobile, code);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = this.authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException ex) {
            throw new AuthCustomizeException(ex.getMessage(), OAuth2ErrorEnum.user_state_exception.getErrorCode());
        } catch (BadCredentialsException ex) {
            throw new AuthCustomizeException(ex.getMessage(), OAuth2ErrorEnum.invalid_sms_code.getErrorCode());
        }
        if (userAuth != null && userAuth.isAuthenticated()) {
            OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(storedOAuth2Request, userAuth);
        } else {
            throw new InvalidGrantException("Could not authenticate user: " + mobile);
        }
    }
}