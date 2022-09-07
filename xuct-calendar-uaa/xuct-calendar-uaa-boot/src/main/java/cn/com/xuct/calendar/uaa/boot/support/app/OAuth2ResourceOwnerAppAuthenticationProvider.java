package cn.com.xuct.calendar.uaa.boot.support.app;

import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.uaa.boot.support.base.OAuth2ResourceOwnerBaseAuthenticationProvider;
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
public class OAuth2ResourceOwnerAppAuthenticationProvider
        extends OAuth2ResourceOwnerBaseAuthenticationProvider<OAuth2ResourceOwnerAppAuthenticationToken> {

    private static final Logger LOGGER = LogManager.getLogger(OAuth2ResourceOwnerAppAuthenticationProvider.class);

    /**
     * Constructs an {@code OAuth2AuthorizationCodeAuthenticationProvider} using the
     * provided parameters.
     *
     * @param authenticationManager
     * @param authorizationService  the authorization service
     * @param tokenGenerator        the token generator
     * @since 0.2.3
     */
    public OAuth2ResourceOwnerAppAuthenticationProvider(AuthenticationManager authenticationManager,
                                                        OAuth2AuthorizationService authorizationService,
                                                        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        super(authenticationManager, authorizationService, tokenGenerator);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean supports = OAuth2ResourceOwnerAppAuthenticationToken.class.isAssignableFrom(authentication);
        LOGGER.debug("supports authentication=" + authentication + " returning " + supports);
        return supports;
    }

    @Override
    public void checkClient(RegisteredClient registeredClient) {
        assert registeredClient != null;
        if (!registeredClient.getAuthorizationGrantTypes().contains(new AuthorizationGrantType(SecurityConstants.APP_GRANT_TYPE))) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }
    }

    @Override
    public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
        String username = (String) reqParameters.get(SecurityConstants.USER_NAME_PARAM);
        String password = (String) reqParameters.get(SecurityConstants.PASSWORD_PARAM);
        return new UsernamePasswordAuthenticationToken(username, password);
    }

}
