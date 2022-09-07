/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: OAuthClientCacheSupport
 * Author:   Derek Xu
 * Date:     2022/9/5 16:37
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot.support.core;

import cn.com.xuct.calendar.common.core.constant.CacheConstants;
import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.common.core.res.AuthResCode;
import cn.com.xuct.calendar.common.core.res.RetOps;
import cn.com.xuct.calendar.common.security.excpetion.OAuthClientException;
import cn.com.xuct.calendar.ums.oauth.client.ClientDetailsFeignClient;
import cn.com.xuct.calendar.ums.oauth.dto.OAuthDetailsDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/9/5
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthClientCacheSupport {


    private final ClientDetailsFeignClient clientDetailsFeignClient;

    @Cacheable(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientId", unless = "#result == null")
    public OAuthDetailsDto getOAthDetails(final String clientId) {
        OAuth2Error error = new OAuth2Error(AuthResCode.CLIENT_AUTHENTICATION_FAILED.getMessage());
        OAuthDetailsDto clientDetails = RetOps.of(clientDetailsFeignClient.getClientDetailsById(clientId, SecurityConstants.FROM_IN)).getData()
                .orElseThrow(() -> new OAuth2AuthorizationCodeRequestAuthenticationException(error, null));
        return clientDetails;
    }


}