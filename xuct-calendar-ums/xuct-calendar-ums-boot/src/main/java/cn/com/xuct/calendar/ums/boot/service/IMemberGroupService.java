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
import cn.com.xuct.calendar.ums.api.vo.GroupMemberPinYinVo;
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

    List<GroupMemberPinYinVo> listByPinYin(final Long groupId);

    List<GroupMemberInfoDto> listAllGroupMemberByMemberId(final Long memberId);

    List<GroupMemberInfoDto> listByGroupIdAndNotMember(final Long groupId, final Long memberId);

    List<GroupMemberInfoDto> listByMemberIds(List<Long> memberIds);

    void applyJoinGroup(Long groupId, String groupName, Long groupCreateMemberId, Long memberId);

    List<Long> deleteAllByGroupId(Long groupId);

    void leaveOut(Long groupId, Long memberId);

    GroupMemberInfoDto getGroupMember(final Long groupId, final Long memberId);
}