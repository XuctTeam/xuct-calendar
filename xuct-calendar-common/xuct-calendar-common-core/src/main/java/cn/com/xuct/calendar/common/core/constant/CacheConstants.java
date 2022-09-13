/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: CacheConstants
 * Author:   Derek Xu
 * Date:     2021/11/15 8:57
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
public interface CacheConstants {


    /**
     * 用户信息缓存
     */
    String USER_DETAILS = "security::user_details";


    /**
     * oauth 客户端信息
     */
    String CLIENT_DETAILS_KEY = "security::client_details";


    /**
     * 验证码前缀
     */
    String DEFAULT_CODE_KEY = "security::login::code";
}