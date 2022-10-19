/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.com.xuct.calendar.common.security.serivces;

import cn.com.xuct.calendar.common.core.constant.CacheConstants;
import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.res.RetOps;
import cn.com.xuct.calendar.common.core.utils.JsonUtils;
import cn.com.xuct.calendar.common.module.feign.OpenIdInfo;
import cn.com.xuct.calendar.common.module.feign.UserInfo;
import cn.com.xuct.calendar.common.module.feign.req.WxUserInfoFeignInfo;
import cn.com.xuct.calendar.common.security.beans.WxUserName;
import cn.com.xuct.calendar.ums.oauth.client.MemberFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 用户详细信息
 *
 * @author lengleng hccake
 */
@Slf4j
@Primary
@RequiredArgsConstructor
public class OAuthMemberDetailsWxServiceImpl implements OAuthUserDetailsService {

    private final MemberFeignClient memberFeignClient;

    private final CacheManager cacheManager;

    /**
     * 用户名密码登录
     *
     * @param username 用户名
     * @return
     */
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) {
        WxUserName wxUserName = JsonUtils.json2pojo(username, WxUserName.class);
        if (wxUserName == null) new UsernameNotFoundException("获取OpenId无效");
        R<OpenIdInfo> openIdInfoR = memberFeignClient.getOpenInfo(wxUserName.getCode(), SecurityConstants.FROM_IN);
        OpenIdInfo openIdInfo = RetOps.of(openIdInfoR).getData().orElseThrow(() -> new UsernameNotFoundException("获取OpenId无效"));
        return this.getByOpenId(true, openIdInfo.getOpenId(), openIdInfo.getSessionKey(), wxUserName.getIv(), wxUserName.getEncryptedData());
    }

    @Override
    public int getOrder() {
        return 4;
    }


    /**
     * 是否支持此客户端校验
     *
     * @param clientId 目标客户端
     * @return true/false
     */
    @Override
    public boolean support(String clientId, String grantType) {
        return SecurityConstants.WX_GRANT_TYPE.equals(clientId);
    }

    @Override
    public UserDetails loadUserByUser(OAuthUser oAuthUser) {
        return this.getByOpenId(false, oAuthUser.getUsername(), null, null, null);
    }

    private UserDetails getByOpenId(final Boolean login, final String openId, final String sessionKey, final String iv, final String encryptedData) {
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
        if (cache != null && cache.get(openId) != null) {
            return (OAuthUser) cache.get(openId).get();
        }
        R<UserInfo> result = memberFeignClient.loadMemberByOpenId(WxUserInfoFeignInfo.builder().login(login).openId(openId).sessionKey(sessionKey).iv(iv)
                .encryptedData(encryptedData).build(), SecurityConstants.FROM_IN);
        UserDetails userDetails = getUserDetails(result, true);
        if (cache != null) {
            cache.put(userDetails.getUsername(), userDetails);
        }
        return userDetails;
    }
}
