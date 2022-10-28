package cn.com.xuct.calendar.common.security.serivces;

import cn.com.xuct.calendar.common.core.constant.GlobalConstants;
import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.res.RetOps;
import cn.com.xuct.calendar.common.module.feign.PersonInfo;
import cn.com.xuct.calendar.common.module.feign.UserInfo;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lengleng
 * @date 2021/12/21
 */
public interface OAuthUserDetailsService extends UserDetailsService, Ordered {

    /**
     * 是否支持此客户端校验
     *
     * @param clientId 目标客户端
     * @return true/false
     */
    default boolean support(String clientId, String grantType) {
        return true;
    }

    /**
     * 排序值 默认取最大的
     *
     * @return 排序值
     */
    default int getOrder() {
        return 0;
    }

    /**
     * 构建userdetails
     *
     * @param result 用户信息
     * @return UserDetails
     */
    default UserDetails getUserDetails(R<UserInfo> result, final boolean appType) {
        UserInfo info = RetOps.of(result).getData().orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        Set<String> dbAuthsSet = new HashSet<>();
        if (ArrayUtil.isNotEmpty(info.getRoles())) {
            // 获取角色
            Arrays.stream(info.getRoles()).forEach(role -> dbAuthsSet.add(SecurityConstants.ROLE + role));
            // 获取资源
            dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));
        }
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));
        if (!appType) return null;
        PersonInfo personInfo = info.getPersonInfo();
        if (personInfo.getStatus() != 0) {
            throw new DisabledException("该账户已被禁用!");
        }
        return new OAuthUser(personInfo.getUserId(), personInfo.getUsername(), SecurityConstants.BCRYPT + personInfo.getPassword(),
                personInfo.getName(), personInfo.getTimeZone(),
                true, true, true, StrUtil.equals(personInfo.getStatus().toString(), GlobalConstants.STATUS_YES), authorities);
    }

    default UserDetails loadUserByUser(OAuthUser oAuthUser) {
        return this.loadUserByUsername(oAuthUser.getUsername());
    }
}
