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

import cn.com.xuct.calendar.common.module.enums.CommonStatusEnum;
import cn.com.xuct.calendar.service.base.BaseServiceImpl;
import cn.com.xuct.calendar.ums.api.dto.GroupInfoDto;
import cn.com.xuct.calendar.ums.api.entity.Group;
import cn.com.xuct.calendar.ums.api.entity.MemberGroup;
import cn.com.xuct.calendar.ums.boot.mapper.GroupMapper;
import cn.com.xuct.calendar.ums.boot.service.IGroupService;
import cn.com.xuct.calendar.ums.boot.service.IMemberGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

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
    public List<GroupInfoDto> findGroupCountByMember(Long memberId) {
        return ((GroupMapper) super.getBaseMapper()).findGroupCountByMember(memberId);
    }

    @Override
    public GroupInfoDto getGroupCountByGroupId(Long id) {
        return ((GroupMapper) super.getBaseMapper()).getGroupCountByGroupId(id);
    }

    @Override
    public List<GroupInfoDto> findGroupBySearch(String word) {
        return ((GroupMapper) super.getBaseMapper()).findGroupBySearch(word);
    }

    @Override
    public List<GroupInfoDto> mineApplyGroup(Long memberId) {
        return ((GroupMapper) super.getBaseMapper()).mineApplyGroup(memberId);
    }

    @Override
    public List<GroupInfoDto> applyMineGroup(Long memberId) {
        return ((GroupMapper) super.getBaseMapper()).applyMineGroup(memberId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addGroup(Long memberId, String name, String imageUrl) {
        Group group = new Group();
        group.setName(name);
        if (StringUtils.hasLength(imageUrl)) {
            group.setImages(imageUrl);
        }
        group.setMemberId(memberId);
        group.setStatus(CommonStatusEnum.NORMAL);
        this.save(group);
        MemberGroup memberGroup = new MemberGroup();
        memberGroup.setGroupId(group.getId());
        memberGroup.setMemberId(memberId);
        memberGroupService.save(memberGroup);
    }
}