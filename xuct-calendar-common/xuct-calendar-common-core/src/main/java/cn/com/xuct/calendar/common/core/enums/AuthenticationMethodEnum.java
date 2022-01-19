/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: AuthenticationMethodEnum
 * Author:   Derek Xu
 * Date:     2021/11/15 10:00
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
 * @create 2021/11/15
 * @since 1.0.0
 */
public enum  AuthenticationMethodEnum {


    USERNAME("username", "用户名"),
    MOBILE("mobile", "手机号"),
    OPENID("openId", "开放式认证系统唯一身份标识");

    @Getter
    private String value;

    @Getter
    private String label;

    AuthenticationMethodEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static AuthenticationMethodEnum getByValue(String value) {
        AuthenticationMethodEnum authenticationMethodEnum = null;
        for (AuthenticationMethodEnum item : values()) {
            if (item.getValue().equals(value)) {
                authenticationMethodEnum = item;
                continue;
            }
        }
        return authenticationMethodEnum;
    }
}