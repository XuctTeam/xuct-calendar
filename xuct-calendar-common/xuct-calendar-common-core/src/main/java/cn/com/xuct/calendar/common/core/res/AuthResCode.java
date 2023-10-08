/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: AuthResCode
 * Author:   Derek Xu
 * Date:     2021/11/17 13:12
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.core.res;


import jakarta.servlet.http.HttpServletResponse;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/17
 * @since 1.0.0
 */
public enum AuthResCode implements IResultCode {

    /**
     * 用户不存在
     */
    USER_NOT_EXIST(HttpServletResponse.SC_BAD_REQUEST, "用户不存在"),

    USERNAME_OR_PASSWORD_ERROR(HttpServletResponse.SC_BAD_REQUEST, "用户名或密码错误"),

    CLIENT_AUTHENTICATION_FAILED(1001, "client_id 或client_secret不正确"),

    CLIENT_SCOPE_FAILED(1006, "客户端scope配置错误"),

    UNAUTHORIZED_ERROR(1101, "权限认证错误"),

    INVALID_GRANT(1003, "无效的grant"),

    CLIENT_GRANT_TYPE_ERROR(1003, "客户端grant配置错误"),

    TOKEN_INVALID_OR_EXPIRED(HttpServletResponse.SC_UNAUTHORIZED, "token无效或已过期"),

    ACCESS_UNAUTHORIZED(1101, "用户认证错误"),

    TOKEN_ACCESS_FORBIDDEN(1101, "token已被禁止访问"),

    FORBIDDEN_OPERATION(HttpServletResponse.SC_NOT_IMPLEMENTED, "演示环境禁止修改、删除重要数据，请本地部署后测试");


    AuthResCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getCode() {
        return code;
    }
}