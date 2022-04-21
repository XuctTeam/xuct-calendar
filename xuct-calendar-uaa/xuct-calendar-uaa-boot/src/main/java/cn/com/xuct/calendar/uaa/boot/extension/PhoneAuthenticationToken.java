/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: PhoneAuthenticationToken
 * Author:   Administrator
 * Date:     2021/11/14 21:35
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot.extension;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/14
 * @since 1.0.0
 */
public class PhoneAuthenticationToken extends AbstractAuthenticationToken {

    //用户信息
    private Object principal;
    private String phone;
    private String code;

    public PhoneAuthenticationToken(String phone, String code) {
        super(null);
        this.phone = phone;
        this.code = code;
        //标记未认证
        super.setAuthenticated(false);//注意这个构造方法是认证时使用的
    }

    public PhoneAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        //标记已认证
        super.setAuthenticated(true);//注意这个构造方法是认证成功后使用的
    }

    public String getPhone() {
        return this.phone;
    }

    public String getCode() {
        return this.code;
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @Override
    public Object getPrincipal() {
        return principal;
    }
}