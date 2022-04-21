/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: WeappAuthenticationProvider
 * Author:   Administrator
 * Date:     2021/11/14 21:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot.extension;

import cn.com.xuct.calendar.uaa.boot.core.userdetails.membner.MemberUserDetailsService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;

import java.util.HashSet;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/14
 * @since 1.0.0
 */
@Slf4j
public class WechatAuthenticationProvider implements AuthenticationProvider, MessageSourceAware {

    @Setter
    private UserDetailsService userDetailsService;

    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //认证代码，认证通过返回认证对象，失败返回null
        WechatAuthenticationToken token = (WechatAuthenticationToken) authentication;
        if (!StringUtils.hasLength(token.getCode())) {
            log.error("传入code为空");
            throw new BadCredentialsException(this.messages.getMessage("WechatAuthenticationProvider.badCredentials", "Invalid code"));
        }
        UserDetails userDetails = ((MemberUserDetailsService) userDetailsService).loadUserByWechatCode(token.getCode(), token.getIv(), token.getEncryptedData());
        WechatAuthenticationToken result = new WechatAuthenticationToken(userDetails, new HashSet<>());
        result.setDetails(authentication.getDetails());
        return result;

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (WechatAuthenticationToken.class.isAssignableFrom(aClass));
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }
}