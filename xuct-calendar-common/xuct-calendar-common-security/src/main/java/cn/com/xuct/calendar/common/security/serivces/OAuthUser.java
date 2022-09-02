/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: OAuthUser
 * Author:   Derek Xu
 * Date:     2022/9/2 15:29
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.security.serivces;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/9/2
 * @since 1.0.0
 */
public class OAuthUser extends User implements OAuth2AuthenticatedPrincipal {

    /**
     * 用户ID
     */
    @Getter
    private final Long id;

    public OAuthUser(Long id, String username, String password, String phone, boolean enabled,
                   boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                   Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }


    @Override
    public Map<String, Object> getAttributes() {
       return new HashMap<>();
    }

    @Override
    public String getName() {
        return this.getUsername();
    }
}