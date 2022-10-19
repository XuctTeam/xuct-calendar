package cn.com.xuct.calendar.uaa.boot.support.app;

import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.uaa.boot.support.base.OAuth2ResourceOwnerBaseAuthenticationConverter;
import cn.com.xuct.calendar.uaa.boot.utils.OAuth2EndpointUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * @author lengleng
 * @date 2022-05-31
 * <p>
 * 短信登录转换器
 */
public class OAuth2ResourceOwnerAppAuthenticationConverter
        extends OAuth2ResourceOwnerBaseAuthenticationConverter<OAuth2ResourceOwnerAppAuthenticationToken> {

    /**
     * 是否支持此convert
     *
     * @param grantType 授权类型
     * @return
     */
    @Override
    public boolean support(String grantType) {
        return SecurityConstants.APP_GRANT_TYPE.equals(grantType);
    }

    @Override
    public OAuth2ResourceOwnerAppAuthenticationToken buildToken(Authentication clientPrincipal, Set requestedScopes, Map additionalParameters) {
        return new OAuth2ResourceOwnerAppAuthenticationToken(new AuthorizationGrantType(SecurityConstants.APP_GRANT_TYPE), clientPrincipal, requestedScopes, additionalParameters);
    }

    /**
     * 校验扩展参数
     * 1. loginType = phone 不需要校验密码
     * 2. loginType = password 需要校验密码
     *
     * @param request 参数列表
     */
    @Override
    public void checkParams(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
        String loginType = parameters.getFirst(SecurityConstants.APP_LOGIN_TYPE_PARAM);
        if (!StringUtils.hasText(loginType))
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, SecurityConstants.APP_LOGIN_TYPE_PARAM, OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        if (loginType.equals(SecurityConstants.PHONE_PARAM)) {
            String phone = parameters.getFirst(SecurityConstants.PHONE_PARAM);
            if (!StringUtils.hasText(phone) || parameters.get(SecurityConstants.PHONE_PARAM).size() != 1) {
                OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, SecurityConstants.PHONE_PARAM, OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
            }
            return;
        }
        String username = parameters.getFirst(SecurityConstants.USER_NAME_PARAM);
        if (!StringUtils.hasText(username) || parameters.get(OAuth2ParameterNames.USERNAME).size() != 1)
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.USERNAME, OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);

        String password = parameters.getFirst(SecurityConstants.PASSWORD_PARAM);
        if (loginType.equals(AuthorizationGrantType.PASSWORD.getValue()) && !StringUtils.hasText(password))
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, SecurityConstants.PASSWORD_PARAM, OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);

    }
}
