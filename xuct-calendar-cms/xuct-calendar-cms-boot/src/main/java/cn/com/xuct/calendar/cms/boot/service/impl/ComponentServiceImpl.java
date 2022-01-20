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
import cn.com.xuct.calendar.cms.boot.service.IComponentAlarmService;
import cn.com.xuct.calendar.cms.boot.service.IComponentService;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.enums.ComponentAlarmEnum;
import cn.com.xuct.calendar.common.module.enums.CommonStatusEnum;
import cn.com.xuct.calendar.cms.boot.mapper.ComponentMapper;
import cn.com.xuct.calendar.service.base.BaseServiceImpl;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    public List<ComponentAlarm> addComponent(final Long memberId, final Long calendarId, final Component component, final String alarmType, final List<Integer> alarmTimes, Long maxDelay) {
        List<ComponentAlarm> componentAlarmList = null;
        component.setAlarmType(ComponentAlarmEnum.getByCode(alarmType));
        this.save(component);
        if (!alarmType.equals(ComponentAlarmEnum.UNKNOWN.name()) && alarmTimes.size() != 0) {
            componentAlarmList = this.addAlarm(memberId, calendarId, component.getId(), component.getDtstart(), alarmTimes);
        }
        if (!CollectionUtils.isEmpty(componentAlarmList)) {
            component.setAlarmTime(DateUtil.date());
            super.updateById(component);
            return componentAlarmList;
        }
        return Lists.newArrayList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ComponentAlarm> updateComponent(final Long memberId, final Component component, final String alarmType, final List<Integer> alarmTimes, boolean change, Long maxDelay) {
        //有更新
        if (change) {
            return updateComponentAlarm(memberId, component, alarmType, alarmTimes);
        }
        if (alarmType.equals(ComponentAlarmEnum.UNKNOWN.name())) {
            this.updateById(component);
            return Lists.newArrayList();
        }
        List<ComponentAlarm> componentAlarmList = componentAlarmService.find(Lists.newArrayList(
                Column.of("component_id", component.getId()), Column.of("status", CommonStatusEnum.NORMAL)));
        List<Integer> triggers = componentAlarmList.stream().map(p -> p.getTriggerSec()).collect(Collectors.toList());
        List<Integer> addReduce = alarmTimes.stream().filter(item -> !triggers.contains(item)).collect(Collectors.toList());
        List<Integer> deleteReduce = triggers.stream().filter(item -> !alarmTimes.contains(item)).collect(Collectors.toList());
        if (addReduce.size() == 0 && deleteReduce.size() == 0) {
            this.updateById(component);
            return Lists.newArrayList();
        }
        return updateComponentAlarm(memberId, component, alarmType, alarmTimes);
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
    private List<ComponentAlarm> updateComponentAlarm(final Long memberId, final Component component, final String alarmType, final List<Integer> alarmTimes) {

        ComponentAlarm updateAlarm = new ComponentAlarm();
        updateAlarm.setStatus(CommonStatusEnum.DELETED);
        componentAlarmService.update(updateAlarm, Column.of("component_id", component.getId()));
        //更新提醒时间
        List<ComponentAlarm> componentAlarmList = null;
        component.setAlarmType(ComponentAlarmEnum.getByCode(alarmType));
        if (!alarmType.equals(ComponentAlarmEnum.UNKNOWN.name()) && alarmTimes.size() != 0) {
            component.setAlarmTime(DateUtil.date());
            componentAlarmList = this.addAlarm(memberId, component.getCalendarId(), component.getId(), component.getDtstart(), alarmTimes);
        }
        this.updateById(component);
        return CollectionUtils.isEmpty(componentAlarmList) ? Lists.newArrayList() : componentAlarmList;
    }

    private List<ComponentAlarm> addAlarm(final Long memberId, final Long calendarId, Component component, String alarmType, List<Integer> alarmTimes, Long maxDelay) {
        List<ComponentAlarm> alarmDates = Lists.newArrayList();
        ComponentAlarm componentAlarm = null;
        DateTime now = DateUtil.date();
        /* 不循环日程 */
        if ("0".equals(component.getRepeatStatus())) {
            long diffTime = component.getDtstart().getTime() - now.getTime();
            for (int i = 0, j = alarmTimes.size(); i < j; i++) {
                Integer trigger = alarmTimes.get(i);
                if (diffTime - trigger < 0) continue;
                componentAlarm = this.getDefaultAlarm(memberId, calendarId, component.getId());
                componentAlarm.setAlarmTime(DateUtil.date(diffTime - trigger));
                componentAlarm.setTriggerSec(trigger);
                if (diffTime - trigger > maxDelay) {
                    componentAlarm.setDelayTime(maxDelay);
                } else {
                    componentAlarm.setDelayTime(diffTime - trigger);
                }
                alarmDates.add(componentAlarm);
            }
        } else {  /* 循环日程 */

        }
        return componentAlarmList;
    }

    /**
     * 功能描述: <br>
     * 〈〉
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