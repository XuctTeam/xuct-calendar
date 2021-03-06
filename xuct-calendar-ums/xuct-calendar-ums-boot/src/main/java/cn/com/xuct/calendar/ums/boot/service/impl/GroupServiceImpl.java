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

import cn.com.xuct.calendar.common.db.service.BaseServiceImpl;
import cn.com.xuct.calendar.common.module.enums.CommonPowerEnum;
import cn.com.xuct.calendar.common.module.enums.CommonStatusEnum;
import cn.com.xuct.calendar.ums.api.dto.GroupInfoDto;
import cn.com.xuct.calendar.ums.api.dto.GroupMemberInfoDto;
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
    public List<GroupInfoDto> findGroupBySearchByPage(final Long memberId, final String word, final Integer page, final Integer limit) {
        return ((GroupMapper) super.getBaseMapper()).findGroupBySearch(memberId, word, page * limit, limit);
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
}