/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: GroupServiceImpl
 * Author:   Derek Xu
 * Date:     2022/2/7 16:13
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.service.impl;

import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.db.service.BaseServiceImpl;
import cn.com.xuct.calendar.common.module.enums.CommonPowerEnum;
import cn.com.xuct.calendar.common.module.enums.CommonStatusEnum;
import cn.com.xuct.calendar.ums.api.dto.GroupInfoDto;
import cn.com.xuct.calendar.ums.api.dto.GroupMemberInfoDto;
import cn.com.xuct.calendar.ums.api.entity.Group;
import cn.com.xuct.calendar.ums.api.entity.MemberGroup;
import cn.com.xuct.calendar.ums.api.vo.GroupMemberTreeVo;
import cn.com.xuct.calendar.ums.boot.mapper.GroupMapper;
import cn.com.xuct.calendar.ums.boot.service.IGroupService;
import cn.com.xuct.calendar.ums.boot.service.IMemberGroupService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/7
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class GroupServiceImpl extends BaseServiceImpl<GroupMapper, Group> implements IGroupService {

    private final IMemberGroupService memberGroupService;

    @Override
    public List<GroupInfoDto> listGroupCountByMember(final Long memberId) {
        return ((GroupMapper) super.getBaseMapper()).findGroupCountByMember(memberId);
    }

    @Override
    public List<GroupMemberTreeVo> listGroupTree(final Long memberId) {
        List<GroupMemberInfoDto> members = memberGroupService.listAllGroupMemberByMemberId(memberId);
        if (CollectionUtils.isEmpty(members)) {
            return Lists.newArrayList();
        }
        List<GroupMemberTreeVo> groupMemberTreeVos = Lists.newArrayList();
        List<String> groupIds = members.stream().map(GroupMemberInfoDto::getGroupId).collect(Collectors.toList());
        Map<Long, Group> groupMap = super.find(Column.in("id", groupIds)).stream().collect(Collectors.toMap(Group::getId, item -> item));
        Map<String, List<GroupMemberInfoDto>> memberGroupIds = members.stream().collect(Collectors.groupingBy(GroupMemberInfoDto::getGroupId));
        GroupMemberTreeVo treeVo = null;
        Group group = null;
        for (String groupId : memberGroupIds.keySet()) {
            treeVo = new GroupMemberTreeVo();
            group = groupMap.get(Long.valueOf(groupId));
            treeVo.setGroupName(group.getName());
            treeVo.setGroupCreateMemberId(String.valueOf(group.getMemberId()));
            treeVo.setGroupId(groupId);
            treeVo.setMembers(memberGroupIds.get(groupId));
            groupMemberTreeVos.add(treeVo);
        }
        return groupMemberTreeVos;
    }

    @Override
    public GroupInfoDto getGroupCountByGroupId(final Long id) {
        return ((GroupMapper) super.getBaseMapper()).getGroupCountByGroupId(id);
    }

    @Override
    public List<GroupInfoDto> pageGroupBySearch(final Long memberId, final String word, final Integer page, final Integer limit, final String hasPass, final String dateScope, final String numCount) {
        return ((GroupMapper) super.getBaseMapper()).findGroupBySearch(memberId, word, page * limit, limit, hasPass, dateScope, numCount);
    }

    @Override
    public List<GroupMemberInfoDto> mineApplyGroup(Long memberId) {
        return ((GroupMapper) super.getBaseMapper()).mineApplyGroup(memberId);
    }

    @Override
    public List<GroupMemberInfoDto> applyMineGroup(Long memberId) {
        return ((GroupMapper) super.getBaseMapper()).applyMineGroup(memberId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addGroup(final Long memberId, final String name, final String password, final String imageUrl, final String power, final Integer num, final String no) {
        Group group = new Group();
        group.setName(name);
        group.setMemberId(memberId);
        group.setImages(StringUtils.hasLength(imageUrl) ? imageUrl : null);
        group.setPassword(StringUtils.hasLength(password) ? password : null);
        group.setPower(CommonPowerEnum.valueOf(power));
        group.setStatus(CommonStatusEnum.NORMAL);
        group.setNum(num);
        group.setNo(no);
        this.save(group);
        MemberGroup memberGroup = new MemberGroup();
        memberGroup.setGroupId(group.getId());
        memberGroup.setMemberId(memberId);
        memberGroupService.save(memberGroup);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> deleteGroup(Long id) {
        List<Long> memberIds = memberGroupService.deleteAllByGroupId(id);
        super.removeById(id);
        return memberIds;
    }

    @Override
    public void removeAllGroupByMemberId(Long memberId) {
        super.removeByMap(new HashMap<String, Object>() {{
            put("member_id", memberId);
        }});
    }
}