/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: AuthCustomizeException
 * Author:   Derek Xu
 * Date:     2021/12/1 15:34
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot.extension;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/1
 * @since 1.0.0
 */
public class AuthCustomizeException extends AuthenticationException {

    @Getter
    private String errorCode;


    public AuthCustomizeException(String msg, String  errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public AuthCustomizeException(String msg, Throwable cause, String errorCode) {
        super(msg, cause);
        this.errorCode = errorCode;
    }
}