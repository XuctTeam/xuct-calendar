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

package cn.com.xuct.calendar.common.security.component;

import cn.com.xuct.calendar.common.core.constant.CacheConstants;
import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.UserInfo;
import cn.com.xuct.calendar.common.security.serivces.OAuthUser;
import cn.com.xuct.calendar.ums.oauth.client.MemberFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户详细信息
 *
 * @author lengleng hccake
 */
@Slf4j
@RequiredArgsConstructor
public class OAuthUserDetailsWxServiceImpl implements OAuthUserDetailsService {

    private final MemberFeignClient remoteUserService;

    private final CacheManager cacheManager;

    /**
     * 微信小程序登录
     *
     * @param openId 手机号
     * @return
     */
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String openId) {
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
//        if (cache != null && cache.get(phone) != null) {
//            return (PigUser) cache.get(phone).get();
//        }

        R<UserInfo> result = remoteUserService.loadMemberByOpenId(openId, SecurityConstants.FROM_IN);
        UserDetails userDetails = getUserDetails(result , true);
        if (cache != null) {
            cache.put(openId, userDetails);
        }
        return null;
    }

    /**
     * check-token 使用
     *
     * @param pigUser user
     * @return
     */
    @Override
    public UserDetails loadUserByUser(OAuthUser authUser) {
        //return this.loadUserByUsername(pigUser.getPhone());
        return null;
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

}
