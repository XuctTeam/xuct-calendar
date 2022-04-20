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
package cn.com.xuct.calendar.auth.boot.utils;

import cn.com.xuct.calendar.auth.boot.core.userdetails.membner.MemberUserDetails;
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
    public MemberUserDetails getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof MemberUserDetails)
            return (MemberUserDetails) principal;
        return null;
    }

    /**
     * 获取用户
     */
    public MemberUserDetails getUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return getUser(authentication);
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