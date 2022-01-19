/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: SvrResCode
 * Author:   Derek Xu
 * Date:     2021/11/19 9:03
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.core.res;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/19
 * @since 1.0.0
 */
public enum SvrResCode implements IResultCode {


    PARAM_ERROR(2001, "参数错误"),

    UMS_SERVER_ERROR(5000, "用户中心异常"),

    UMS_WX_ERROR(5001, "ums:远程访问微信异常"),

    UMS_MEMBER_YET_EXIST(5002, "ums:用户已经存在"),

    UMS_REGISTER_SMS_CODE_ERROR(5003, "注册验证码无效"),

    UMS_BING_PHONE_ERROR(5004, "绑定手机验证码无效"),

    UMS_MEMBER_AUTH_TYPE_ERROR(5005, "获取认证方式无效"),


    CMS_SERVER_ERROR(6000, "日程中心异常"),

    CMS_CALENDAR_NOT_FOUND(6001, "日历不存在"),

    CMS_COMPONENT_REPEAT_UNTIL_EMPTY(6101 , "循环截止日期为空"),

    CMS_COMPONENT_NOT_FOUND(6102, "日程不存在"),

    CMS_COMPONENT_DAY_LIST_EMPTY(6103, "日程查询日期为空");


    private int code;

    private String message;

    SvrResCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}