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

import cn.com.xuct.calendar.cms.api.entity.Component;
import cn.com.xuct.calendar.cms.api.entity.ComponentAlarm;
import cn.com.xuct.calendar.cms.api.entity.MemberCalendar;
import cn.com.xuct.calendar.cms.api.vo.ComponentDayListVo;
import cn.com.xuct.calendar.cms.api.vo.ComponentDayVo;
import cn.com.xuct.calendar.cms.api.vo.ComponentListVo;
import cn.com.xuct.calendar.cms.boot.handler.RabbitmqOutChannel;
import cn.com.xuct.calendar.cms.boot.service.IComponentService;
import cn.com.xuct.calendar.cms.boot.service.IMemberCalendarService;
import cn.com.xuct.calendar.cms.boot.utils.DateHelper;
import cn.com.xuct.calendar.common.core.constant.DateConstants;
import cn.com.xuct.calendar.common.core.constant.RabbitmqConstants;
import cn.com.xuct.calendar.common.core.exception.SvrException;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.res.SvrResCode;
import cn.com.xuct.calendar.common.core.utils.JsonUtils;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.dto.AlarmInfoDto;
import cn.com.xuct.calendar.common.module.enums.CommonStatusEnum;
import cn.com.xuct.calendar.common.module.enums.ComponentRepeatTypeEnum;
import cn.com.xuct.calendar.common.module.params.ComponentAddParam;
import cn.com.xuct.calendar.common.web.utils.JwtUtils;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
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

import java.util.*;

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

    private final IComponentService componentService;

    private final IMemberCalendarService memberCalendarService;

    private final RabbitmqOutChannel rabbitmqOutChannel;


    @ApiOperation(value = "通过日历查询日程")
    @GetMapping("/list/calendar/days")
    public R<List<ComponentListVo>> listByCalendarId(@RequestParam("calendarId") String calendarId, @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
                                                     @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end) {

        List<Component> componentList = componentService.query(Long.valueOf(calendarId), start.getTime(), end.getTime());
        if (CollectionUtils.isEmpty(componentList)) return R.data(Lists.newArrayList());
        LinkedHashMap<String, List<Component>> componentListMap = Maps.newLinkedHashMap();
        this.covertComponentMaps(componentList, componentListMap);
        List<ComponentListVo> componentListVos = Lists.newArrayList();
        if (MapUtil.isEmpty(componentListMap)) return R.data(componentListVos);
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

    @ApiOperation(value = "通过ID查询日程")
    @GetMapping("/days/{id}")
    public R<List<ComponentListVo>> getComponentDaysById(@PathVariable("id") String id) {
        Component component = componentService.getById(id);
        if (component == null) throw new SvrException(SvrResCode.CMS_COMPONENT_NOT_FOUND);
        final List<DateTime> dayRanges = "0".equals(component.getRepeatStatus()) ? DateHelper.getRangeDateList(component.getDtstart(), component.getDtend()) :
                DateHelper.getRepeatRangeDataList(JwtUtils.getTimeZone(), component);
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


    @ApiOperation(value = "通过关键字查询日程")
    @GetMapping("/list/search/days")
    public R<List<ComponentDayListVo>> listBySearch(@RequestParam("word") String word, @RequestParam("limit") Integer limit, @RequestParam("page") Integer page) {
        List<ComponentDayVo> componentDayVos = componentService.search(word, page, limit);
        if (CollectionUtils.isEmpty(componentDayVos)) return R.data(Lists.newArrayList());
        LinkedHashMap<String, List<ComponentDayVo>> componentListMap = Maps.newLinkedHashMap();
        this.covertComponentMaps(componentDayVos, componentListMap);
        List<ComponentDayListVo> list = Lists.newArrayList();
        if (MapUtil.isEmpty(componentListMap)) return R.data(list);
        ComponentDayListVo componentDayVo = null;
        for (Map.Entry<String, List<ComponentDayVo>> entry : componentListMap.entrySet()) {
            componentDayVo = new ComponentDayListVo();
            componentDayVo.setDay(entry.getKey());
            componentDayVo.setComponents(entry.getValue());
            list.add(componentDayVo);
        }
        Collections.sort(list, (o1, o2) -> DateUtil.parse(o2.getDay()).compareTo(DateUtil.parse(o1.getDay())));
        return R.data(list);
    }


    @ApiOperation(value = "获取日程详情")
    @GetMapping("/{id}")
    public R<ComponentDayVo> getComponentById(@PathVariable("id") String id) {
        Component component = componentService.getById(id);
        if (component == null) throw new SvrException(SvrResCode.CMS_COMPONENT_NOT_FOUND);
        ComponentDayVo componentDayVo = new ComponentDayVo();
        BeanUtils.copyProperties(component, componentDayVo);
        MemberCalendar memberCalendar = memberCalendarService.get(Lists.newArrayList(Column.of("member_id", JwtUtils.getUserId()), Column.of("calendar_id", component.getCalendarId())));
        componentDayVo.setColor(memberCalendar.getColor());
        componentDayVo.setCalendarName(memberCalendar.getName());
        return R.data(componentDayVo);
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
                        JsonUtils.obj2json(AlarmInfoDto.builder().componentId(String.valueOf(componentAlarm.getComponentId())).alarmId(String.valueOf(componentAlarm.getId())).build()),
                        componentAlarm.getDelayTime());
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
        return componentService.addComponent(JwtUtils.getUserId(), JwtUtils.getTimeZone(), Long.valueOf(param.getCalendarId()), component, param.getAlarmType(), param.getAlarmTimes());
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
                !param.getAlarmType().equals(component.getAlarmType().getCode())) {
            changed = true;
        }
        this.setComponent(param, component);
        return componentService.updateComponent(JwtUtils.getUserId(), JwtUtils.getTimeZone(), component, param.getAlarmType(), param.getAlarmTimes(), changed);
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
     * 〈比较日程重复〉
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

    /**
     * 功能描述: <br>
     * 〈封装按天查询日程〉
     *
     * @param comps
     * @param componentListMap
     * @return:void
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/1/24 18:10
     */
    private <T extends Component> void covertComponentMaps(List<T> comps, LinkedHashMap<String, List<T>> componentListMap) {
        List<DateTime> dayRanges = Lists.newArrayList();
        comps.stream().forEach(component -> {
            dayRanges.clear();
            if ("0".equals(component.getRepeatStatus())) {
                dayRanges.addAll(DateHelper.getRangeDateList(component.getDtstart(), component.getDtend()));
            } else {
                dayRanges.addAll(DateHelper.getRepeatRangeDataList(JwtUtils.getTimeZone(), component));
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
    }
}