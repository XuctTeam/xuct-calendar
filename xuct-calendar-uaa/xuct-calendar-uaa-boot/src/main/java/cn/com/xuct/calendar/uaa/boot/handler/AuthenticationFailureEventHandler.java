/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: AuthenticationFailureEventHandler
 * Author:   Derek Xu
 * Date:     2022/9/2 9:36
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot.handler;

import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.common.core.res.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import java.io.IOException;
import java.util.Map;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/9/2
 * @since 1.0.0
 */
public class AuthenticationFailureEventHandler implements AuthenticationFailureHandler {

    private final MappingJackson2HttpMessageConverter errorHttpResponseConverter = new MappingJackson2HttpMessageConverter();


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        // 写出错误信息
        Map<String, String[]> params = request.getParameterMap();
        if (params.containsKey(SecurityConstants.GRANT_TYPE)) {
            String grantTypes = request.getParameter(SecurityConstants.GRANT_TYPE);
//            if (SecurityConstants..equals(grantTypes)) {
//                //TODO:: 微信记录code
//            } else {
//                String username = request.getParameter(OAuth2ParameterNames.USERNAME);
//            }
        }
        sendErrorResponse(response, exception);
    }

    private void sendErrorResponse(HttpServletResponse response, AuthenticationException exception) throws IOException {
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        this.errorHttpResponseConverter.write(R.fail(exception.getLocalizedMessage()), MediaType.APPLICATION_JSON, httpResponse);
    }
}