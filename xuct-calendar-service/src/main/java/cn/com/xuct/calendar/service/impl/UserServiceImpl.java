/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: UserServiceImpl
 * Author:   Derek Xu
 * Date:     2021/11/22 15:00
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.service.impl;

import cn.com.xuct.calendar.dao.entity.User;
import cn.com.xuct.calendar.dao.mapper.UserMapper;
import cn.com.xuct.calendar.service.IUserService;
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
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements IUserService {
}