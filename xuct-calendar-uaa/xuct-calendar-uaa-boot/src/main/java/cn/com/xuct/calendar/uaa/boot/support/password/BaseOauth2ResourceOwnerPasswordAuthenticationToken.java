package cn.com.xuct.calendar.uaa.boot.support.password;

import cn.com.xuct.calendar.uaa.boot.support.base.BaseOauth2ResourceOwnerBaseAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * @author jumuning
 * @description 密码授权token信息
 */
public class BaseOauth2ResourceOwnerPasswordAuthenticationToken extends BaseOauth2ResourceOwnerBaseAuthenticationToken {

	public BaseOauth2ResourceOwnerPasswordAuthenticationToken(AuthorizationGrantType authorizationGrantType,
															  Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
		super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
	}

}
