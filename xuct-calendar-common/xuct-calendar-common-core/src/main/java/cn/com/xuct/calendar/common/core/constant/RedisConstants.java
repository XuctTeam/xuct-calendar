/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: RedisConstant
 * Author:   Derek Xu
 * Date:     2021/11/10 19:31
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.core.constant;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/10
 * @since 1.0.0
 */
public interface RedisConstants {

    /**
     * 用户session key信息
     */
    public static final String WX_USER_SESSION_KEY = "member::session:key:";

    /**
     * openID缓存用户信息
     */
    public static final String MEMBER_OPEN_INFO_KEY = "member::open:info:";


    /* 短信验证码 */
    public static final String MEMBER_PHONE_LOGIN_CODE_KEY = "member::login::sms:code:";

    /* 注册图形验证码 */
    public static final String MEMBER_CAPTCHA_REGISTER_CODE_KEY = "member::register::captcha:code:";

    /* 注册短信验证码 */
    public static final String MEMBER_PHONE_REGISTER_CODE_KEY = "member::register::phone:code:";

    /* 注册邮箱验证码 */
    public static final String MEMBER_EMAIL_REGISTER_CODE_KEY = "member::register::email:code:";

    /* 手机号绑定 */
    public static final String MEMBER_BIND_PHONE_CODE_KEY = "member::bind::phone::sms:code:";

    /* 手机号解绑 */
    public static final String MEMBER_UNBIND_PHONE_CODE_KEY = "member::unbind::phone::sms:code:";

    /* 邮箱绑定 */
    public static final String MEMBER_BIND_EMAIL_CODE_KEY = "member::bind::email::sms:code:";

    /* 邮箱解绑 */
    public static final String MEMBER_UNBIND_EMAIL_CODE_KEY = "member::unbind::email::sms:code:";

    /* 手机号忘记密码 */
    public static final String MEMBER_FORGET_PASSWORD_PHONE_CODE_KEY = "member::forget::password::phone::code:";

    /* 邮箱忘记密码 */
    public static final String MEMBER_FORGET_PASSWORD_EMAIL_CODE_KEY = "member::forget::password::email::code:";

    /* 短链接与长链接对应关系 */
    public static final String BASIC_SHORT_URL_KEY = "bacis::short::url:";

}