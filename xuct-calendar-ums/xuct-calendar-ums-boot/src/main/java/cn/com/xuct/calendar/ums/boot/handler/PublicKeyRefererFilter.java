/**
 * Copyright (C), 2015-2023, XXX有限公司
 * FileName: RefererFilter
 * Author:   Derek Xu
 * Date:     2023/3/3 13:17
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.handler;

import cn.com.xuct.calendar.common.core.constant.GlobalConstants;
import cn.com.xuct.calendar.common.core.utils.JsonUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2023/3/3
 * @since 1.0.0
 */
@Data
@Slf4j
@ConditionalOnProperty(prefix = "security.public.key.csrf", name = "enable", havingValue = "true")
@WebFilter(filterName = "publicKeyRefererFilter", urlPatterns = "/api/app/v1/anno/public/*")
public class PublicKeyRefererFilter implements Filter {


    /**
     * 过滤器配置对象
     */
    FilterConfig filterConfig = null;

    /**
     * 是否启用
     */
    @Value("${security.public.key.csrf.enable:false}")
    private boolean enable = true;

    /**
     * 忽略的URL
     */
    @Value("${security.public.key.csrf.excludes:}")
    private String excludes;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        if (!enable) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String host = request.getServerName();
        String referer = request.getHeader("referer");
        if (referer == null) {
            // 状态置为404
            this.writeError(response);
            return;
        }
        java.net.URL url = null;
        try {
            url = new java.net.URL(referer);
        } catch (MalformedURLException e) {
            // 状态置为404
            this.writeError(response);
            return;
        }
        // 首先判断请求域名和referer域名是否相同
        if (!host.equals(url.getHost()) && !isExcludeServerName(url.getHost())) {
            // 如果不等，判断是否在白名单中
            this.writeError(response);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    /**
     * 判断是否为忽略的URL
     * <p>
     * <p>
     * URL路径
     *
     * @return true-忽略，false-过滤
     */
    private boolean isExcludeServerName(String serverName) {
        if (StringUtils.isBlank(excludes)) {
            return false;
        }
        List<String> urls = Arrays.asList(excludes.trim().split(GlobalConstants.COMMA));
        for (String uri : urls) {
            // 正则验证
            Pattern p = Pattern.compile("^" + uri);
            if (p.matcher(serverName).find()) {
                return true;
            }
        }
        return false;
    }

    private void writeError(HttpServletResponse response) {
        PrintWriter writer = null;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            writer = response.getWriter();
            writer.println(JsonUtils.mapToJson(new HashMap<String, Object>(2) {{
                put("code", HttpStatus.UNAUTHORIZED.value());
                put("message", "非法的请求");
            }}));
            writer.flush();
        } catch (Exception ee) {
            log.error("RefererFilter:: get sms public key error ");
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}