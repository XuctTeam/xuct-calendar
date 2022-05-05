/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberEventListener
 * Author:   Derek Xu
 * Date:     2022/3/14 11:15
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.event;

import cn.com.xuct.calendar.common.module.feign.req.CalendarInitFeignInfo;
import cn.com.xuct.calendar.common.module.enums.MemberMessageTypeEnum;
import cn.com.xuct.calendar.ums.api.entity.MemberMessage;
import cn.com.xuct.calendar.ums.api.feign.CalendarFeignClient;
import cn.com.xuct.calendar.ums.boot.service.IMemberMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/14
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventListener {

    private final CalendarFeignClient calendarFeignClient;

    private final IMemberMessageService memberMessageService;

    @Async
    @EventListener
    public void listenerModifyEvent(MemberEvent nameEvent) {
        CalendarInitFeignInfo calendarInitFeignInfo = new CalendarInitFeignInfo();
        calendarInitFeignInfo.setMemberId(nameEvent.getMemberId());
        calendarInitFeignInfo.setMemberNickName(nameEvent.getName());
        calendarFeignClient.updateMemberCalendarName(calendarInitFeignInfo);
    }

    @Async
    @EventListener
    public void listenerRegisterEvent(MemberEvent event) {
        MemberMessage memberMessage = new MemberMessage();
        memberMessage.setMemberId(event.getMemberId());
        memberMessage.setType(MemberMessageTypeEnum.SYSTEM);
        memberMessage.setOperation(event.getType());
        memberMessage.setStatus(0);
        String title = "";
        switch (event.getType()) {
            case 0:
                title = event.getName() + "注册成功";
                break;
            case 1:
                title = event.getName() + "修改姓名";
                break;
            case 2:
                title = event.getName() + "使用微信头像昵称";
                break;
            case 3:
                title = event.getName() + "合并账号";
                break;
        }
        memberMessage.setTitle(title);
        JSONObject content = new JSONObject();
        content.put("user_name", event.getName());
        memberMessage.setContent(content);
        memberMessageService.save(memberMessage);
    }
}