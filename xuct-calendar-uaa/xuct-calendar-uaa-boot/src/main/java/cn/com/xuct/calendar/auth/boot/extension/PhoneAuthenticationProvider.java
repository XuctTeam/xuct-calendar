/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: PhoneAuthenticationProvider
 * Author:   Administrator
 * Date:     2021/11/14 21:17
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.auth.boot.extension;

import cn.com.xuct.calendar.auth.boot.core.userdetails.membner.MemberUserDetailsService;
import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashSet;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/14
 * @since 1.0.0
 */
public class PhoneAuthenticationProvider implements AuthenticationProvider, MessageSourceAware {

    @Setter
    private UserDetailsService userDetailsService;

    @Setter
    private StringRedisTemplate stringRedisTemplate;

    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //认证代码，认证通过返回认证对象，失败返回null
        PhoneAuthenticationToken token = (PhoneAuthenticationToken) authentication;
        if (token.getPhone() == null)
            throw new BadCredentialsException(this.messages.getMessage("PhoneAuthenticationProvider.badCredentials", "无效的电话号码"));

        String code = stringRedisTemplate.opsForValue().get(RedisConstants.MEMBER_PHONE_LOGIN_CODE_KEY.concat(token.getPhone()));
        if (code == null || !token.getCode().equals(code)) {
            throw new BadCredentialsException(this.messages.getMessage("PhoneAuthenticationProvider code is not eq", "无效的验证码"));
        }
        UserDetails userDetails = ((MemberUserDetailsService) userDetailsService).loadUserByMobile(token.getPhone());
        PhoneAuthenticationToken result = new PhoneAuthenticationToken(userDetails, new HashSet<>());
        result.setDetails(authentication.getDetails());
        return result;

    }

    @Override
    public boolean supports(Class<?> aClass) {
        //Manager传递token给provider，调用本方法判断该provider是否支持该token。不支持则尝试下一个filter
        //本类支持的token类：UserPasswordAuthenticationToken
        return (PhoneAuthenticationToken.class.isAssignableFrom(aClass));
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }
}