/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: ResponseUtils
 * Author:   Derek Xu
 * Date:     2021/11/22 22:45
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.web.utils;

import cn.com.xuct.calendar.common.core.constant.GlobalConstants;
import cn.com.xuct.calendar.common.core.utils.JsonUtils;
import cn.com.xuct.calendar.common.core.res.AuthResCode;
import cn.com.xuct.calendar.common.core.res.R;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/22
 * @since 1.0.0
 */
public class ResponseUtils {

    /**
     * 功能描述: <br>
     * 〈〉
     *
     * @param response
     * @param ee
     * @return:void
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2021/11/22 22:46
     */
    public static void write(HttpServletResponse response, Exception ee) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(cn.hutool.http.HttpStatus.HTTP_OK);
        response.setHeader(CONTENT_TYPE, GlobalConstants.CONTENT_TYPE);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        R result = R.fail(AuthResCode.UNAUTHORIZED_ERROR, ee.getMessage());
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(JsonUtils.obj2json(result));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}