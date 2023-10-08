/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: OauthDetailsFeignClient
 * Author:   Derek Xu
 * Date:     2022/9/2 13:45
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.oauth.client;

import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.common.core.constant.ServiceNameConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.ums.oauth.dto.OAuthDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/9/2
 * @since 1.0.0
 */
@FeignClient(contextId = "oauth-client", value = ServiceNameConstants.UMS_SERVICE)
public interface ClientDetailsFeignClient {

    /**
     * 根据客户端id 查询客户端信息
     *
     * @param clientId 客户 端id
     * @param from     请求来源
     * @return R
     */
    @GetMapping("/api/feign/v1/oauth/client/{clientId}")
    R<OAuthDetailsDto> getClientDetailsById(@PathVariable("clientId") String clientId, @RequestHeader(SecurityConstants.FROM) String from);
}