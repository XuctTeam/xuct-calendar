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

    /**
     * 手机号缓存用户信息
     */
    public static final String MEMBER_INFO_KEY = "member:info:";

    /* 短信验证码 */
    public static final String MEMBER_PHONE_LOGIN_CODE_KEY = "member::login::sms:code:";

    /* 注册验证码 */
    public static final String MEMBER_PHONE_REGISTER_CODE_KEY = "member::register::sms:code:";

    /* 手机号绑定 */
    public static final String MEMBER_BIND_PHONE_CODE_KEY = "member::bind::phone::sms:code:";

    /* 手机号解绑 */
    public static final String MEMBER_UNBIND_PHONE_CODE_KEY = "member::unbind::phone::sms:code:";

}