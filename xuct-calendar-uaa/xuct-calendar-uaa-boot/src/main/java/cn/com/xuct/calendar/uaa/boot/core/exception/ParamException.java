/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: ParamException
 * Author:   Derek Xu
 * Date:     2021/12/1 15:38
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot.core.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/1
 * @since 1.0.0
 */
@JsonSerialize(using = AuthExceptionSerializer.class)
public class ParamException extends Auth2Exception{


    public ParamException(String msg, String errorCode) {
        super(msg, errorCode);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return this.getErrorCode();
    }

    @Override
    public int getHttpErrorCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }
}