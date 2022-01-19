/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: TokenInvalidException
 * Author:   Derek Xu
 * Date:     2021/11/23 9:26
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.auth.boot.core.exception;

import cn.com.xuct.calendar.common.core.enums.OAuth2ErrorEnum;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

/**
 * 〈一句话功能简述〉<br> 
 * 〈令牌不合法〉
 *
 * @author Derek Xu
 * @create 2021/11/23
 * @since 1.0.0
 */
@JsonSerialize(using = AuthExceptionSerializer.class)
public class TokenInvalidException extends Auth2Exception{

    public TokenInvalidException(String msg, Throwable t) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return OAuth2ErrorEnum.invalid_token.getErrorCode();
    }

    @Override
    public int getHttpErrorCode() {
        return HttpStatus.FAILED_DEPENDENCY.value();
    }
}