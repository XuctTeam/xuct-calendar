/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: ComponentServiceImpl
 * Author:   Derek Xu
 * Date:     2021/12/21 16:44
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.service.impl;

import cn.com.xuct.calendar.cms.api.entity.Component;
import cn.com.xuct.calendar.cms.api.entity.ComponentAlarm;
import cn.com.xuct.calendar.cms.boot.config.RabbitmqConfiguration;
import cn.com.xuct.calendar.cms.boot.service.IComponentAlarmService;
import cn.com.xuct.calendar.cms.boot.service.IComponentService;
import cn.com.xuct.calendar.cms.boot.utils.DateHelper;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.enums.ComponentAlarmEnum;
import cn.com.xuct.calendar.common.module.enums.CommonStatusEnum;
import cn.com.xuct.calendar.cms.boot.mapper.ComponentMapper;
import cn.com.xuct.calendar.common.web.utils.JwtUtils;
import cn.com.xuct.calendar.service.base.BaseServiceImpl;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/21
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class ComponentServiceImpl extends BaseServiceImpl<ComponentMapper, Component> implements IComponentService {

    private final IComponentAlarmService componentAlarmService;

    private final RabbitmqConfiguration rabbitmqConfiguration;

    @Override
    public List<Component> query(Long calendarId, Long start, Long end) {
        QueryWrapper<Component> mapper = super.getQuery();
        mapper.eq("calendar_id", calendarId).
                and(wrapper -> wrapper.le("start_time", start).ge("end_time", start)
                        .or(lessWrapper -> lessWrapper.le("start_time", end).ge("end_time", end))
                        .or(normalWrapper -> normalWrapper.ge("start_time", start).le("end_time", end)));
        return super.getBaseMapper().selectList(mapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ComponentAlarm> addComponent(final Long memberId, final String timeZone, final Long calendarId, final Component component, final String alarmType, final List<Integer> alarmTimes) {
        List<ComponentAlarm> componentAlarmList = null;
        component.setAlarmType(ComponentAlarmEnum.getByCode(alarmType));
        if (alarmTimes.size() != 0) {
            component.setAlarmTimes(ArrayUtil.join(alarmTimes.toArray(new Integer[alarmTimes.size()]), ","));
        }
        this.save(component);
        if (!alarmType.equals(ComponentAlarmEnum.UNKNOWN.name()) && alarmTimes.size() != 0) {
            componentAlarmList = this.addAlarm(memberId, timeZone, calendarId, component, alarmTimes);
        }
        return componentAlarmList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ComponentAlarm> updateComponent(final Long memberId, final String timeZone, final Component component, final String alarmType, final List<Integer> alarmTimes, boolean change) {
        //有更新
        if (change) {
            return updateComponentAlarm(memberId, timeZone, component, alarmType, alarmTimes);
        }
        if (alarmType.equals(ComponentAlarmEnum.UNKNOWN.name())) {
            this.updateById(component);
            return Lists.newArrayList();
        }

        final List<Integer> triggers = Lists.newArrayList();
        if (StringUtils.hasText(component.getAlarmTimes())) {
            triggers.addAll(Arrays.asList(component.getAlarmTimes().split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList()));
        }
        List<Integer> addReduce = alarmTimes.stream().filter(item -> !triggers.contains(item)).collect(Collectors.toList());
        List<Integer> deleteReduce = triggers.stream().filter(item -> !alarmTimes.contains(item)).collect(Collectors.toList());
        if (addReduce.size() == 0 && deleteReduce.size() == 0) {
            this.updateById(component);
            return Lists.newArrayList();
        }
        if (alarmTimes.size() != 0) {
            component.setAlarmTimes(ArrayUtil.join(alarmTimes.toArray(new Integer[alarmTimes.size()]), ","));
        }
        List<ComponentAlarm> resultComponentAlarms = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(deleteReduce)) {
            List<ComponentAlarm> componentAlarmList = componentAlarmService.find(Lists.newArrayList(Column.of("component_id", component.getId()), Column.of("status", CommonStatusEnum.NORMAL)));
            if (!CollectionUtils.isEmpty(componentAlarmList)) {
                List<ComponentAlarm> deleteAlarmList = componentAlarmList.stream().filter(x -> deleteReduce.contains(x.getTriggerSec())).map(x -> {
                    x.setStatus(CommonStatusEnum.DELETED);
                    return x;
                }).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(deleteAlarmList)) {
                    componentAlarmService.saveOrUpdateBatch(deleteAlarmList);
                }
            }
        }
        if (!CollectionUtils.isEmpty(addReduce)) {
            resultComponentAlarms.addAll(this.addAlarm(memberId, timeZone, component.getCalendarId(), component, addReduce));
        }
        return resultComponentAlarms;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(final Long componentId) {
        componentAlarmService.delete(Column.of("component_id", componentId));
        super.removeById(componentId);
    }

    /**
     * 功能描述: <br>
     * 〈更新日程的提醒〉
     *
     * @param memberId
     * @param component
     * @param alarmType
     * @param alarmTimes
     * @return:java.util.List<cn.com.xuct.calendar.dao.entity.ComponentAlarm>
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/1/17 13:52
     */
    private List<ComponentAlarm> updateComponentAlarm(final Long memberId, final String timeZone, final Component component, final String alarmType, final List<Integer> alarmTimes) {
        ComponentAlarm updateAlarm = new ComponentAlarm();
        updateAlarm.setStatus(CommonStatusEnum.DELETED);
        componentAlarmService.update(updateAlarm, Column.of("component_id", component.getId()));
        //更新提醒时间
        List<ComponentAlarm> componentAlarmList = null;
        component.setAlarmType(ComponentAlarmEnum.getByCode(alarmType));
        if (alarmTimes.size() != 0) {
            component.setAlarmTimes(ArrayUtil.join(alarmTimes.toArray(new Integer[alarmTimes.size()]), ","));
        }
        if (!alarmType.equals(ComponentAlarmEnum.UNKNOWN.name()) && alarmTimes.size() != 0) {
            componentAlarmList = this.addAlarm(memberId, timeZone, component.getCalendarId(), component, alarmTimes);
        }
        this.updateById(component);
        return CollectionUtils.isEmpty(componentAlarmList) ? Lists.newArrayList() : componentAlarmList;
    }

    /**
     * 功能描述: <br>
     * 〈封装日程提醒〉
     *
     * @param memberId
     * @param timeZone
     * @param calendarId
     * @param component
     * @param alarmTimes
     * @return:java.util.List<cn.com.xuct.calendar.cms.api.entity.ComponentAlarm>
     * @since: 1.0.0
     * @Author:
     * @Date: 2022/1/20 22:40
     */
    private List<ComponentAlarm> addAlarm(final Long memberId, final String timeZone, final Long calendarId, Component component, final List<Integer> alarmTimes) {
        /* 不循环日程 */
        List<ComponentAlarm> componentAlarmList = null;
        if ("0".equals(component.getRepeatStatus())) {
            componentAlarmList = noRepeatAlarm(memberId, calendarId, component, alarmTimes);
        } else {
            componentAlarmList = repeatAlarm(memberId, timeZone, calendarId, component, alarmTimes);
        }
        if (!CollectionUtils.isEmpty(componentAlarmList)) {
            componentAlarmList.stream().forEach(alarm -> {
                componentAlarmService.save(alarm);
            });
        }
        return componentAlarmList;
    }

    /**
     * 功能描述: <br>
     * 〈日程不循环提醒〉
     *
     * @param memberId
     * @param calendarId
     * @param component
     * @param alarmTimes
     * @return:java.util.List<cn.com.xuct.calendar.cms.api.entity.ComponentAlarm>
     * @since: 1.0.0
     * @Author:
     * @Date: 2022/1/20 20:32
     */
    private List<ComponentAlarm> noRepeatAlarm(final Long memberId, final Long calendarId, Component component, List<Integer> alarmTimes) {
        List<ComponentAlarm> alarmDatas = Lists.newArrayList();
        DateTime now = DateUtil.date();
        ComponentAlarm componentAlarm = null;
        long diffTime = component.getDtstart().getTime() - now.getTime();
        for (int i = 0, j = alarmTimes.size(); i < j; i++) {
            Integer trigger = alarmTimes.get(i);
            if (diffTime - trigger < 0) continue;
            componentAlarm = this.getDefaultAlarm(memberId, calendarId, component.getId());
            componentAlarm.setAlarmTime(DateUtil.date(diffTime - trigger));
            componentAlarm.setTriggerSec(trigger);
            if (diffTime - trigger > rabbitmqConfiguration.getMaxDelay()) {
                componentAlarm.setDelayTime(rabbitmqConfiguration.getMaxDelay());
            } else {
                componentAlarm.setDelayTime(diffTime - trigger);
            }
            alarmDatas.add(componentAlarm);
        }
        return alarmDatas;
    }

    /**
     * 功能描述: <br>
     * 〈日程循环提醒〉
     *
     * @param memberId
     * @param calendarId
     * @param component
     * @param alarmTimes
     * @return:java.util.List<cn.com.xuct.calendar.cms.api.entity.ComponentAlarm>
     * @since: 1.0.0
     * @Author:
     * @Date: 2022/1/20 20:34
     */
    private List<ComponentAlarm> repeatAlarm(final Long memberId, final String timeZone, final Long calendarId, Component component, List<Integer> alarmTimes) {
        List<DateTime> dateTimes = DateHelper.getRepeatRangeDataList(timeZone, component);
        List<ComponentAlarm> alarmDatas = Lists.newArrayList();
        if (CollectionUtils.isEmpty(dateTimes)) return alarmDatas;
        final DateTime now = DateUtil.date();
        DateTime repeatDate = null;
        ComponentAlarm componentAlarm = null;
        for (int i = 0, j = alarmTimes.size(); i < j; i++) {
            final Integer trigger = alarmTimes.get(i);
            componentAlarm = this.getDefaultAlarm(memberId, calendarId, component.getId());
            componentAlarm.setTriggerSec(trigger);
            for (int m = 0, n = dateTimes.size(); m < n; m++) {
                repeatDate = dateTimes.get(m);
                if (repeatDate.getTime() - now.getTime() - trigger < 0) continue;
                componentAlarm.setAlarmTime(DateUtil.date(repeatDate.getTime() - now.getTime() - trigger));
                if (repeatDate.getTime() - now.getTime() - trigger > rabbitmqConfiguration.getMaxDelay()) {
                    componentAlarm.setDelayTime(rabbitmqConfiguration.getMaxDelay());
                    alarmDatas.add(componentAlarm);
                    break;
                }
                alarmDatas.add(componentAlarm);
                componentAlarm.setDelayTime(repeatDate.getTime() - now.getTime() - trigger);
            }
        }
        return alarmDatas;
    }


    /**
     * 功能描述: <br>
     * 〈封装提醒〉
     *
     * @param memberId
     * @param calendarId
     * @param componentId
     * @return:cn.com.xuct.calendar.dao.entity.ComponentAlarm
     * @since: 1.0.0
     * @Author:
     * @Date: 2022/1/20 15:54
     */
    private ComponentAlarm getDefaultAlarm(final Long memberId, final Long calendarId, final Long componentId) {
        ComponentAlarm componentAlarm = new ComponentAlarm();
        componentAlarm.setCreateMemberId(memberId);
        componentAlarm.setCalendarId(calendarId);
        componentAlarm.setComponentId(componentId);
        componentAlarm.setStatus(CommonStatusEnum.NORMAL);
        return componentAlarm;
    }

}