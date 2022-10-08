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

import cn.com.xuct.calendar.cms.api.entity.*;
import cn.com.xuct.calendar.cms.api.feign.BasicServicesFeignClient;
import cn.com.xuct.calendar.cms.api.vo.*;
import cn.com.xuct.calendar.cms.boot.config.DomainConfiguration;
import cn.com.xuct.calendar.cms.boot.config.UploadConfiguration;
import cn.com.xuct.calendar.cms.boot.handler.RabbitmqOutChannel;
import cn.com.xuct.calendar.cms.boot.service.IComponentAttachmentService;
import cn.com.xuct.calendar.cms.boot.service.IComponentAttendService;
import cn.com.xuct.calendar.cms.boot.service.IComponentService;
import cn.com.xuct.calendar.cms.boot.service.IMemberCalendarService;
import cn.com.xuct.calendar.cms.boot.utils.DateHelper;
import cn.com.xuct.calendar.cms.queue.event.ComponentDelEvent;
import cn.com.xuct.calendar.common.core.constant.DateConstants;
import cn.com.xuct.calendar.common.core.constant.RabbitmqConstants;
import cn.com.xuct.calendar.common.core.exception.SvrException;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.res.RetOps;
import cn.com.xuct.calendar.common.core.res.SvrResCode;
import cn.com.xuct.calendar.common.core.utils.JsonUtils;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.fdfs.client.FdfsClient;
import cn.com.xuct.calendar.common.fdfs.exception.FdfsClientException;
import cn.com.xuct.calendar.common.module.dto.AlarmInfoDto;
import cn.com.xuct.calendar.common.module.enums.CommonStatusEnum;
import cn.com.xuct.calendar.common.module.enums.ComponentRepeatTypeEnum;
import cn.com.xuct.calendar.common.module.feign.PersonInfo;
import cn.com.xuct.calendar.common.module.feign.req.ShortChainFeignInfo;
import cn.com.xuct.calendar.common.module.params.ComponentAddParam;
import cn.com.xuct.calendar.common.module.params.ComponentAttendParam;
import cn.com.xuct.calendar.common.security.utils.SecurityUtils;
import cn.com.xuct.calendar.common.web.utils.SpringContextHolder;
import cn.com.xuct.calendar.ums.oauth.client.MemberFeignClient;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
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
@Slf4j
@RestController
@Tag(name = "【移动端】事件服务")
@RequestMapping("/api/app/v1/component")
@RequiredArgsConstructor
public class ComponentController {

    private final DomainConfiguration domainConfiguration;
    private final UploadConfiguration uploadConfiguration;
    private final IComponentService componentService;
    private final IComponentAttendService componentAttendService;
    private final IMemberCalendarService memberCalendarService;
    private final IComponentAttachmentService componentAttachmentService;

    private final MemberFeignClient memberFeignClient;
    private final BasicServicesFeignClient basicServicesFeignClient;
    private final RabbitmqOutChannel rabbitmqOutChannel;

    @Operation(summary = "通过日历查询日程-天分组")
    @GetMapping("/list/calendar/days")
    public R<List<ComponentListVo>> listByCalendarId(@RequestParam("calendarId") String calendarId, @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
                                                     @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end) {
        List<Component> componentList = componentAttendService.listByCalendarId(Long.valueOf(calendarId), start.getTime(), end.getTime());
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

    @Operation(summary = "通过ID查询日程-天分组")
    @GetMapping("/days/{id}")
    public R<List<ComponentListVo>> getComponentDaysById(@PathVariable("id") String id) {
        Component component = componentService.getById(id);
        if (component == null) throw new SvrException(SvrResCode.CMS_COMPONENT_NOT_FOUND);
        final List<DateTime> dayRanges = "0".equals(component.getRepeatStatus()) ?
                DateHelper.getRangeDateList(component.getDtstart(), component.getDtend()) : DateHelper.getRepeatRangeDataList(component, SecurityUtils.getTimeZone());
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


    @Operation(summary = "通过关键字查询日程")
    @GetMapping("/list/search")
    public R<ComponentSearchVo> listBySearch(@RequestParam("word") String word, @RequestParam("limit") Integer limit, @RequestParam("page") Integer page) {
        ComponentSearchVo componentSearchVo = new ComponentSearchVo();
        List<CalendarComponentVo> calendarComponentVos = componentAttendService.searchWord(SecurityUtils.getUserId(), word, page, limit + 1);
        if (calendarComponentVos.size() <= limit) {
            componentSearchVo.setFinished(true);
            componentSearchVo.setComponents(calendarComponentVos);
            return R.data(componentSearchVo);
        }
        calendarComponentVos.remove(limit.intValue());
        componentSearchVo.setComponents(calendarComponentVos);
        return R.data(componentSearchVo);
    }


    @Operation(summary = "获取日程详情")
    @GetMapping("/{id}")
    public R<CalendarComponentVo> getComponentById(@PathVariable("id") String id) {
        Component component = componentService.getById(id);
        Assert.notNull(component, "事件不存在");
        CalendarComponentVo calendarComponentVo = new CalendarComponentVo();
        BeanUtils.copyProperties(component, calendarComponentVo);
        Long calendarId = component.getCalendarId();
        if (!String.valueOf(component.getCreatorMemberId()).equals(String.valueOf(SecurityUtils.getUserId()))) {
            ComponentAttend attend = componentAttendService.get(Lists.newArrayList(Column.of("component_id", id), Column.of("member_id", SecurityUtils.getUserId())));
            Assert.notNull(attend, "邀请数据错误");
            calendarId = attend.getAttendCalendarId();
        }
        MemberCalendar memberCalendar = memberCalendarService.get(Lists.newArrayList(Column.of("calendar_id", calendarId), Column.of("member_id", SecurityUtils.getUserId())));
        Assert.notNull(memberCalendar, "日历不存在");
        calendarComponentVo.setColor(memberCalendar.getColor());
        calendarComponentVo.setCalendarName(memberCalendar.getName());
        return R.data(calendarComponentVo);
    }

    @Operation(summary = "新增或修改日程")
    @PostMapping
    public R<String> add(@Validated @RequestBody ComponentAddParam param) {
        MemberCalendar memberCalendar = memberCalendarService.get(Lists.newArrayList(Column.of("calendar_id", Long.valueOf(param.getCalendarId())), Column.of("member_id", SecurityUtils.getUserId())));
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
        if (CollectionUtils.isEmpty(componentAlarmList)) return R.data(String.valueOf(component.getId()));
        ComponentAlarm componentAlarm = null;
        for (int i = 0, j = componentAlarmList.size(); i < j; i++) {
            componentAlarm = componentAlarmList.get(i);
            log.info("component controller:: alarm delay component id = {} , next time = {}", component.getId(), componentAlarm.getDelayTime());
            rabbitmqOutChannel.pushAlarmDelayedMessage(
                    RabbitmqConstants.COMPONENT_ALARM_TYPE,
                    JsonUtils.obj2json(AlarmInfoDto.builder().componentId(String.valueOf(componentAlarm.getComponentId())).alarmId(String.valueOf(componentAlarm.getId())).build()),
                    componentAlarm.getDelayTime());
        }
        return R.data(String.valueOf(component.getId()));
    }

    @Operation(summary = "删除日程")
    @DeleteMapping("/{id}")
    public R<String> delete(@PathVariable("id") Long id) {
        Component component = componentService.getById(id);
        Assert.notNull(component, "事件不存在");
        List<Long> memberIds = componentService.deleteByComponentId(SecurityUtils.getUserId(), id);
        /* 1.推送日程删除消息 */
        if (CollectionUtils.isEmpty(memberIds)) return R.status(true);
        SpringContextHolder.publishEvent(new ComponentDelEvent(this, component.getId(), component.getSummary(),
                DateUtil.format(component.getDtstart(), DatePattern.NORM_DATETIME_FORMAT), component.getCreatorMemberId(), component.getLocation(), component.getRepeatStatus(), null, memberIds));
        return R.status(true);
    }

    @Operation(summary = "查询所有邀请人ID")
    @GetMapping("/attend/member/ids")
    public R<List<String>> queryComponentMemberIds(@RequestParam("componentId") Long componentId) {
        List<Long> memberIds = componentAttendService.listByComponentIdNoMemberId(SecurityUtils.getUserId(), componentId);
        if (CollectionUtils.isEmpty(memberIds)) return R.data(Lists.newArrayList());
        return R.data(memberIds.stream().map(x -> String.valueOf(x)).collect(Collectors.toList()));
    }

    @Operation(summary = "查询所有邀请人")
    @GetMapping("/attend/member")
    public R<List<ComponentAttendVo>> queryComponentAttend(@RequestParam("componentId") Long componentId, @RequestParam(value = "createMemberId", required = false) Long createMemberId) {
        List<Long> memberIds = componentAttendService.listByComponentIdNoMemberId(createMemberId, componentId);
        if (CollectionUtils.isEmpty(memberIds)) return R.data(Lists.newArrayList());
        R<List<PersonInfo>> memberInfoResult = memberFeignClient.listMemberByIds(memberIds);
        List<PersonInfo> personInfos = RetOps.of(memberInfoResult).getData().orElse(Lists.newArrayList());
        if (CollectionUtils.isEmpty(personInfos)) return R.data(Lists.newArrayList());
        return R.data(memberInfoResult.getData().stream().map(info -> {
            ComponentAttendVo attendVo = new ComponentAttendVo();
            attendVo.setAvatar(info.getAvatar());
            attendVo.setMemberId(String.valueOf(info.getUserId()));
            attendVo.setName(info.getName());
            return attendVo;
        }).collect(Collectors.toList()));
    }

    @Operation(summary = "获取邀请状态")
    @GetMapping("/attend/status")
    public R<Integer> getComponentAttendStatus(@RequestParam("componentId") Long componentId) {
        Component component = componentService.getById(componentId);
        Assert.notNull(component, "事件不存在");
        ComponentAttend attend = componentAttendService.get(Lists.newArrayList(Column.of("component_id", componentId), Column.of("member_id", SecurityUtils.getUserId())));
        Assert.notNull(attend, "邀请数据错误");
        return R.data(attend.getStatus());
    }

    @Operation(summary = "邀请待定或接受")
    @PostMapping("/attend/status")
    public R<String> updateComponentAttendStatus(@RequestBody ComponentAttendParam param) {
        ComponentAttend attend = componentAttendService.get(Lists.newArrayList(Column.of("component_id", param.getComponentId()), Column.of("member_id", SecurityUtils.getUserId())));
        Assert.notNull(attend, "邀请数据错误");
        attend.setStatus(param.getStatus());
        componentAttendService.updateById(attend);
        return R.status(true);
    }

    @Operation(summary = "拒绝邀请")
    @DeleteMapping("/attend/{componentId}")
    public R<String> deleteComponentAttend(@PathVariable("componentId") Long componentId) {
        ComponentAttend attend = componentAttendService.get(Lists.newArrayList(Column.of("component_id", componentId), Column.of("member_id", SecurityUtils.getUserId())));
        Assert.notNull(attend, "邀请数据错误");
        componentAttendService.removeById(attend);
        return R.status(true);
    }

    @Operation(summary = "判断邀请是否存在")
    @GetMapping("/attend/exists")
    public R<Integer> attendExists(@RequestParam("componentId") Long componentId) {
        Component component = componentService.getById(componentId);
        Assert.notNull(component, "事件不存在");
        ComponentAttend attend = componentAttendService.get(Lists.newArrayList(Column.of("component_id", componentId), Column.of("member_id", SecurityUtils.getUserId())));
        return R.data(attend == null ? 0 : 1);
    }

    @Operation(summary = "加入邀请")
    @PostMapping("/attend/accept")
    public R<String> acceptAttend(@RequestBody ComponentAttendParam param) {
        Component component = componentService.getById(param.getComponentId());
        Long memberId = SecurityUtils.getUserId();
        Assert.notNull(component, "事件不存在");
        ComponentAttend attend = componentAttendService.get(Lists.newArrayList(Column.of("component_id", param.getComponentId()), Column.of("member_id", memberId)));
        Assert.isNull(attend, "已加入邀请");
        MemberCalendar memberCalendar = memberCalendarService.get(Lists.newArrayList(Column.of("member_id", memberId), Column.of("major", 1)));
        Assert.notNull(memberCalendar, "查询主日历失败");
        componentAttendService.acceptAttend(memberId, component.getCalendarId(), memberCalendar.getCalendarId(), param.getComponentId());
        return R.status(true);
    }

    @Operation(summary = "统计日程邀请")
    @GetMapping("/attend/statistics")
    public R<CalendarAttendCountVo> statistics(@RequestParam("componentId") String componentId) {
        return R.data(componentAttendService.statistics(Long.valueOf(componentId)));
    }

    @Operation(summary = "获取短链接")
    @GetMapping("/short")
    public R<String> getShortChain(@RequestParam("componentId") String componentId) {
        return basicServicesFeignClient.shortChain(ShortChainFeignInfo.builder().url(domainConfiguration.getCalendar().concat("?componentId=").concat(componentId))
                .type("calendar").expire(7200000).build());
    }

    @Operation(summary = "上传附件")
    @PostMapping("/upload")
    public R<ComponentAttachment> upload(@RequestParam("file") MultipartFile file, @RequestParam("uuid") String uuid, @RequestParam(value = "componentId", required = false) Long componentId) throws IOException, FdfsClientException {
        Assert.isTrue(!(StringUtils.hasText(uuid) && componentId == null), "参数错误");
        Long count = componentAttachmentService.count(componentId != null ? Column.of("component_id", componentId) : Column.of("uuid", uuid));
        if (count > uploadConfiguration.getMaxNumber()) return R.fail("已达到最大");
        FdfsClient fdfsClient = new FdfsClient();
        String url = fdfsClient.upload(file, Maps.newHashMap());
        ComponentAttachment componentAttachment = new ComponentAttachment();
        componentAttachment.setDomain(domainConfiguration.getImages());
        componentAttachment.setFileName(file.getOriginalFilename());
        componentAttachment.setSuffix(file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."), file.getOriginalFilename().length()));
        componentAttachment.setPath(url);
        if (componentId == null && StringUtils.hasText(uuid)) {
            componentAttachment.setUuid(uuid);
        }
        if (componentId != null) {
            componentAttachment.setComponentId(componentId);
        }
        componentAttachmentService.save(componentAttachment);
        return R.data(componentAttachment);
    }


    /**
     * 功能描述: <b>r
     * 〈新增日程〉
     *
     * @param
     * @return:void
     * @since: 1.0.0
     * @Author:
     * @Date: 2022/1/16 14:64
     */
    private List<ComponentAlarm> insertComponent(ComponentAddParam param, Component component) {
        if (!param.getRepeatStatus().equals("0") && param.getRepeatUntil() == null)
            throw new SvrException(SvrResCode.CMS_COMPONENT_REPEAT_UNTIL_EMPTY);
        this.setComponent(param, component);
        return componentService.addComponent(SecurityUtils.getUserId(), SecurityUtils.getTimeZone(), Long.valueOf(param.getCalendarId()), component, param.getMemberIds(), param.getAlarmType(), param.getAlarmTimes());
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
        Long oldCalendarId = component.getCalendarId();
        if (param.getDtstart().getTime() != component.getDtstart().getTime() ||
                param.getDtend().getTime() != component.getDtend().getTime() ||
                //全天事件要处理
                param.getFullDay() != component.getFullDay() ||
                !param.getRepeatStatus().equals(component.getRepeatStatus()) ||
                !param.getRepeatType().equals(component.getRepeatType().getValue()) ||
                !param.getRepeatByday().equals(component.getRepeatByday()) ||
                !param.getRepeatBymonth().equals(component.getRepeatBymonth()) ||
                !param.getRepeatBymonthday().equals(component.getRepeatBymonthday()) ||
                param.getRepeatInterval() != component.getRepeatInterval() ||
                !this.getRepeatUntilEquals(param.getRepeatUntil(), component.getRepeatUntil()) ||
                !param.getAlarmType().equals(component.getAlarmType().getCode())) {
            changed = true;
        }
        this.setComponent(param, component);
        return componentService.updateComponent(oldCalendarId, SecurityUtils.getUserId(), SecurityUtils.getTimeZone(), component, param.getMemberIds(), param.getAlarmType(), param.getAlarmTimes(), changed);
    }


    private void setComponent(ComponentAddParam param, Component component) {
        BeanUtils.copyProperties(param, component);
        component.setStartTime(param.getDtstart().getTime());
        component.setCalendarId(Long.valueOf(param.getCalendarId()));
        component.setEndTime(param.getDtend().getTime());
        component.setCreatorMemberId(SecurityUtils.getUserId());
        component.setStatus(CommonStatusEnum.NORMAL);
        if (param.getRepeatStatus().equals("0")) {
            component.setRepeatType(ComponentRepeatTypeEnum.UNKNOWN);
        } else {
            component.setRepeatType(ComponentRepeatTypeEnum.getValueByValue(param.getRepeatType()));
            component.setEndTime(param.getRepeatUntil().getTime());
        }
        /* 全天事件处理 */
        if (param.getFullDay() == 1) {
            Date date = param.getDtstart();
            component.setDtstart(DateUtil.beginOfDay(date));
            component.setDtend(DateUtil.endOfDay(date).offset(DateField.MILLISECOND, -999));
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
    private boolean getRepeatUntilEquals(Date paramRepeatUntil, Date componentRepeatUntil) {
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
                dayRanges.addAll(DateHelper.getRepeatRangeDataList(component, SecurityUtils.getTimeZone()));
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