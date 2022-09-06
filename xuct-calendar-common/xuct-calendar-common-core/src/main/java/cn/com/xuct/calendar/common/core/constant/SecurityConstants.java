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

    String APP_GRANT_TYPE = "app";


    String PHONE_PARAM = "phone";

    String WX_GRANT_TYPE = "wx";


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

    /**
     * {bcrypt} 加密的特征码
     */
    String BCRYPT = "{bcrypt}";


    String USER_NAME_KEY = "username";


    String TIME_ZONE = "timeZone";

    /**
     * 角色前缀
     */
    String ROLE = "ROLE_";
}