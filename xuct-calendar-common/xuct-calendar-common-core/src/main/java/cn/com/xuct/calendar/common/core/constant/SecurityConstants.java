/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: SecurityConstant
 * Author:   Derek Xu
 * Date:     2021/11/15 9:02
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
 * @create 2021/11/15
 * @since 1.0.0
 */
public interface SecurityConstants {

    String CLIENT_ID = "client_id";

    /* 电话扩展验证方式  */
    String PHONE_GRANT_TYPE = "phone";

    /* 微信扩展验证方式 */
    String WX_GRANT_TYPE  = "wx";

    /* client为app时多校验方式 */
    String CLIENT_ID_APP = "app";

    /* token刷新验证方式 */
    String REFRESH_TOKEN_GRANT_TYPE = "refresh_token";

    /**
     * phone参数
     */
    String PHONE_PARAM = "phone";

    String GRANT_TYPE = "grant_type";

    String USER_NAME_PARAM = "username";

    String PASSWORD_PARAM = "password";

    String CODE_PARAM = "code";

    String IV_PARAM = "iv";


    String ENCRYPTED_DATA_PARAM = "encryptedData";



    /**
     * 协议字段
     */
    String DETAILS_LICENSE = "license";

    /**
     * 项目的license
     */
    String PROJECT_LICENSE = "https://xuct.net.cn";

    /**
     * 客户端模式
     */
    String CLIENT_CREDENTIALS = "client_credentials";


    /**
     * 用户信息
     */
    String DETAILS_USER = "user_info";

    /**
     * 用户id
     */
    String DETAILS_USER_NAME = "user_name";

    /**
     * 用户时区
     */
    String DETAILS_USER_TIMEZONE = "user_timezone";


    /**
     * 授权码模式confirm
     */
    String CUSTOM_CONSENT_PAGE_URI = "/token/confirm_access";


    /**
     * 默认登录URL
     */
    String OAUTH_TOKEN_URL = "/oauth2/token";

    /**
     * 刷新token
     */
    String REFRESH_TOKEN = "refresh_token";


    /**
     * 标志
     */
    String FROM = "from";

    /**
     * 内部
     */
    String FROM_IN = "Y";

    String NOOP = "{noop}";

    /**
     * {bcrypt} 加密的特征码
     */
    String BCRYPT = "{bcrypt}";


    String TIME_ZONE = "timeZone";

    /**
     * 角色前缀
     */
    String ROLE = "ROLE_";

    /**
     * 验证码有效期,默认 60秒
     */
    long CODE_TIME = 60;
}