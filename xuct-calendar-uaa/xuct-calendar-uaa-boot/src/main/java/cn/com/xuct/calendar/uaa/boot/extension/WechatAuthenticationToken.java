/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: WeappAuthenticationToken
 * Author:   Derek Xu
 * Date:     2021/11/15 10:36
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot.extension;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/15
 * @since 1.0.0
 */
public class WechatAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 550L;

    private Object principal;
    @Getter
    private String code;

    @Getter
    private String encryptedData;

    @Getter
    private String iv;

    /**
     * 账号校验之前的token构建
     */
    public WechatAuthenticationToken(String code, String iv, String encryptedData) {
        super(null);
        this.code = code;
        this.iv = iv;
        this.encryptedData = encryptedData;
        setAuthenticated(false);
    }

    /**
     * 账号校验成功之后的token构建
     *
     * @param principal
     * @param authorities
     */
    public WechatAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(isAuthenticated == false, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    public void eraseCredentials() {
        super.eraseCredentials();
    }
}