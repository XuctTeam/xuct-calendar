/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: Oatuh2ErrorEnum
 * Author:   Administrator
 * Date:     2021/11/30 21:50
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.core.enums;

import lombok.Getter;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/30
 * @since 1.0.0
 */
public enum OAuth2ErrorEnum {

    invalid_client(1001, "invalid_client"),

    invalid_wechat_code(1002, "invalid_wechat_code"),

    invalid_exception(1003, "invalid_exception"),

    invalid_token(1004, "invalid_token"),

    invalid_request(1005, "invalid_request"),

    invalid_scope(1006, "invalid_scope"),

    invalid_sms_code(1007 , "invalid_sms_code"),

    access_denied(1101, "access_denied"),

    method_not_allowed(1102, "method_not_allowed"),

    user_state_exception(1103, "user_state_exception"),

    unauthorized(1104, "unauthorized"),

    server_error(1200, "server_error");


    @Getter
    private Integer code;

    @Getter
    private String errorCode;

    OAuth2ErrorEnum(Integer code, String errorCode) {
        this.code = code;
        this.errorCode = errorCode;
    }


    public static Integer getCode(String errorCode) {
        OAuth2ErrorEnum[] carTypeEnums = values();
        for (OAuth2ErrorEnum carTypeEnum : carTypeEnums) {
            if (carTypeEnum.getErrorCode().equals(errorCode)) {
                return carTypeEnum.getCode();
            }
        }
        return 500;
    }
}