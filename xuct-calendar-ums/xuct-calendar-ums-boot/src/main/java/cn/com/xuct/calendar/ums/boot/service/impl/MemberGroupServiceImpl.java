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
import cn.com.xuct.calendar.ums.api.dto.GroupInfoDto;
import cn.com.xuct.calendar.ums.api.entity.MemberGroup;
import cn.com.xuct.calendar.ums.api.entity.MemberMessage;
import cn.com.xuct.calendar.ums.boot.mapper.MemberGroupMapper;
import cn.com.xuct.calendar.ums.boot.service.IMemberGroupService;
import cn.com.xuct.calendar.ums.boot.service.IMemberMessageService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

    private final IMemberMessageService memberMessageService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyJoinGroup(Long groupId, String groupName, Long groupCreateMemberId, Long memberId) {
        MemberGroup memberGroup = new MemberGroup();
        memberGroup.setGroupId(groupId);
        memberGroup.setMemberId(memberId);
        memberGroup.setStatus(GroupMemberStatusEnum.APPLY);
        this.save(memberGroup);
        /* 给群组拥有发送消息 */
        MemberMessage memberMessage = new MemberMessage();
        memberMessage.setMemberId(groupCreateMemberId);
        memberMessage.setType(MemberMessageTypeEnum.GROUP);
        memberMessage.setOperation(0);
        memberMessage.setStatus(0);
        JSONObject content = new JSONObject();
        content.put("group_id", String.valueOf(groupId)).put("group_name", groupId).put("member_id", String.valueOf(memberId));
        memberMessage.setContent(content);
        memberMessageService.save(memberMessage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyAgreeJoinGroup(Long groupId, Long memberId) {
        ((MemberGroupMapper) super.getBaseMapper()).applyAgreeJoinGroup(groupId, memberId);
        this.saveApplyGroup(groupId, memberId, 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyRefuseJoinGroup(Long groupId, Long memberId) {
        ((MemberGroupMapper) super.getBaseMapper()).applyRefuseJoinGroup(groupId, memberId);
        this.saveApplyGroup(groupId, memberId, 2);
    }

    /**
     * 功能描述: <br>
     * 〈保存群组操作消息〉
     *
     * @param groupId
     * @param memberId
     * @param operate
     * @return:void
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/2/28 15:51
     */
    private void saveApplyGroup(Long groupId, Long memberId, Integer operate) {
        MemberMessage memberMessage = new MemberMessage();
        memberMessage.setMemberId(memberId);
        memberMessage.setType(MemberMessageTypeEnum.GROUP);
        memberMessage.setOperation(operate);
        memberMessage.setStatus(0);
        JSONObject content = new JSONObject();
        content.put("group_id", String.valueOf(groupId));
        memberMessage.setContent(content);
        memberMessageService.save(memberMessage);
    }
}