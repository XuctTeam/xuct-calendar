/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MemberUserDetailsService
 * Author:   Derek Xu
 * Date:     2021/11/15 9:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.auth.boot.core.userdetails.membner;

import cn.com.xuct.calendar.auth.api.client.MemberFeignClient;
import cn.com.xuct.calendar.common.core.constant.AuthConstants;
import cn.com.xuct.calendar.common.core.enums.AuthenticationMethodEnum;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.MemberFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.WxUserInfoFeignInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/15
 * @since 1.0.0
 */
@Service("memberUserDetailsService")
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {

    private final MemberFeignClient memberFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberUserDetails userDetails = null;
        R<MemberFeignInfo> result = memberFeignClient.loadMemberByUserName(username);
        if (!R.isSuccess(result)) {
            throw new UsernameNotFoundException(AuthConstants.USER_NOT_EXIST);
        }
        MemberFeignInfo member = result.getData();
        if (!StringUtils.hasLength(member.getPassword())) {
            throw new BadCredentialsException(AuthConstants.BAD_CREDENTIALS);
        }
        if (member == null) {
            throw new UsernameNotFoundException(AuthConstants.USER_NOT_EXIST);
        }
        userDetails = new MemberUserDetails(member);
        userDetails.setAuthenticationMethod(AuthenticationMethodEnum.USERNAME.getValue());   // 认证方式：OpenId
        return getUserDetails(userDetails);
    }

    public UserDetails loadUserByMobile(String mobile) {
        R<MemberFeignInfo> result = memberFeignClient.loadMemberByMobile(mobile);
        if (!R.isSuccess(result) || result.getData() == null) {
            throw new UsernameNotFoundException(AuthConstants.USER_NOT_EXIST);
        }
        MemberFeignInfo member = result.getData();
        MemberUserDetails userDetails = new MemberUserDetails(member);
        userDetails.setAuthenticationMethod(AuthenticationMethodEnum.MOBILE.getValue());   // 认证方式：OpenId
        return getUserDetails(userDetails);
    }

    public UserDetails loadUserByWechatCode(String code, String iv, String encryptedData) {
        R<MemberFeignInfo> result = memberFeignClient.loadMemberByWechatCode(WxUserInfoFeignInfo.builder().code(code).iv(iv).encryptedData(encryptedData).build());
        if (!R.isSuccess(result) || result.getData() == null) {
            throw new UsernameNotFoundException(AuthConstants.USER_NOT_EXIST);
        }
        MemberFeignInfo member = result.getData();
        MemberUserDetails userDetails = new MemberUserDetails(member);
        userDetails.setAuthenticationMethod(AuthenticationMethodEnum.OPENID.getValue());   // 认证方式：OpenId
        return getUserDetails(userDetails);
    }

    public UserDetails loadUserByOpenId(String openId) {
        R<MemberFeignInfo> result = memberFeignClient.loadMemberByOpenId(openId);
        if (!R.isSuccess(result) || result.getData() == null) {
            throw new UsernameNotFoundException(AuthConstants.USER_NOT_EXIST);
        }
        MemberFeignInfo member = result.getData();
        MemberUserDetails userDetails = new MemberUserDetails(member);
        userDetails.setAuthenticationMethod(AuthenticationMethodEnum.OPENID.getValue());   // 认证方式：OpenId
        return getUserDetails(userDetails);
    }

    @NotNull
    private UserDetails getUserDetails(MemberUserDetails userDetails) {
        if (userDetails == null) {
            throw new UsernameNotFoundException(AuthConstants.USER_NOT_EXIST);
        } else if (!userDetails.isEnabled()) {
            throw new DisabledException("该账户已被禁用!");
        } else if (!userDetails.isAccountNonLocked()) {
            throw new LockedException("该账号已被锁定!");
        } else if (!userDetails.isAccountNonExpired()) {
            throw new AccountExpiredException("该账号已过期!");
        }
        return userDetails;
    }
}