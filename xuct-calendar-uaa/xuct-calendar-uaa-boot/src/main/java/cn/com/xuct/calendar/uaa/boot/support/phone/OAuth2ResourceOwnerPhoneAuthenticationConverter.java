package cn.com.xuct.calendar.uaa.boot.support.phone;

import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.uaa.boot.support.base.OAuth2ResourceOwnerBaseAuthenticationConverter;
import cn.com.xuct.calendar.uaa.boot.utils.OAuth2EndpointUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * @author lengleng
 * @date 2022-05-31
 * <p>
 * 短信登录转换器
 */
public class OAuth2ResourceOwnerPhoneAuthenticationConverter
        extends OAuth2ResourceOwnerBaseAuthenticationConverter<OAuth2ResourceOwnerPhoneAuthenticationToken> {

    /**
     * 是否支持此convert
     *
     * @param grantType 授权类型
     * @return
     */
    @Override
    public boolean support(String grantType) {
        return SecurityConstants.PHONE_GRANT_TYPE.equals(grantType);
    }

    @Override
    public OAuth2ResourceOwnerPhoneAuthenticationToken buildToken(Authentication clientPrincipal, Set requestedScopes, Map additionalParameters) {
        return new OAuth2ResourceOwnerPhoneAuthenticationToken(new AuthorizationGrantType(SecurityConstants.PHONE_GRANT_TYPE), clientPrincipal, requestedScopes, additionalParameters);
    }

    /**
     * 校验扩展参数
     * @param request 参数列表
     */
    @Override
    public void checkParams(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
        String phone = parameters.getFirst(SecurityConstants.PHONE_PARAM);
        if (!StringUtils.hasText(phone) || parameters.get(SecurityConstants.PHONE_PARAM).size() != 1) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, SecurityConstants.PHONE_PARAM, OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
    }
}
