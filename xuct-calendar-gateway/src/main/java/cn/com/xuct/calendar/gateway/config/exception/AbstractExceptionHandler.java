/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: AbstractExceptionHandler
 * Author:   Derek Xu
 * Date:     2021/11/30 11:55
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.gateway.config.exception;

import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/30
 * @since 1.0.0
 */
public abstract class AbstractExceptionHandler {

    private static final Integer DEFAULT_ERROR_CODE = 9999;

    protected String formatMessage(Throwable ex) {
        String errorMessage = null;
        if (ex instanceof NotFoundException) {
            String reason = ((NotFoundException) ex).getReason();
            errorMessage = reason;
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            errorMessage = responseStatusException.getMessage();
        } else {
            errorMessage = ex.getMessage();
        }
        return errorMessage;
    }

    protected Map<String, Object> buildErrorMap(String errorMessage) {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("success", false);
        resMap.put("code", DEFAULT_ERROR_CODE);
        resMap.put("msg", errorMessage);
        resMap.put("data", null);
        return resMap;
    }
}