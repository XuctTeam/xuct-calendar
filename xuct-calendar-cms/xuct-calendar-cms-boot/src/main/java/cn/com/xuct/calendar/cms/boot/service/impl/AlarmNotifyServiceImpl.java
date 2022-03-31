/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: AlarmNotifyServiceImpl
 * Author:   Derek Xu
 * Date:     2022/3/29 15:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.service.impl;

import cn.com.xuct.calendar.cms.api.entity.Component;
import cn.com.xuct.calendar.cms.api.entity.ComponentAlarm;
import cn.com.xuct.calendar.cms.api.entity.ComponentAttend;
import cn.com.xuct.calendar.cms.api.feign.UmsFeignClient;
import cn.com.xuct.calendar.cms.boot.config.RabbitmqConfiguration;
import cn.com.xuct.calendar.cms.boot.handler.RabbitmqOutChannel;
import cn.com.xuct.calendar.cms.boot.service.IAlarmNotifyService;
import cn.com.xuct.calendar.cms.boot.service.IComponentAlarmService;
import cn.com.xuct.calendar.cms.boot.service.IComponentAttendService;
import cn.com.xuct.calendar.cms.boot.utils.DateHelper;
import cn.com.xuct.calendar.common.core.constant.RabbitmqConstants;
import cn.com.xuct.calendar.common.core.utils.JsonUtils;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.dto.AlarmInfoDto;
import cn.com.xuct.calendar.common.module.enums.ComponentAlarmEnum;
import cn.com.xuct.calendar.common.module.enums.ComponentRepeatTypeEnum;
import cn.com.xuct.calendar.common.module.feign.AlarmNotifyFeignInfoReq;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.format.DateParser;
import cn.hutool.core.date.format.DatePrinter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/29
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmNotifyServiceImpl implements IAlarmNotifyService {


    private final IComponentAttendService componentAttendService;

    private final IComponentAlarmService componentAlarmService;

    private final RabbitmqConfiguration rabbitmqConfiguration;

    private final UmsFeignClient umsFeignClient;

    private final RabbitmqOutChannel rabbitmqOutChannel;

    @Async("taskExecutor")
    @Override
    public void timerOverAlarmNotify(Component component) {
        List<ComponentAttend> componentAttendList = componentAttendService.find(Column.of("component_id", component.getId()));
        if (CollectionUtils.isEmpty(componentAttendList)) {
            log.error("alarm notify service::component attend list empty , calendar id = {} , component id = {}", component.getCalendarId(), component.getId());
            return;
        }
        AlarmNotifyFeignInfoReq alarmNotifyFeignInfoReq = AlarmNotifyFeignInfoReq.builder()
                .componentId(component.getId())
                .summary(component.getSummary())
                .startDate(DateUtil.format(new Date(component.getStartTime()), DatePattern.NORM_DATETIME_FORMATTER))
                .createMemberId(component.getCreatorMemberId())
                .type(Integer.parseInt(component.getAlarmType().getCode()))
                .ids(componentAttendList.stream().map(ComponentAttend::getMemberId).collect(Collectors.toList()))
                .build();
        umsFeignClient.notifyAlarm(alarmNotifyFeignInfoReq);
    }

    @Async("taskExecutor")
    @Override
    public void noRepeatAlarmPushToQueue(Component component, Long alarmId, Integer triggerSec) {
        long diffTime = component.getDtstart().getTime() - DateUtil.date().getTime() - triggerSec;
        if (diffTime < 0) {
            log.error("alarm notify service:: next time less 0 , calendar id = {} , component id = {}", component.getCalendarId(), component.getId());
            return;
        }
        Long nextDelayTime = diffTime;
        if (diffTime > rabbitmqConfiguration.getMaxDelay()) {
            nextDelayTime = rabbitmqConfiguration.getMaxDelay();
        }
        rabbitmqOutChannel.pushAlarmDelayedMessage(
                RabbitmqConstants.COMPONENT_ALARM_TYPE,
                JsonUtils.obj2json(AlarmInfoDto.builder().componentId(String.valueOf(component.getId()))
                        .alarmId(String.valueOf(alarmId)).build()), nextDelayTime);
    }

    @Override
    public void repeatAlarmPushToQueue(Component component, ComponentAlarm alarm) {
        /* TODO 1.发送本次循环参数消息*/
        /* 2.增加下次循环时间 */
        Integer triggerSec = alarm.getTriggerSec();
        List<DateTime> dateTimes = DateHelper.getRepeatRangeDataList(component.getTimeZone(), component);
        if (CollectionUtils.isEmpty(dateTimes)) {
            log.error("alarm notify service:: repeat component date list empty , calendar id = {} , component id = {}", component.getCalendarId(), component.getId());
            return;
        }
        List<DateTime> nextDelayTimes = dateTimes.stream().filter(x -> new Date(x.getTime() + triggerSec).after(new Date(component.getStartTime()))).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(nextDelayTimes)) {
            log.error("alarm notify service:: repeat component date list empty , calendar id = {} , component id = {}", component.getCalendarId(), component.getId());
            return;
        }
        Long nextDelayTime = nextDelayTimes.get(0).getTime() - triggerSec;
        if (nextDelayTime > rabbitmqConfiguration.getMaxDelay()) {
            nextDelayTime = rabbitmqConfiguration.getMaxDelay();
        }
        alarm.setAlarmTime(new Date(nextDelayTime));
        /* 更新提醒时间 */
        componentAlarmService.updateById(alarm);
        rabbitmqOutChannel.pushAlarmDelayedMessage(
                RabbitmqConstants.COMPONENT_ALARM_TYPE,
                JsonUtils.obj2json(AlarmInfoDto.builder().componentId(String.valueOf(component.getId()))
                        .alarmId(String.valueOf(alarm.getId())).build()), nextDelayTime);
    }
}