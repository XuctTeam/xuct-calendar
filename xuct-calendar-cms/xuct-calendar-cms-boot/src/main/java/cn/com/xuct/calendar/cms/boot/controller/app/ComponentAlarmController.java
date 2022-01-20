/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ComponentAlarmController
 * Author:   Derek Xu
 * Date:     2022/1/13 14:02
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.controller.app;

import cn.com.xuct.calendar.common.core.enums.SortEnum;
import cn.com.xuct.calendar.common.core.exception.SvrException;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.res.SvrResCode;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.core.vo.Sort;
import cn.com.xuct.calendar.common.module.enums.CommonStatusEnum;
import cn.com.xuct.calendar.common.module.params.data.ComponentAddAlarmData;
import cn.com.xuct.calendar.dao.entity.Component;
import cn.com.xuct.calendar.dao.entity.ComponentAlarm;
import cn.com.xuct.calendar.cms.boot.service.IComponentAlarmService;
import cn.com.xuct.calendar.cms.boot.service.IComponentService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/13
 * @since 1.0.0
 */
@Slf4j
@RestController
@Api(tags = "【移动端】日程提醒服务")
@RequestMapping("/api/app/v1/alarm")
@RequiredArgsConstructor
public class ComponentAlarmController {

    private final IComponentService componentService;

    private final IComponentAlarmService componentAlarmService;

    @GetMapping("/{componentId}")
    @ApiOperation("查询日程提醒")
    public R<ComponentAddAlarmData> list(@PathVariable("componentId") String componentId) {
        Component component = componentService.getById(componentId);
        if (component == null) throw new SvrException(SvrResCode.CMS_COMPONENT_NOT_FOUND);
        ComponentAddAlarmData componentAddAlarmData = new ComponentAddAlarmData();
        componentAddAlarmData.setAlarmType(component.getAlarmType().getCode());
        componentAddAlarmData.setAlarmTime(Lists.newArrayList());
        List<ComponentAlarm> componentAlarmList = componentAlarmService.find(Lists.newArrayList(Column.of("component_id", componentId), Column.of("status", CommonStatusEnum.NORMAL)), Sort.of("trigger_sec", SortEnum.asc));
        if (CollectionUtils.isEmpty(componentAlarmList)) return R.data(componentAddAlarmData);
        componentAddAlarmData.setAlarmTime(componentAlarmList.stream().map(ComponentAlarm::getTriggerSec).collect(Collectors.toList()));
        return R.data(componentAddAlarmData);
    }
}