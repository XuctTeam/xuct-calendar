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
package cn.com.xuct.calendar.service.impl;

import cn.com.xuct.calendar.common.module.dto.UserInfoDto;
import cn.com.xuct.calendar.dao.entity.UserRole;
import cn.com.xuct.calendar.dao.mapper.UserRoleMapper;
import cn.com.xuct.calendar.service.IUserRoleService;
import cn.com.xuct.calendar.service.base.BaseServiceImpl;
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
    public UserInfoDto selectUserByUserName(String userName) {
        return userRoleMapper.selectUserByUserName(userName);
    }
}