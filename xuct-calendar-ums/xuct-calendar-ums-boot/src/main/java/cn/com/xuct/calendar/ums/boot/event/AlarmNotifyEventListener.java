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
import cn.com.xuct.calendar.ums.api.entity.Member;
import cn.com.xuct.calendar.ums.api.entity.MemberMessage;
import cn.com.xuct.calendar.ums.boot.service.IMemberMessageService;
import cn.com.xuct.calendar.ums.boot.service.IMemberService;
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
 * @create 2022/3/30
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmNotifyEventListener {

    private final IMemberMessageService memberMessageService;


    @Async
    @EventListener
    public void listener(AlarmNotifyEvent notifyEvent) {
        MemberMessage memberMessage = null;
        List<MemberMessage> memberMessageList = Lists.newArrayList();
        for (int i = 0, j = notifyEvent.getIds().size(); i < j; i++) {
            memberMessage = new MemberMessage();
            memberMessage.setMemberId(notifyEvent.getIds().get(i));
            memberMessage.setType(MemberMessageTypeEnum.EVENT);
            memberMessage.setOperation(0);
            memberMessage.setStatus(0);
            JSONObject content = new JSONObject();
            content.put("create_user_name", notifyEvent.getCreateMemberName());
            content.put("create_member_id", notifyEvent.getCreateMemberId());
            content.put("component_id", notifyEvent.getComponentId());
            content.put("summary", notifyEvent.getSummary());
            content.put("start_date", notifyEvent.getStartDate());
            memberMessage.setContent(content);
            memberMessageList.add(memberMessage);
        }
        if (!CollectionUtils.isEmpty(memberMessageList)) {
            memberMessageService.saveBatch(memberMessageList);
        }
    }
}