/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ComponentEventListener
 * Author:   Derek Xu
 * Date:     2022/3/15 10:09
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.handler;

import cn.com.xuct.calendar.cms.api.entity.Component;
import cn.com.xuct.calendar.cms.api.entity.ComponentAlarm;
import cn.com.xuct.calendar.cms.boot.config.RabbitmqConfiguration;
import cn.com.xuct.calendar.cms.boot.service.IAlarmNotifyService;
import cn.com.xuct.calendar.cms.boot.service.IComponentAlarmService;
import cn.com.xuct.calendar.cms.boot.service.IComponentService;
import cn.com.xuct.calendar.cms.queue.event.AlarmEvent;
import cn.com.xuct.calendar.cms.queue.event.ComponentDelEvent;
import cn.com.xuct.calendar.common.core.utils.JsonUtils;
import cn.com.xuct.calendar.common.module.dto.AlarmInfoDto;
import cn.com.xuct.calendar.common.module.enums.CommonStatusEnum;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/15
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ComponentEventListener {

    private final IComponentService componentService;

    private final IComponentAlarmService componentAlarmService;

    private final IAlarmNotifyService alarmNotifyService;

    private final RabbitmqConfiguration rabbitmqConfiguration;

    /**
     * 功能描述: <br>
     * 〈事件被删除〉
     *
     * @param delEvent
     * @return:void
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/3/15 10:21
     */
    @Async
    @EventListener(classes = ComponentDelEvent.class)
    public void listenerComponentDelEvent(ComponentDelEvent delEvent) {

    }

    /**
     * 功能描述: <br>
     * 〈提醒事件〉
     *
     * @param alarmEvent
     * @return:void
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/3/15 10:30
     */
    @Async
    @EventListener(classes = AlarmEvent.class)
    public void onEvent(AlarmEvent alarmEvent) {
        log.info("alarm event.... , message = {}", alarmEvent.getMessage());
        if (!StringUtils.hasLength(alarmEvent.getMessage())) {
            log.error("alarm event listener:: message is error");
            return;
        }
        AlarmInfoDto alarmInfoDto = JsonUtils.json2pojo(alarmEvent.getMessage(), AlarmInfoDto.class);
        if (alarmInfoDto == null) {
            log.error("alarm event listener:: message to bean error");
            return;
        }
        ComponentAlarm alarm = componentAlarmService.getById(alarmInfoDto.getAlarmId());
        if (alarm == null || alarm.getStatus().equals(CommonStatusEnum.DELETED)) {
            log.error("alarm event listener:: alarm is not exist or component had update , component id = {} , alarm id = {}", alarmInfoDto.getComponentId(), alarmInfoDto.getAlarmId());
            return;
        }
        DateTime now = DateUtil.date();
        Component component = componentService.getById(alarmInfoDto.getComponentId());
        if (component == null || component.getStatus().equals(CommonStatusEnum.DELETED) || new Date(component.getStartTime()).before(now)) {
            log.error("alarm event listener:: component is null , component id = {} , alarm id = {}", alarmInfoDto.getComponentId(), alarmInfoDto.getAlarmId());
            return;
        }
        if (!component.getAlarmTimes().contains(alarm.getTriggerSec().toString())) {
            log.error("alarm event listener:: alarm times is not in component alarm times , component id = {} , alarm id = {}", alarmInfoDto.getComponentId(), alarmInfoDto.getAlarmId());
            return;
        }
        /* 1. 到最后提醒时间 */
        if (alarm.getAlarmTime().before(new Date(now.getTime() + alarm.getTriggerSec()))) {
            alarmNotifyService.timerOverAlarmNotify(component);
            return;
        }
        /* 2. 不循环事件*/
        if ("0".equals(component.getRepeatStatus())) {
            alarmNotifyService.noRepeatAlarmPushToQueue(component, alarm.getId(), alarm.getTriggerSec());
            return;
        }
        /* 3. 循环事件处理 */
        alarmNotifyService.repeatAlarmPushToQueue(component, alarm);
    }
}