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
import cn.com.xuct.calendar.common.module.feign.OpenIdInfo;
import cn.com.xuct.calendar.common.module.feign.UserInfo;
import cn.com.xuct.calendar.ums.oauth.client.MemberFeignClient;
import cn.hutool.core.util.ArrayUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.BadCredentialsException;
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
public class OAuthUserDetailsWxMemberServiceImpl implements OAuthUserDetailsService {

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

        R<OpenIdInfo> openIdInfoR = memberFeignClient.getOpenInfo(username, SecurityConstants.FROM_IN);
        OpenIdInfo openIdInfo = RetOps.of(openIdInfoR).getData().orElseThrow(() -> new BadCredentialsException("获取OpenId无效"));
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
        if (cache != null && cache.get(openIdInfo.getOpenId()) != null) {
            return (OAuthUser) cache.get(openIdInfo.getOpenId()).get();
        }


        R<UserInfo> result = memberFeignClient.loadMemberByUserName(username, SecurityConstants.FROM_IN);
        UserDetails userDetails = getUserDetails(result, true);
        if (cache != null) {
            cache.put(username, userDetails);
        }
        return userDetails;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
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
        return this.loadUserByUsername(oAuthUser.getUsername());
    }
}
