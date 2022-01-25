/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ComponentEventListener
 * Author:   Administrator
 * Date:     2022/1/25 22:12
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.handler;

import cn.com.xuct.calendar.cms.boot.service.IComponentAlarmService;
import cn.com.xuct.calendar.cms.boot.service.IComponentService;
import cn.com.xuct.calendar.cms.queue.event.AlarmEvent;
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
 * @create 2022/1/25
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ComponentEventListener {

    private final IComponentService componentService;

    private final IComponentAlarmService componentAlarmService;

    @Async
    @EventListener(classes = AlarmEvent.class)
    public void onEvent(AlarmEvent alarmEvent) {
        log.info("alarm event....");
        //cn.com.xuct.calendar.cms.api.entity.Component = componentService.getById(alarmEvent.getMessage())
    }
}