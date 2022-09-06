/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: SecurityUtils
 * Author:   Derek Xu
 * Date:     2021/11/24 13:44
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.security.utils;

import cn.com.xuct.calendar.common.security.serivces.OAuthUser;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/24
 * @since 1.0.0
 */
@UtilityClass
public class SecurityUtils {

    /**
     * 获取Authentication
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    /**
     * 获取用户
     */
    public OAuthUser getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuthUser) {
            return (OAuthUser) principal;
        }
        return null;
    }


    public static Long getUserId() {
        Authentication authentication = getAuthentication();
        return getUser(authentication).getId();
    }

    /**
     * 解析JWT获取时区
     *
     * @return
     */
    public static String getTimeZone() {
        Authentication authentication = getAuthentication();
        return "";
    }

    public static String getUserName(){
        Authentication authentication = getAuthentication();
        return getUser(authentication).getUsername();
    }


    /**
     * 获取用户角色信息
     *
     * @return 角色集合
     */
    public List<Integer> getRoles() {
        Authentication authentication = getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<Integer> roleIds = new ArrayList<>();
        authorities.stream().filter(granted -> StrUtil.startWith(granted.getAuthority(), "role_"))
                .forEach(granted -> {
                    String id = StrUtil.removePrefix(granted.getAuthority(), "role_");
                    roleIds.add(Integer.parseInt(id));
                });
        return roleIds;
    }
}