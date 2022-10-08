/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: UserFeignController
 * Author:   Derek Xu
 * Date:     2022/9/6 17:25
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.controller.feign;

import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.UserInfo;
import cn.com.xuct.calendar.common.security.annotation.Inner;
import cn.com.xuct.calendar.ums.boot.service.IUserRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/9/6
 * @since 1.0.0
 */
@Slf4j
@RestController
@Tag(name = "【远程调用】用户接口")
@RequestMapping("/api/feign/v1/user")
@RequiredArgsConstructor
public class UserFeignController {

    private final IUserRoleService userRoleService;

    @Inner
    @GetMapping("/get/username")
    public R<UserInfo> getUserByUserName(@RequestParam("username") String username) {
        return R.data(null);
    }
}