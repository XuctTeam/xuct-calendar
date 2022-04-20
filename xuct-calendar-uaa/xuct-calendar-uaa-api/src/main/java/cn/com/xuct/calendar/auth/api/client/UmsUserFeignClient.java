/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: UserFeignClient
 * Author:   Derek Xu
 * Date:     2021/11/15 12:50
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.auth.api.client;

import cn.com.xuct.calendar.common.module.feign.UserInfoFeignInfo;
import cn.com.xuct.calendar.common.core.res.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/15
 * @since 1.0.0
 */
@FeignClient(value = "dav-ums", contextId = "users")
public interface UmsUserFeignClient {

    /**
     * 通过用户名查询系统用户
     *
     * @param username
     * @return
     */
    @GetMapping("/api/v1/user/get/username")
    R<UserInfoFeignInfo> getUserByUsername(@RequestParam("username") String username);
}