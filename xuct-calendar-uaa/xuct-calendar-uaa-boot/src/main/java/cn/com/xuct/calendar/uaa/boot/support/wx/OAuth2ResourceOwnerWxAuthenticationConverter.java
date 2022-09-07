package cn.com.xuct.calendar.uaa.boot.support.wx;

import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.uaa.boot.support.base.OAuth2ResourceOwnerBaseAuthenticationConverter;
import cn.com.xuct.calendar.uaa.boot.utils.OAuth2EndpointUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
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
public class OAuth2ResourceOwnerWxAuthenticationConverter
        extends OAuth2ResourceOwnerBaseAuthenticationConverter<OAuth2ResourceOwnerWxAuthenticationToken> {

    /**
     * 是否支持此convert
     *
     * @param grantType 授权类型
     * @return
     */
    @Override
    public boolean support(String grantType) {
        return SecurityConstants.WX_GRANT_TYPE.equals(grantType);
    }

    @Override
    public OAuth2ResourceOwnerWxAuthenticationToken buildToken(Authentication clientPrincipal, Set requestedScopes, Map additionalParameters) {
        return new OAuth2ResourceOwnerWxAuthenticationToken(new AuthorizationGrantType(SecurityConstants.WX_GRANT_TYPE), clientPrincipal, requestedScopes, additionalParameters);
    }

    /**
     * 校验扩展参数 密码模式密码必须不为空
     *
     * @param request 参数列表
     */
    @Override
    public void checkParams(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
        String code = parameters.getFirst(SecurityConstants.CODE_PARAM);
        String iv = parameters.getFirst(SecurityConstants.IV_PARAM);
        String encryptedData = parameters.getFirst(SecurityConstants.ENCRYPTED_DATA_PARAM);
        if(!(StringUtils.hasLength(code) && StringUtils.hasLength(iv) && StringUtils.hasLength(encryptedData))){
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, SecurityConstants.USER_NAME_PARAM, OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
    }
}
