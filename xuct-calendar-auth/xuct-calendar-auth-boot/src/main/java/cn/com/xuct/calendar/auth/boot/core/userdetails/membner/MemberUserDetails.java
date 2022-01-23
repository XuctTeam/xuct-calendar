/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MemberUserDetails
 * Author:   Derek Xu
 * Date:     2021/11/15 9:48
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.auth.boot.core.userdetails.membner;

import cn.com.xuct.calendar.common.module.dto.MemberInfoDto;
import cn.com.xuct.calendar.common.core.constant.GlobalConstants;
import cn.com.xuct.calendar.common.core.enums.PasswordEncoderTypeEnum;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/15
 * @since 1.0.0
 */
@Data
public class MemberUserDetails implements UserDetails {

    private Long userId;
    private String username;
    private String password;
    private Boolean enabled;
    private String timeZone;

    /**
     * 认证方式
     */
    private String authenticationMethod;


    /**
     * 小程序会员用户体系
     *
     * @param member 小程序会员用户认证信息
     */
    public MemberUserDetails(MemberInfoDto member) {
        this.setUserId(member.getUserId());
        this.setUsername(member.getUsername());
        this.setPassword(PasswordEncoderTypeEnum.BCRYPT.getPrefix() + member.getPassword());
        this.setEnabled(GlobalConstants.STATUS_YES.equals(member.getStatus()));
        this.setTimeZone(member.getTimeZone());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new HashSet<>();
        return collection;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}