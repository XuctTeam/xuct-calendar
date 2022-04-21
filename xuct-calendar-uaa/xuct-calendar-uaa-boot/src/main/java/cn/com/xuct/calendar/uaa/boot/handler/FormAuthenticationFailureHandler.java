/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: FormAuthenticationFailureHandler
 * Author:   Derek Xu
 * Date:     2021/11/23 15:12
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot.handler;

import cn.com.xuct.calendar.common.core.constant.AuthConstants;
import cn.com.xuct.calendar.common.web.utils.WebUtils;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/23
 * @since 1.0.0
 */

/**
 * @author lengleng
 * @date 2019-08-20
 * <p>
 * 表单登录失败处理逻辑
 */
@Slf4j
public class FormAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * Called when an authentication attempt fails.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     */
    @Override
    @SneakyThrows
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) {
        log.debug("表单登录失败:{}", exception.getLocalizedMessage());
        String url = HttpUtil.encodeParams(String.format(AuthConstants.REDIRECT_URL.concat("?error=%s"), exception.getMessage()), CharsetUtil.CHARSET_UTF_8);
        WebUtils.getResponse().sendRedirect(url);
    }
}