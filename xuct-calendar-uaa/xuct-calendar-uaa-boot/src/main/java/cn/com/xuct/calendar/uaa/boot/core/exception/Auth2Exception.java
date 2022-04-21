/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: Auth2Exception
 * Author:   Derek Xu
 * Date:     2021/11/23 9:15
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot.core.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/23
 * @since 1.0.0
 */
@JsonSerialize(using = AuthExceptionSerializer.class)
public class Auth2Exception extends OAuth2Exception {

    @Getter
    private String errorCode;

    public Auth2Exception(String msg) {
        super(msg);
    }


    public Auth2Exception(String msg, String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}