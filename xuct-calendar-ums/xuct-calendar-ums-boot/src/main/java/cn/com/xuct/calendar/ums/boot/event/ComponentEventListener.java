/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: AlarmNotifyEventListener
 * Author:   Derek Xu
 * Date:     2022/3/30 11:15
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
import org.jetbrains.annotations.NotNull;
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
 * @create 2022/3/30
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ComponentEventListener {

    private final IMemberMessageService memberMessageService;


    @Async
    @EventListener
    public void listenerDeleteEvent(ComponentDelEvent delEvent) {
        this.batchOperateMessage(delEvent, delEvent.getIds(), 2);
    }


    @Async
    @EventListener
    public void listenerAlarmEvent(AlarmNotifyEvent notifyEvent) {
        this.batchOperateMessage(notifyEvent, notifyEvent.getIds(), 3);
    }

    private void batchOperateMessage(ComponentEvent event, List<Long> memberIds, Integer operate) {
        MemberMessage memberMessage = null;
        List<MemberMessage> memberMessageList = Lists.newArrayList();
        for (int i = 0, j = memberIds.size(); i < j; i++) {
            memberMessage = new MemberMessage();
            memberMessage.setTitle(event.getSummary());
            memberMessage.setMemberId(memberIds.get(i));
            memberMessage.setType(MemberMessageTypeEnum.EVENT);
            memberMessage.setOperation(operate);
            memberMessage.setStatus(0);
            JSONObject content = geMessageContent(event);
            memberMessage.setContent(content);
            memberMessageList.add(memberMessage);
        }
        if (CollectionUtils.isEmpty(memberMessageList)) return;
        memberMessageService.saveBatch(memberMessageList);
    }


    @NotNull
    private JSONObject geMessageContent(ComponentEvent componentEvent) {
        JSONObject content = new JSONObject();
        content.put("summary", componentEvent.getSummary());
        content.put("startDate", componentEvent.getStartDate());
        content.put("createMemberName", componentEvent.getCreateMemberName());
        content.put("componentId", componentEvent.getComponentId());
        content.put("location", componentEvent.getLocation());
        content.put("repeat", componentEvent.getRepeat());
        content.put("triggerSec", componentEvent.getTriggerSec());
        return content;
    }
}