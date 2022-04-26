/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: GroupEventListener
 * Author:   Derek Xu
 * Date:     2022/3/5 18:33
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.event;

import cn.com.xuct.calendar.common.module.enums.MemberMessageTypeEnum;
import cn.com.xuct.calendar.ums.api.entity.MemberMessage;
import cn.com.xuct.calendar.ums.boot.service.IMemberMessageService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/5
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GroupEventListener {

    private final IMemberMessageService memberMessageService;

    /**
     * 功能描述: <br>
     * 〈群组删除消息〉
     *
     * @param deleteEvent
     * @return:void
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/3/8 18:04
     */
    @Async
    @EventListener
    public void listenerDeleteEvent(GroupDeleteEvent deleteEvent) {

        log.info("group event:: event = {}", deleteEvent.toString());
        MemberMessage memberMessage = null;
        JSONObject jsonObject = null;
        Long createMemberId = deleteEvent.getCreateMemberId();
        List<Long> memberIds = deleteEvent.getMemberIds();
        List<MemberMessage> memberMessages = Lists.newArrayList();
        for (int i = 0, j = memberIds.size(); i < j; i++) {
            memberMessage = new MemberMessage();
            memberMessage.setTitle(deleteEvent.getGroupName());
            memberMessage.setType(MemberMessageTypeEnum.GROUP);
            memberMessage.setOperation(3);
            memberMessage.setStatus(0);
            memberMessage.setMemberId(memberIds.get(i));
            jsonObject = new JSONObject();
            jsonObject.put("groupId", deleteEvent.getGroupId()).put("groupName", deleteEvent.getGroupName()).put("createMemberId", createMemberId);
            memberMessage.setContent(jsonObject);
            memberMessages.add(memberMessage);
        }
        if (CollectionUtils.isEmpty(memberMessages)) return;
        memberMessageService.saveBatch(memberMessages);
    }

    /**
     * 功能描述: <br>
     * 〈申请入群消息〉
     *
     * @param applyEvent
     * @return:void
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/3/8 18:03
     */
    @Async
    @EventListener
    public void listenerApplyEvent(GroupApplyEvent applyEvent) {
        MemberMessage memberMessage = new MemberMessage();
        memberMessage.setMemberId(applyEvent.getApplyMemberId());
        memberMessage.setType(MemberMessageTypeEnum.GROUP);
        memberMessage.setOperation(0);
        memberMessage.setStatus(0);
        memberMessage.setTitle(applyEvent.getGroupName());
        JSONObject content = new JSONObject();
        content.put("group_id", String.valueOf(applyEvent.getGroupId()))
                .put("group_name", applyEvent.getGroupName())
                .put("member_id", String.valueOf(applyEvent.getApplyMemberId()));
        memberMessage.setContent(content);
        memberMessageService.save(memberMessage);
    }

    /**
     * 功能描述: <br>
     * 〈群组同意、拒绝入群操作〉
     *
     * @param optionEvent
     * @return:void
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/3/8 18:03
     */
    @Async
    @EventListener
    public void listenerApplyOptionEvent(GroupApplyOptionEvent optionEvent) {
        MemberMessage memberMessage = new MemberMessage();
        memberMessage.setTitle(optionEvent.getGroupName());
        memberMessage.setMemberId(optionEvent.getMemberId());
        memberMessage.setType(MemberMessageTypeEnum.GROUP);
        memberMessage.setOperation(optionEvent.getOperate());
        memberMessage.setStatus(0);
        JSONObject content = new JSONObject();
        content.put("group_id", String.valueOf(optionEvent.getGroupId()));
        memberMessage.setContent(content);
        memberMessageService.save(memberMessage);
    }

    /**
     * 功能描述: <br>
     * 〈群组人员离开〉
     *
     * @param leaveEvent
     * @return:void
     * @since: 1.0.0
     * @Author:
     * @Date: 2022/3/8 20:59
     */
    @Async
    @EventListener
    public void listenerLeaveOutEvent(GroupLeaveEvent leaveEvent) {
        MemberMessage memberMessage = new MemberMessage();
        memberMessage.setMemberId(leaveEvent.getMemberId());
        memberMessage.setType(MemberMessageTypeEnum.GROUP);
        memberMessage.setOperation(leaveEvent.getOperate());
        memberMessage.setStatus(0);
        memberMessage.setTitle(leaveEvent.getGroupName());
        JSONObject content = new JSONObject();
        content.put("group_id", leaveEvent.getGroupId()).put("name", leaveEvent.getGroupName());
        memberMessage.setContent(content);
        memberMessageService.save(memberMessage);
    }
}