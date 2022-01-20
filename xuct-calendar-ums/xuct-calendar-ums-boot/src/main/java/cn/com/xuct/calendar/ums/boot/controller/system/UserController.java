/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: UserController
 * Author:   Derek Xu
 * Date:     2021/11/22 17:08
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.controller.system;

import cn.com.xuct.calendar.common.module.dto.UserInfoDto;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.ums.boot.service.IUserRoleService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/22
 * @since 1.0.0
 */
@RestController
@Api(tags = "系统管理员接口")
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserRoleService userRoleService;

    @GetMapping("/get/username")
    public R<UserInfoDto> getUserByUserName(@RequestParam("username") String username) {
        return R.data(userRoleService.selectUserByUserName(username));
    }
}