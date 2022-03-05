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

    @Async
    @EventListener
    public void listenerEvent(GroupEvent groupEvent) {
        log.info("group event:: event = {}", groupEvent.toString());
        MemberMessage memberMessage = null;
        JSONObject jsonObject = null;
        Long createMemberId = groupEvent.getCreateMemberId();
        List<Long> memberIds = groupEvent.getMemberIds();
        List<MemberMessage> memberMessages = Lists.newArrayList();
        for (int i = 0, j = memberIds.size(); i < j; i++) {
            memberMessage = new MemberMessage();
            memberMessage.setType(MemberMessageTypeEnum.GROUP);
            memberMessage.setOperation(2);
            memberMessage.setStatus(0);
            memberMessage.setMemberId(memberIds.get(i));
            jsonObject = new JSONObject();
            jsonObject.put("groupId", groupEvent.getGroupId()).put("groupName", groupEvent.getGroupName()).put("createMemberId", createMemberId);
            memberMessage.setContent(jsonObject);
            memberMessages.add(memberMessage);
        }
        if (CollectionUtils.isEmpty(memberMessages)) return;
        memberMessageService.saveBatch(memberMessages);
    }

}