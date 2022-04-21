/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: AuthWebResponseExceptionTranslator
 * Author:   Derek Xu
 * Date:     2021/11/23 8:59
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot.handler;

import cn.com.xuct.calendar.uaa.boot.core.exception.*;
import cn.com.xuct.calendar.uaa.boot.extension.AuthCustomizeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.web.HttpRequestMethodNotSupportedException;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/23
 * @since 1.0.0
 */
public class AuthWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {


    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {

        // Try to extract a SpringSecurityException from the stacktrace
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);

        Exception ase = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class, causeChain);
        if (ase != null) {
            if (ase instanceof AuthCustomizeException) {
                return handleOAuth2Exception(new ParamException(e.getMessage(), ((AuthCustomizeException) ase).getErrorCode()));
            }
            return handleOAuth2Exception(new UnauthorizedException(e.getMessage(), e));
        }
        ase = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class, causeChain);
        if (ase != null) {
            return handleOAuth2Exception(new ForbiddenException(ase.getMessage(), ase));
        }

        ase = (InvalidGrantException) throwableAnalyzer.getFirstThrowableOfType(InvalidGrantException.class, causeChain);
        if (ase != null) {
            return handleOAuth2Exception(new InvalidException(ase.getMessage(), ase));
        }

        // token 过期 特殊处理 返回 424 不是 401
        ase = (InvalidTokenException) throwableAnalyzer.getFirstThrowableOfType(InvalidTokenException.class, causeChain);
        if (ase != null) {
            return handleOAuth2Exception(new TokenInvalidException(ase.getMessage(), ase));
        }

        //客户端grant 异常
        ase = (InvalidClientException) throwableAnalyzer.getFirstThrowableOfType(InvalidClientException.class, causeChain);
        if (ase != null) {
            return handleOAuth2Exception(new ClientInvalidException(ase.getMessage(), ase));
        }

        ase = (HttpRequestMethodNotSupportedException) throwableAnalyzer.getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class, causeChain);
        if (ase != null) {
            return handleOAuth2Exception(new MethodNotAllowed(ase.getMessage(), ase));
        }

        ase = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);

        if (ase != null) {
            return handleOAuth2Exception((OAuth2Exception) ase);
        }

        return handleOAuth2Exception(new ServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e));


    }

    private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e) {

        int status = e.getHttpErrorCode();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CACHE_CONTROL, "no-store");
        headers.set(HttpHeaders.PRAGMA, "no-cache");
        if (status == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
            headers.set(HttpHeaders.WWW_AUTHENTICATE, String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
        }
        // 客户端异常直接返回客户端,不然无法解析
        if (e instanceof ClientAuthenticationException) {
            return new ResponseEntity<>(e, headers, HttpStatus.valueOf(status));
        }
        return new ResponseEntity<>(new Auth2Exception(e.getOAuth2ErrorCode(), e.getMessage()), headers, HttpStatus.valueOf(status));
    }
}