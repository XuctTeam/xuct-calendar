package cn.com.xuct.calendar.uaa.boot.support.phone;

import cn.com.xuct.calendar.uaa.boot.support.base.BaseOauth2ResourceOwnerBaseAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * @author lengleng
 * @description 短信登录token信息
 */
public class BaseOauth2ResourceOwnerPhoneAuthenticationToken extends BaseOauth2ResourceOwnerBaseAuthenticationToken {

	public BaseOauth2ResourceOwnerPhoneAuthenticationToken(AuthorizationGrantType authorizationGrantType,
														   Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
		super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
	}

}
