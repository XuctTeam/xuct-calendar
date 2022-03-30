/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: UserRoleServiceImpl
 * Author:   Derek Xu
 * Date:     2021/11/22 16:42
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.service.impl;

import cn.com.xuct.calendar.common.module.feign.UserInfoFeignInfoRes;
import cn.com.xuct.calendar.ums.api.entity.UserRole;
import cn.com.xuct.calendar.ums.boot.mapper.UserRoleMapper;
import cn.com.xuct.calendar.common.db.service.BaseServiceImpl;
import cn.com.xuct.calendar.ums.boot.service.IUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/22
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    private final UserRoleMapper userRoleMapper;

    @Override
    public UserInfoFeignInfoRes selectUserByUserName(String userName) {
        return userRoleMapper.selectUserByUserName(userName);
    }
}