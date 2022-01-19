/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: AuthCodeEndpoint
 * Author:   Derek Xu
 * Date:     2021/11/23 16:08
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.auth.boot.endpoint;

import cn.com.xuct.calendar.auth.boot.utils.SecurityUtils;
import cn.com.xuct.calendar.common.core.constant.AuthConstants;
import cn.com.xuct.calendar.common.core.res.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/23
 * @since 1.0.0
 */
@Slf4j
@Controller
@Api(tags = "【通用】认证中心服务")
@RequiredArgsConstructor
@SessionAttributes({"authorizationRequest"})
public class AuthCodeEndpoint {


    private final ClientDetailsService clientDetailsService;

    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * 当用户没登录的时候，会经过这个请求，在这个请求中可以处理一些逻辑
     *
     * @param request  request
     * @param response response
     * @return ResultModel
     * @throws IOException IOException
     */
    @RequestMapping(AuthConstants.LOGIN_PAGE)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ApiOperation(value = "统一认证中心跳转")
    public R<String> login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (null != savedRequest) {
            String targetUrl = savedRequest.getRedirectUrl();
            log.info("引发跳转的请求是:" + targetUrl);
            redirectStrategy.sendRedirect(request, response, AuthConstants.REDIRECT_URL);
        }
        //如果访问的是接口资源
        return R.fail(401, "访问的服务需要身份认证，请引导用户到登录页");
    }


    @SuppressWarnings("all")
    @RequestMapping(AuthConstants.REDIRECT_URL)
    public ModelAndView login(ModelAndView modelAndView, @RequestParam(required = false) String error) {
        modelAndView.setViewName("ftl/login");
        modelAndView.addObject("error", error);
        return modelAndView;
    }

    /**
     * 确认授权页面
     *
     * @param request
     * @param session
     * @param modelAndView
     * @return
     */
    @SuppressWarnings("all")
    @RequestMapping("/oauth/confirm_access")
    public ModelAndView confirm(HttpServletRequest request, HttpSession session, ModelAndView modelAndView) {
        Map<String, Object> scopeList = (Map<String, Object>) request.getAttribute("scopes");
        modelAndView.addObject("scopeList", scopeList.keySet());

        Object auth = session.getAttribute("authorizationRequest");
        if (auth != null) {
            AuthorizationRequest authorizationRequest = (AuthorizationRequest) auth;
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(authorizationRequest.getClientId());
            modelAndView.addObject("app", clientDetails.getAdditionalInformation());
            modelAndView.addObject("user", SecurityUtils.getUser());
        }
        modelAndView.setViewName("ftl/confirm");
        return modelAndView;
    }
}