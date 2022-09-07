package cn.com.xuct.calendar.uaa.boot.support.wx;

import cn.com.xuct.calendar.uaa.boot.support.base.OAuth2ResourceOwnerBaseAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * @author lengleng
 * @description 短信登录token信息
 */
public class OAuth2ResourceOwnerWxAuthenticationToken extends OAuth2ResourceOwnerBaseAuthenticationToken {

	public OAuth2ResourceOwnerWxAuthenticationToken(AuthorizationGrantType authorizationGrantType,
													Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
		super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
	}

}
