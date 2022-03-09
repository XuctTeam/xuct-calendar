/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: MemberGroupServiceImpl
 * Author:   Derek Xu
 * Date:     2022/2/13 21:58
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.service.impl;

import cn.com.xuct.calendar.common.module.enums.GroupMemberStatusEnum;
import cn.com.xuct.calendar.common.module.enums.MemberMessageTypeEnum;
import cn.com.xuct.calendar.service.base.BaseServiceImpl;
import cn.com.xuct.calendar.ums.api.dto.GroupMemberInfoDto;
import cn.com.xuct.calendar.ums.api.entity.MemberGroup;
import cn.com.xuct.calendar.ums.api.entity.MemberMessage;
import cn.com.xuct.calendar.ums.boot.mapper.MemberGroupMapper;
import cn.com.xuct.calendar.ums.boot.service.IMemberGroupService;
import cn.com.xuct.calendar.ums.boot.service.IMemberMessageService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/13
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class MemberGroupServiceImpl extends BaseServiceImpl<MemberGroupMapper, MemberGroup> implements IMemberGroupService {

    @Override
    public List<GroupMemberInfoDto> list(Long memberId) {
        return ((MemberGroupMapper) super.getBaseMapper()).list(memberId);
    }

    @Override
    public List<GroupMemberInfoDto> queryMembersByGroupId(Long groupId) {
        return ((MemberGroupMapper) super.getBaseMapper()).queryMembersByGroupId(groupId);
    }

    @Override
    public List<GroupMemberInfoDto> distinctGroupMembers(Long memberId) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyJoinGroup(Long groupId, String groupName, Long groupCreateMemberId, Long memberId) {
        MemberGroup memberGroup = new MemberGroup();
        memberGroup.setGroupId(groupId);
        memberGroup.setMemberId(memberId);
        memberGroup.setStatus(GroupMemberStatusEnum.APPLY);
        this.save(memberGroup);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyAgreeJoinGroup(Long groupId, Long memberId) {
        ((MemberGroupMapper) super.getBaseMapper()).applyAgreeJoinGroup(groupId, memberId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyRefuseJoinGroup(Long groupId, Long memberId) {
        ((MemberGroupMapper) super.getBaseMapper()).applyRefuseJoinGroup(groupId, memberId);
    }

    @Override
    public List<Long> deleteAllByGroupId(Long groupId) {
        List<Long> memberIds = ((MemberGroupMapper) super.getBaseMapper()).findMemberIdsByGroupId(groupId);
        if (CollectionUtils.isEmpty(memberIds)) return Lists.newArrayList();
        super.getBaseMapper().deleteByMap(new HashMap<>() {{
            put("group_id", groupId);
        }});
        return memberIds;
    }

    @Override
    public void leaveOut(Long groupId, Long memberId) {
        super.removeByMap(new HashMap<String, Object>() {{
            put("group_id", groupId);
            put("member_id", memberId);
        }});
    }
}