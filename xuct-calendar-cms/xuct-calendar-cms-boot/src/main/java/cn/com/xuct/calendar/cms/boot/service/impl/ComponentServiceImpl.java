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

import cn.com.xuct.calendar.cms.api.dodo.MemberMarjoCalendarDo;
import cn.com.xuct.calendar.cms.api.entity.Component;
import cn.com.xuct.calendar.cms.api.entity.ComponentAlarm;
import cn.com.xuct.calendar.cms.api.entity.ComponentAttend;
import cn.com.xuct.calendar.cms.boot.config.RabbitmqConfiguration;
import cn.com.xuct.calendar.cms.boot.mapper.ComponentMapper;
import cn.com.xuct.calendar.cms.boot.service.IComponentAlarmService;
import cn.com.xuct.calendar.cms.boot.service.IComponentAttendService;
import cn.com.xuct.calendar.cms.boot.service.IComponentService;
import cn.com.xuct.calendar.cms.boot.service.IMemberCalendarService;
import cn.com.xuct.calendar.cms.boot.utils.DateHelper;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.db.service.BaseServiceImpl;
import cn.com.xuct.calendar.common.module.enums.CommonStatusEnum;
import cn.com.xuct.calendar.common.module.enums.ComponentAlarmEnum;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
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

    private final IMemberCalendarService calendarService;

    private final IComponentAttendService componentAttendService;

    private final IComponentAlarmService componentAlarmService;

    private final RabbitmqConfiguration rabbitmqConfiguration;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ComponentAlarm> addComponent(final Long memberId, final String timeZone, final Long calendarId, final Component component, final List<String> memberIds, final String alarmType, final List<Integer> alarmTimes) {
        component.setAlarmType(ComponentAlarmEnum.getByCode(alarmType));
        component.setTimeZone(timeZone);
        if (alarmTimes.size() != 0) {
            component.setAlarmTimes(ArrayUtil.join(alarmTimes.toArray(new Integer[alarmTimes.size()]), ","));
        }
        /* 全天事件处理 */
        if (component.getFullDay() == 1) {
            Date date = component.getDtstart();
            component.setDtstart(DateUtil.beginOfDay(date));
            component.setDtend(DateUtil.endOfDay(date).offset(DateField.MILLISECOND,-999));
        }
        this.save(component);
        /* 增加邀请人数据 */
        this.addComponentAttends(memberId, calendarId, component, memberIds, false);
        /* 增加提醒数据 */
        if (alarmType.equals(ComponentAlarmEnum.UNKNOWN.getCode())) return Lists.newArrayList();
        return this.addAlarm(memberId, timeZone, calendarId, component, alarmTimes);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ComponentAlarm> updateComponent(final Long oldCalendarId, final Long memberId, final String timeZone, final Component component, final List<String> memberIds, final String alarmType, final List<Integer> alarmTimes, boolean change) {
        /* 更新邀请人信息 */
        this.updateComponentAttend(oldCalendarId, memberId, component.getCalendarId(), component, memberIds);
        //有更新
        if (change) {
            return updateComponentAlarm(memberId, timeZone, component, alarmType, alarmTimes);
        }
        if (alarmType.equals(ComponentAlarmEnum.UNKNOWN.getCode())) {
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
    public List<Long> deleteByComponentId(final Long memberId, final Long componentId) {
        List<Long> memberIds = Lists.newArrayList();
        /* 1.删除提醒信息 */
        componentAlarmService.delete(Column.of("component_id", componentId));
        /* 2.删除邀请信息 */
        List<ComponentAttend> componentAttends = componentAttendService.find(Lists.newArrayList(Column.of("component_id", componentId)));
        if (!CollectionUtils.isEmpty(componentAttends)) {
            memberIds = componentAttends.stream().map(ComponentAttend::getMemberId).collect(Collectors.toList());
            componentAttendService.removeBatchByIds(componentAttends.stream().map(ComponentAttend::getId).collect(Collectors.toList()));
        }
        super.removeById(componentId);
        return memberIds;
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
        component.setAlarmType(ComponentAlarmEnum.getByCode(alarmType));
        if (alarmTimes.size() != 0) {
            component.setAlarmTimes(ArrayUtil.join(alarmTimes.toArray(new Integer[alarmTimes.size()]), ","));
        }
        this.updateById(component);
        if (alarmType.equals(ComponentAlarmEnum.UNKNOWN.getCode())) return Lists.newArrayList();
        return this.addAlarm(memberId, timeZone, component.getCalendarId(), component, alarmTimes);
    }

    /**
     * 功能描述: <br>
     * 〈更新邀请人〉
     *
     * @param
     * @return:void
     * @since: 1.0.0
     * @Author:
     * @Date: 2022/3/13 21:00
     */
    private void updateComponentAttend(final Long oldCalendarId, Long memberId, Long calendarId, Component component, List<String> memberIds) {
        List<Long> attends = componentAttendService.listByComponentIdNoMemberId(memberId, component.getId());
        /* 1. 更新自己邀请事件表 */
        boolean isNewCalendar = !String.valueOf(oldCalendarId).equals(String.valueOf(calendarId));
        if (isNewCalendar) {
            componentAttendService.updateMemberAttendCalendarId(memberId, oldCalendarId, calendarId, component.getId());
        }
        if (CollectionUtils.isEmpty(attends)) {
            if (CollectionUtils.isEmpty(attends)) return;
            this.addComponentAttends(memberId, calendarId, component, memberIds, true);
            return;
        }
        List<String> addReduce = memberIds.stream().filter(item -> !attends.contains(Long.valueOf(item))).collect(Collectors.toList());
        List<Long> deleteReduce = attends.stream().filter(item -> !memberIds.contains(String.valueOf(item))).collect(Collectors.toList());
        /* TODO 增加邀请消息 */
        if (!CollectionUtils.isEmpty(addReduce)) {
            this.addComponentAttends(memberId, calendarId, component, addReduce, true);
        }
        if (!CollectionUtils.isEmpty(deleteReduce)) {
            componentAttendService.remove(componentAttendService.getQuery().lambda().eq(ComponentAttend::getComponentId, component.getId()).in(ComponentAttend::getMemberId, deleteReduce));
            /* TODO 增加删除消息 */
        }
        /* 更新参会人的邀请日历 */
        if (isNewCalendar) {
            List<Long> updateReduce = attends.stream().filter(item -> memberIds.contains(String.valueOf(item))).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(updateReduce)) return;
            componentAttendService.batchUpdateAttendMemberCalendarId(component.getId(), calendarId, updateReduce);
        }
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
        if (CollectionUtils.isEmpty(alarmTimes)) return Lists.newArrayList();
        /* 不循环日程 */
        List<ComponentAlarm> componentAlarmList = null;
        if ("0".equals(component.getRepeatStatus())) {
            componentAlarmList = noRepeatAlarm(memberId, calendarId, component, alarmTimes);
        } else {
            componentAlarmList = repeatAlarm(memberId, timeZone, calendarId, component, alarmTimes);
        }
        componentAlarmService.saveBatch(componentAlarmList);
        return componentAlarmList;
    }

    /**
     * 功能描述: <br>
     * 〈添加邀请参与人〉
     *
     * @param memberId
     * @param calendarId
     * @param component
     * @param memberIds
     * @param up         是否是更新，更新则不更新自己的事件
     * @return:void
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/3/29 13:03
     */
    private void addComponentAttends(Long memberId, Long calendarId, Component component, List<String> memberIds, boolean up) {
        List<ComponentAttend> componentAttends = Lists.newArrayList();
        ComponentAttend componentAttend = null;
        MemberMarjoCalendarDo calendarDo = null;
        if (!CollectionUtils.isEmpty(memberIds)) {
            List<MemberMarjoCalendarDo> memberMarjoCalendarDos = calendarService.queryMarjoCalendarIds(memberIds);
            for (int i = 0, j = memberMarjoCalendarDos.size(); i < j; i++) {
                calendarDo = memberMarjoCalendarDos.get(i);
                componentAttend = new ComponentAttend();
                componentAttend.setComponentId(component.getId());
                componentAttend.setCalendarId(calendarId);
                componentAttend.setAttendCalendarId(calendarDo.getCalendarId());
                componentAttend.setMemberId(calendarDo.getMemberId());
                componentAttend.setStatus(0);
                componentAttends.add(componentAttend);
            }
        }
        if (!up) {
            componentAttend = new ComponentAttend();
            componentAttend.setComponentId(component.getId());
            componentAttend.setCalendarId(calendarId);
            componentAttend.setAttendCalendarId(calendarId);
            componentAttend.setMemberId(memberId);
            componentAttend.setStatus(1);
            componentAttends.add(componentAttend);
        }
        componentAttendService.saveBatch(componentAttends);
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
        Long diffTime = component.getDtstart().getTime() - now.getTime();
        for (int i = 0, j = alarmTimes.size(); i < j; i++) {
            Long trigger = alarmTimes.get(i) * 60 * 1000L;
            Long diff = diffTime - trigger;
            if (diff < 0) continue;
            componentAlarm = this.getDefaultAlarm(memberId, calendarId, component.getId());
            componentAlarm.setTriggerSec(alarmTimes.get(i));
            componentAlarm.setAlarmTime(DateUtil.offsetMillisecond(now, Math.toIntExact(diff)));
            componentAlarm.setDelayTime(NumberUtil.compare(diff, rabbitmqConfiguration.getMaxDelay()) == -1 ? diff : rabbitmqConfiguration.getMaxDelay());
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
        List<DateTime> dateTimes = DateHelper.getRepeatRangeDataList(component, timeZone);
        List<ComponentAlarm> alarmDatas = Lists.newArrayList();
        if (CollectionUtils.isEmpty(dateTimes)) return alarmDatas;
        final DateTime now = DateUtil.date();
        DateTime repeatDate = null;
        ComponentAlarm componentAlarm = null;
        for (int i = 0, j = alarmTimes.size(); i < j; i++) {
            Long trigger = alarmTimes.get(i) * 60 * 1000L;
            componentAlarm = this.getDefaultAlarm(memberId, calendarId, component.getId());
            componentAlarm.setTriggerSec(alarmTimes.get(i));
            for (int m = 0, n = dateTimes.size(); m < n; m++) {
                repeatDate = dateTimes.get(m);
                long nextTime = repeatDate.getTime() - now.getTime() - trigger;
                if (nextTime < 0) continue;
                componentAlarm.setAlarmTime(DateUtil.offsetMillisecond(repeatDate, -Math.toIntExact(trigger)));
                componentAlarm.setDelayTime(NumberUtil.compare(nextTime, rabbitmqConfiguration.getMaxDelay()) == -1 ? nextTime : rabbitmqConfiguration.getMaxDelay());
                alarmDatas.add(componentAlarm);
                break;
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