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

import cn.com.xuct.calendar.common.core.utils.PinYinUtils;
import cn.com.xuct.calendar.common.db.service.BaseServiceImpl;
import cn.com.xuct.calendar.common.module.enums.GroupMemberStatusEnum;
import cn.com.xuct.calendar.common.security.utils.SecurityUtils;
import cn.com.xuct.calendar.ums.api.dto.GroupMemberInfoDto;
import cn.com.xuct.calendar.ums.api.entity.MemberGroup;
import cn.com.xuct.calendar.ums.api.vo.GroupMemberPinYinVo;
import cn.com.xuct.calendar.ums.boot.mapper.MemberGroupMapper;
import cn.com.xuct.calendar.ums.boot.service.IMemberGroupService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

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
    public List<GroupMemberPinYinVo> listByPinYin(final Long groupId) {
        List<GroupMemberInfoDto> memberInfoDtos = this.list(groupId, SecurityUtils.getUserId());
        if (CollectionUtils.isEmpty(memberInfoDtos))
            return Lists.newArrayList();
        TreeMap<String, List<GroupMemberInfoDto>> pinyinVos = new TreeMap<String, List<GroupMemberInfoDto>>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        memberInfoDtos.stream().forEach(x -> {
            String first = PinYinUtils.first(x.getName());
            if (!pinyinVos.containsKey(first)) {
                pinyinVos.put(first, Lists.newArrayList(x));
                return;
            }
            pinyinVos.get(first).add(x);
        });
        List<GroupMemberPinYinVo> groupMemberPinYinVos = Lists.newArrayList();
        GroupMemberPinYinVo pinVo = null;
        for (String pinyin : pinyinVos.keySet()) {
            pinVo = new GroupMemberPinYinVo();
            pinVo.setCharCode(pinyin);
            pinVo.setMembers(pinyinVos.get(pinyin));
            groupMemberPinYinVos.add(pinVo);
        }
        return groupMemberPinYinVos;
    }

    @Override
    public List<GroupMemberInfoDto> listAllGroupMemberByMemberId(Long memberId) {
        return ((MemberGroupMapper) super.getBaseMapper()).listAllGroupMemberByMemberId(memberId);
    }

    @Override
    public List<GroupMemberInfoDto> listByGroupIdAndNotMember(final Long groupId, final Long memberId) {
        return ((MemberGroupMapper) super.getBaseMapper()).queryMembersByGroupId(groupId, memberId);
    }

    @Override
    public List<GroupMemberInfoDto> listByMemberIds(List<Long> memberIds) {
        return ((MemberGroupMapper) super.getBaseMapper()).queryMemberIds(memberIds);
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


    @Override
    public GroupMemberInfoDto getGroupMember(Long groupId, Long memberId) {
        return ((MemberGroupMapper) super.getBaseMapper()).getGroupMember(groupId, memberId);
    }

    private List<GroupMemberInfoDto> list(final Long groupId, Long memberId) {
        return ((MemberGroupMapper) super.getBaseMapper()).list(groupId, memberId);
    }
}