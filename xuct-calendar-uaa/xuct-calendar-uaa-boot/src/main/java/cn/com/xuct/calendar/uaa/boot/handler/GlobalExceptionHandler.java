/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: GlobalExceptionHandler
 * Author:   Derek Xu
 * Date:     2021/11/17 17:07
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot.handler;

import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.res.ResultCode;
import cn.com.xuct.calendar.common.security.excpetion.OAuthClientException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/17
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice(basePackages = "cn.com.xuct.calendar.auth.boot.controller.web")
public class GlobalExceptionHandler {

    /**
     * 远程接口调用异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = FeignException.class)
    @ResponseBody
    public R<String> feignExceptionHandler(FeignException e) {
        log.error("远程接口异常！原因是：{}", e.getMessage());
        return R.fail(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public R<String> exceptionHandler(Exception e) {
        log.error("未知异常！原因是：{}", e.getMessage());
        return R.fail(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }

}