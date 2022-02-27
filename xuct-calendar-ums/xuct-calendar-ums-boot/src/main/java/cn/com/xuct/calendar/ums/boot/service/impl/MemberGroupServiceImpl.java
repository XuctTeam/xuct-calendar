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
import cn.com.xuct.calendar.ums.api.dto.GroupCountDto;
import cn.com.xuct.calendar.ums.api.entity.Group;
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
    public void applyJoinGroup(GroupCountDto group, MemberGroup memberGroup) {
        memberGroup.setStatus(GroupMemberStatusEnum.APPLY);
        memberGroup.setCreateTime(new Date());
        this.save(memberGroup);
        MemberMessage memberMessage = new MemberMessage();
        memberMessage.setMemberId(group.getMemberId());
        memberMessage.setType(MemberMessageTypeEnum.GROUP);
        memberMessage.setOperation(0);
        memberMessage.setStatus(0);
        JSONObject content = new JSONObject();
        content.append("member_id", String.valueOf(memberGroup.getMemberId()));
        memberMessage.setContent(content);
        memberMessageService.save(memberMessage);
    }
}