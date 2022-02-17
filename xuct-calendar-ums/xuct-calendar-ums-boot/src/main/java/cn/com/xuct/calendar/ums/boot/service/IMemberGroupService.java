/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: IMemberGroupService
 * Author:   Derek Xu
 * Date:     2022/2/13 21:57
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.service;

import cn.com.xuct.calendar.service.base.IBaseService;
import cn.com.xuct.calendar.ums.api.dto.GroupCountDto;
import cn.com.xuct.calendar.ums.api.entity.Group;
import cn.com.xuct.calendar.ums.api.entity.MemberGroup;
import cn.com.xuct.calendar.ums.boot.mapper.MemberGroupMapper;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/13
 * @since 1.0.0
 */
public interface IMemberGroupService extends IBaseService<MemberGroupMapper, MemberGroup> {

    /**
     * 申请加入群组
     *
     * @param group       申请加入群组
     * @param memberGroup
     */
    void applyJoinGroup(GroupCountDto group, MemberGroup memberGroup);
}