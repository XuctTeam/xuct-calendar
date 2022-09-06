/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: UserFeignClient
 * Author:   Derek Xu
 * Date:     2022/9/6 17:20
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.oauth.client;

import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.common.core.constant.ServiceNameConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/9/6
 * @since 1.0.0
 */
@FeignClient(name = ServiceNameConstants.UMS_SERVICE, contextId = "users")
public interface UserFeignClient {

    @GetMapping("/api/feign/v1/user/get/username")
    R<UserInfo> info(@RequestParam("username") String username, @RequestHeader(SecurityConstants.FROM) String from);
}