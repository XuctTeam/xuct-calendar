/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: RequestUtils
 * Author:   Derek Xu
 * Date:     2021/11/15 10:02
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.web.utils;

import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.common.core.enums.AuthenticationMethodEnum;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/15
 * @since 1.0.0
 */
@Slf4j
public class RequestUtils {

    public static final String EMPTY = "";

    @SneakyThrows
    public static String getGrantType() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String grantType = request.getParameter(SecurityConstants.GRANT_TYPE_KEY);
        return grantType;
    }



    /**
     * 解析JWT获取获取认证方式
     *
     * @return
     */
    @SneakyThrows
    public static String getAuthenticationMethod() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String refreshToken = request.getParameter(SecurityConstants.REFRESH_TOKEN_KEY);

        String payload = StrUtil.toString(JWSObject.parse(refreshToken).getPayload());
        JSONObject jsonObject = JSONUtil.parseObj(payload);

        String authenticationMethod = jsonObject.getStr(SecurityConstants.AUTHENTICATION_METHOD);
        if (StrUtil.isBlank(authenticationMethod)) {
            authenticationMethod = AuthenticationMethodEnum.USERNAME.getValue();
        }
        return authenticationMethod;
    }
}