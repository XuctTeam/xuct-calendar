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

import cn.com.xuct.calendar.common.db.service.IBaseService;
import cn.com.xuct.calendar.ums.api.dto.GroupMemberInfoDto;
import cn.com.xuct.calendar.ums.api.entity.MemberGroup;
import cn.com.xuct.calendar.ums.boot.mapper.MemberGroupMapper;

import java.util.List;

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
     * 群组内所有人
     *
     * @param memberId
     * @return
     */
    List<GroupMemberInfoDto> list(final Long groupId, final Long memberId);

    /**
     * 查询群组下所有用户
     *
     * @param groupId
     * @return
     */
    List<GroupMemberInfoDto> queryMembersByGroupId(Long groupId, final Long memberId);

    /**
     * 通过会员id查询
     *
     * @param memberIds
     * @return
     */
    List<GroupMemberInfoDto> queryMemberIds(List<Long> memberIds);

    /**
     * 申请加入群组
     *
     * @param groupId
     * @param groupName
     * @param memberId
     */
    void applyJoinGroup(Long groupId, String groupName, Long groupCreateMemberId, Long memberId);


    /**
     * 通过群组删除用户
     *
     * @param groupId
     */
    List<Long> deleteAllByGroupId(Long groupId);

    /**
     * 请离用户
     *
     * @param groupId
     * @param memberId
     */
    void leaveOut(Long groupId, Long memberId);

    /**
     * 通过用户id删除用户下所有组内用户
     *
     * @param memberId
     */
    void removeGroupMemberByMemberId(final Long memberId);
}