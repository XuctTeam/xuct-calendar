/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: UserRoleMapper
 * Author:   Derek Xu
 * Date:     2021/11/22 16:41
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.mapper;

import cn.com.xuct.calendar.common.module.dto.UserInfoDto;
import cn.com.xuct.calendar.ums.api.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/22
 * @since 1.0.0
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 通过登录用户名查询用户
     *
     * @param userName
     * @return
     */
    UserInfoDto selectUserByUserName(@Param("userName") String userName);
}