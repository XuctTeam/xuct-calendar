package cn.com.xuct.calendar.uaa.boot.support.phone;

import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.uaa.boot.support.base.BaseOauth2ResourceOwnerBaseAuthenticationProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Map;

/**
 * @author lengleng
 * @date date
 * <p>
 * 短信登录的核心处理
 */
public class BaseOauth2ResourceOwnerPhoneAuthenticationProvider
        extends BaseOauth2ResourceOwnerBaseAuthenticationProvider<BaseOauth2ResourceOwnerPhoneAuthenticationToken> {

    private static final Logger LOGGER = LogManager.getLogger(BaseOauth2ResourceOwnerPhoneAuthenticationProvider.class);

    /**
     * Constructs an {@code OAuth2AuthorizationCodeAuthenticationProvider} using the
     * provided parameters.
     *
     * @param authenticationManager
     * @param authorizationService  the authorization service
     * @param tokenGenerator        the token generator
     * @since 0.2.3
     */
    public BaseOauth2ResourceOwnerPhoneAuthenticationProvider(AuthenticationManager authenticationManager,
                                                              OAuth2AuthorizationService authorizationService,
                                                              OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        super(authenticationManager, authorizationService, tokenGenerator);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean supports = BaseOauth2ResourceOwnerPhoneAuthenticationToken.class.isAssignableFrom(authentication);
        LOGGER.debug("supports authentication=" + authentication + " returning " + supports);
        return supports;
    }

    @Override
    public void checkClient(RegisteredClient registeredClient) {
        assert registeredClient != null;
        if (!registeredClient.getAuthorizationGrantTypes().contains(new AuthorizationGrantType(SecurityConstants.PHONE_GRANT_TYPE))) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }
    }

    @Override
    public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
        String phone = (String) reqParameters.get(SecurityConstants.PHONE_PARAM);
        return new UsernamePasswordAuthenticationToken(phone , "");
    }
}
