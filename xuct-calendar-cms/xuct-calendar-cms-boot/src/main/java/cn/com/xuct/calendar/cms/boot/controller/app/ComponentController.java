/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: ComponentAppController
 * Author:   Derek Xu
 * Date:     2021/12/21 17:30
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.controller.app;

import cn.com.xuct.calendar.cms.boot.handler.RabbitmqOutChannel;
import cn.com.xuct.calendar.cms.boot.utils.DateHelper;
import cn.com.xuct.calendar.cms.boot.vo.ComponentListVo;

import cn.com.xuct.calendar.cms.boot.vo.ComponentVo;
import cn.com.xuct.calendar.common.core.constant.DateConstants;
import cn.com.xuct.calendar.common.core.constant.RabbitmqConstants;
import cn.com.xuct.calendar.common.core.exception.SvrException;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.res.SvrResCode;
import cn.com.xuct.calendar.common.core.utils.JsonUtils;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.dto.AlarmInfoDto;
import cn.com.xuct.calendar.common.module.enums.ComponentRepeatTypeEnum;
import cn.com.xuct.calendar.common.module.enums.CommonStatusEnum;
import cn.com.xuct.calendar.common.module.params.ComponentAddParam;
import cn.com.xuct.calendar.common.web.utils.JwtUtils;
import cn.com.xuct.calendar.dao.entity.*;
import cn.com.xuct.calendar.service.IComponentService;
import cn.com.xuct.calendar.service.IMemberCalendarService;
import cn.com.xuct.calendar.service.IMemberService;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/21
 * @since 1.0.0
 */
@Slf4j
@RestController
@Api(tags = "【移动端】日程服务")
@RequestMapping("/api/app/v1/component")
@RequiredArgsConstructor
public class ComponentController {

    private final IMemberService memberService;

    private final IComponentService componentService;

    private final IMemberCalendarService memberCalendarService;

    private final RabbitmqOutChannel rabbitmqOutChannel;

    @ApiOperation(value = "按天查询日历下日程")
    @GetMapping("/list/calendar/days")
    public R<List<ComponentListVo>> listByCalendarId(@RequestParam("calendarId") String calendarId, @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
                                                     @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end) {

        Member member = memberService.getById(JwtUtils.getUserId());
        if (member == null) return R.data(Lists.newArrayList());
        List<Component> componentList = componentService.query(Long.valueOf(calendarId), start.getTime(), end.getTime());
        if (CollectionUtils.isEmpty(componentList)) return R.data(Lists.newArrayList());
        LinkedHashMap<String, List<Component>> componentListMap = Maps.newLinkedHashMap();
        final List<DateTime> dayRanges = Lists.newArrayList();

        componentList.stream().forEach(component -> {
            dayRanges.clear();
            if ("0".equals(component.getRepeatStatus())) {
                dayRanges.addAll(DateHelper.getRangeDateList(component.getDtstart(), component.getDtend()));
            } else {
                dayRanges.addAll(DateHelper.getRepeatRangeDataList(member.getTimeZone(), component));
            }
            if (dayRanges.size() == 0) return;
            for (int i = 0; i < dayRanges.size(); i++) {
                final String formatDay = DateUtil.format(dayRanges.get(i), DateConstants.PATTERN_DATE);
                if (!componentListMap.containsKey(formatDay)) {
                    componentListMap.put(formatDay, Lists.newArrayList());
                }
                componentListMap.get(formatDay).add(component);
            }
        });
        List<ComponentListVo> componentListVos = Lists.newArrayList();
        ComponentListVo componentListVo = null;
        for (String day : componentListMap.keySet()) {
            componentListVo = new ComponentListVo();
            componentListVo.setCalendarId(calendarId);
            componentListVo.setDay(day);
            componentListVo.setComponents(componentListMap.get(day));
            componentListVos.add(componentListVo);
        }
        return R.data(componentListVos);
    }

    @ApiOperation(value = "按天查询日程")
    @GetMapping("/days/{id}")
    public R<List<ComponentListVo>> getComponentDaysById(@PathVariable("id") String id) {
        Component component = componentService.getById(id);
        if (component == null) throw new SvrException(SvrResCode.CMS_COMPONENT_NOT_FOUND);
        Member member = memberService.getById(JwtUtils.getUserId());
        if (member == null) return R.data(Lists.newArrayList());
        final List<DateTime> dayRanges = "0".equals(component.getRepeatStatus()) ? DateHelper.getRangeDateList(component.getDtstart(), component.getDtend()) :
                DateHelper.getRepeatRangeDataList(member.getTimeZone(), component);
        if (CollectionUtils.isEmpty(dayRanges)) throw new SvrException(SvrResCode.CMS_COMPONENT_DAY_LIST_EMPTY);
        List<ComponentListVo> componentListVos = Lists.newArrayList();
        ComponentListVo componentListVo = null;
        for (int i = 0, j = dayRanges.size(); i < j; i++) {
            componentListVo = new ComponentListVo();
            componentListVo.setCalendarId(String.valueOf(component.getCalendarId()));
            componentListVo.setDay(DateUtil.format(dayRanges.get(i), DateConstants.PATTERN_DATE));
            componentListVo.getComponents().add(component);
            componentListVos.add(componentListVo);
        }
        return R.data(componentListVos);
    }

    @ApiOperation(value = "获取日程详情")
    @GetMapping("/{id}")
    public R<ComponentVo> getComponentById(@PathVariable("id") String id) {
        Component component = componentService.getById(id);
        if (component == null) throw new SvrException(SvrResCode.CMS_COMPONENT_NOT_FOUND);
        ComponentVo componentVo = new ComponentVo();
        BeanUtils.copyProperties(component, componentVo);
        MemberCalendar memberCalendar = memberCalendarService.get(Lists.newArrayList(Column.of("member_id", JwtUtils.getUserId()), Column.of("calendar_id", component.getCalendarId())));
        componentVo.setColor(memberCalendar.getColor());
        componentVo.setCalendarName(memberCalendar.getName());
        return R.data(componentVo);
    }


    @ApiOperation(value = "新增或修改日程")
    @PostMapping
    public R<String> add(@Validated @RequestBody ComponentAddParam param) {
        MemberCalendar memberCalendar = memberCalendarService.get(Lists.newArrayList(Column.of("calendar_id", Long.valueOf(param.getCalendarId())), Column.of("member_id", JwtUtils.getUserId())));
        if (memberCalendar == null) throw new SvrException(SvrResCode.CMS_CALENDAR_NOT_FOUND);
        List<ComponentAlarm> componentAlarmList = null;
        Component component = null;
        if (!StringUtils.hasText(param.getId())) {
            component = new Component();
            componentAlarmList = this.insertComponent(param, component);
        } else {
            component = componentService.getById(param.getId());
            componentAlarmList = this.updateComponent(param, component);
        }
        if (!CollectionUtils.isEmpty(componentAlarmList)) {
            ComponentAlarm componentAlarm = null;
            for (int i = 0, j = componentAlarmList.size(); i < j; i++) {
                componentAlarm = componentAlarmList.get(i);
                rabbitmqOutChannel.pushAlarmDelayedMessage(
                        RabbitmqConstants.COMPONENT_ALARM_TYPE,
                        JsonUtils.obj2json(AlarmInfoDto.builder().componentId(String.valueOf(componentAlarm.getComponentId())).alarmId(String.valueOf(componentAlarm.getId())).componentAlarmDate(component.getAlarmTime()).build()),
                        componentAlarm.getAlarmTime().getTime());
            }
        }
        return R.data(String.valueOf(component.getId()));
    }

    @ApiOperation(value = "删除日程")
    @ApiImplicitParam(name = "id", value = "日程ID")
    @DeleteMapping("/{id}")
    public R<String> delete(@PathVariable("id") String id) {
        componentService.delete(Long.valueOf(id));
        return R.status(true);
    }

    /**
     * 功能描述: <br>
     * 〈新增日程〉
     *
     * @param
     * @return:void
     * @since: 1.0.0
     * @Author:
     * @Date: 2022/1/16 14:46
     */
    private List<ComponentAlarm> insertComponent(ComponentAddParam param, Component component) {
        if (!param.getRepeatStatus().equals("0") && param.getRepeatUntil() == null)
            throw new SvrException(SvrResCode.CMS_COMPONENT_REPEAT_UNTIL_EMPTY);
        this.setComponent(param, component);
        return componentService.addComponent(JwtUtils.getUserId(), Long.valueOf(param.getCalendarId()), component, param.getAlarm().getAlarmType(), param.getAlarm().getAlarmTime());
    }

    /**
     * 功能描述: <br>
     * 〈〉
     *
     * @param param
     * @param component
     * @return:java.util.List<cn.com.xuct.calendar.dao.entity.ComponentAlarm>
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/1/17 14:07
     */
    private List<ComponentAlarm> updateComponent(ComponentAddParam param, Component component) {
        if (component == null) throw new SvrException(SvrResCode.CMS_COMPONENT_NOT_FOUND);
        if (!param.getRepeatStatus().equals("0") && param.getRepeatUntil() == null)
            throw new SvrException(SvrResCode.CMS_COMPONENT_REPEAT_UNTIL_EMPTY);
        boolean changed = false;
        if (param.getDtstart().getTime() != component.getDtstart().getTime() ||
                param.getDtend().getTime() != component.getDtend().getTime() ||
                !param.getRepeatStatus().equals(component.getRepeatStatus()) ||
                !param.getRepeatType().equals(component.getRepeatType().getValue()) ||
                !param.getRepeatByday().equals(component.getRepeatByday()) ||
                !param.getRepeatBymonth().equals(component.getRepeatBymonth()) ||
                !param.getRepeatBymonthday().equals(component.getRepeatBymonthday()) ||
                param.getRepeatInterval() != component.getRepeatInterval() ||
                !this.getRepeatUntilEqale(param.getRepeatUntil(), component.getRepeatUntil()) ||
                !param.getAlarm().getAlarmType().equals(component.getAlarmType().getCode())) {
            changed = true;
        }
        this.setComponent(param, component);
        return componentService.updateComponent(JwtUtils.getUserId(), component, param.getAlarm().getAlarmType(), param.getAlarm().getAlarmTime(), changed);
    }


    private void setComponent(ComponentAddParam param, Component component) {
        BeanUtils.copyProperties(param, component);
        component.setStartTime(param.getDtstart().getTime());
        component.setCalendarId(Long.valueOf(param.getCalendarId()));
        component.setEndTime(param.getDtend().getTime());
        component.setCreatorMemberId(JwtUtils.getUserId());
        component.setStatus(CommonStatusEnum.NORMAL);
        if (param.getRepeatStatus().equals("0")) {
            component.setRepeatType(ComponentRepeatTypeEnum.UNKNOWN);
        } else {
            component.setRepeatType(ComponentRepeatTypeEnum.getValueByValue(param.getRepeatType()));
            component.setEndTime(param.getRepeatUntil().getTime());
        }
    }

    /**
     * 功能描述: <br>
     * 〈〉
     *
     * @param paramRepeatUntil
     * @param componentRepeatUntil
     * @return:boolean
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/1/17 17:09
     */
    private boolean getRepeatUntilEqale(Date paramRepeatUntil, Date componentRepeatUntil) {
        if (paramRepeatUntil == null && componentRepeatUntil == null) return true;
        if (paramRepeatUntil == null && componentRepeatUntil != null || paramRepeatUntil != null && componentRepeatUntil == null)
            return false;
        if (paramRepeatUntil.getTime() != componentRepeatUntil.getTime()) return false;
        return true;
    }
}