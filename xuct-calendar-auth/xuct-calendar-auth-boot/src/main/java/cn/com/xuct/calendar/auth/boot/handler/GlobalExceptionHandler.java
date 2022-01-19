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
package cn.com.xuct.calendar.auth.boot.handler;

import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.res.ResultCode;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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


//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(InvalidScopeException.class)
//    public R<String> InvalidScopeExceptionException(InvalidScopeException e) {
//        log.error("客户端验证scope异常: {}", e.getMessage());
//        return R.fail(AuthResCode.CLIENT_SCOPE_FAILED, e.getMessage());
//    }
//
//
//    /**
//     * 用户不存在
//     *
//     * @param e
//     * @return
//     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(UsernameNotFoundException.class)
//    public R<String> handleUsernameNotFoundException(UsernameNotFoundException e) {
//        return R.fail(AuthResCode.USER_NOT_EXIST);
//    }
//
//    /**
//     * 用户名和密码异常
//     *
//     * @param e
//     * @return
//     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(InvalidGrantException.class)
//    public R<String> handleInvalidGrantException(InvalidGrantException e) {
//        return R.fail(AuthResCode.USERNAME_OR_PASSWORD_ERROR);
//    }
//
//
//    /**
//     * 账户异常(禁用、锁定、过期)
//     *
//     * @param e
//     * @return
//     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler({InternalAuthenticationServiceException.class})
//    public R<String> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
//        return R.fail(e.getMessage());
//    }


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