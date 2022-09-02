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

    String GRANT_TYPE_KEY = "grant_type";

    String CLIENT_ID = "client_id";

    String PHONE_GRANT_TYPE = "phone";

    String SMS_PARAMETER_NAME = "mobile";


    String USER_ID_KEY = "userId";

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
     * 授权码模式confirm
     */
    String CUSTOM_CONSENT_PAGE_URI = "/token/confirm_access";

    /**
     * 标志
     */
    String FROM = "from";

    /**
     * 内部
     */
    String FROM_IN = "Y";

    String NOOP = "{noop}";


    String USER_NAME_KEY = "username";

    String USER_PASSWORD_KEY = "password";

    String TIME_ZONE = "timeZone";

    /**
     * JWT令牌前缀
     */
    String JWT_PREFIX = "Bearer ";


    /**
     * Basic认证前缀
     */
    String BASIC_PREFIX = "Basic ";

    /**
     * JWT载体key
     */
    String JWT_PAYLOAD_KEY = "payload";

    /**
     * JWT ID 唯一标识
     */
    String JWT_JTI = "jti";

    /**
     * JWT ID 唯一标识
     */
    String JWT_EXP = "exp";

    /**
     * JWT存储权限前缀
     */
    String AUTHORITY_PREFIX = "ROLE_";

    /**
     * 黑名单token前缀
     */
    String TOKEN_BLACKLIST_PREFIX = "auth:token:blacklist:";


    String REFRESH_TOKEN_KEY = "refresh_token";


    /**
     * 认证方式
     */
    String AUTHENTICATION_METHOD = "authenticationMethod";

    /**
     * 验证码key前缀
     */
    String VALIDATE_CODE_PREFIX = "VALIDATE_CODE:";

    /**
     * JWT存储权限属性
     */
    String JWT_AUTHORITIES_KEY = "authorities";


    String LOGOUT_PATH = "/uaa/oauth/logout";


    /**
     * sys_oauth_client_details 表的字段，不包括client_id、client_secret
     */
    String CLIENT_FIELDS = "client_id, CONCAT('{bcrypt}',client_secret) as client_secret, resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, autoapprove";


    /**
     * JdbcClientDetailsService 查询语句
     */
    String BASE_FIND_STATEMENT = "select " + CLIENT_FIELDS + " from oauth_client_details";


    /**
     * 默认的查询语句
     */
    String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by client_id";

    /**
     * 按条件client_id 查询
     */
    String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where client_id = ?";


    /**
     * 系统管理 web 客户端ID
     */
    String ADMIN_CLIENT_ID = "admin_web";

    /**
     * 移动端（H5/Android/IOS）客户端ID
     */
    String APP_CLIENT_ID = "app_id";

    /**
     * 小程序端（微信小程序、....） 客户端ID
     */
    String WEAPP_CLIENT_ID = "wechat_id";

    /**
     * app接口api
     */
    String APP_API_PATTERN = "/*/api/app/**";
}