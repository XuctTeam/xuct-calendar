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
package cn.com.xuct.calendar.ums.boot.handler;

import cn.com.xuct.calendar.common.core.exception.SvrException;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.res.SvrResCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/17
 * @since 1.0.0
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(value = SvrException.class)
    public R<String> svrExceptionHandler(SvrException e) {
        log.error("数据异常！原因是：{}", e.getMessage());
        return R.fail(e.getCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R<String> errorHandler(Exception e) {
        log.error("服务器异常！原因是：{}", e.getMessage());
        return R.fail(SvrResCode.UMS_SERVER_ERROR);
    }

}