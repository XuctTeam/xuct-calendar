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

import cn.com.xuct.calendar.common.module.dto.CalendarInitDto;
import cn.com.xuct.calendar.ums.api.feign.CalendarFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Async
    @EventListener
    public void listenerModifyEvent(MemberModifyNameEvent nameEvent) {
        CalendarInitDto calendarInitDto = new CalendarInitDto();
        calendarInitDto.setMemberId(nameEvent.getMemberId());
        calendarInitDto.setMemberNickName(nameEvent.getName());
        calendarFeignClient.updateMemberCalendarName(calendarInitDto);
    }
}