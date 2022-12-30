/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: IComponentService
 * Author:   Derek Xu
 * Date:     2021/12/21 16:44
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.service;

import cn.com.xuct.calendar.cms.api.entity.Component;
import cn.com.xuct.calendar.cms.api.entity.ComponentAlarm;
import cn.com.xuct.calendar.cms.api.vo.CalendarComponentVo;
import cn.com.xuct.calendar.cms.api.vo.ComponentListVo;
import cn.com.xuct.calendar.cms.api.vo.ComponentSearchVo;
import cn.com.xuct.calendar.cms.boot.mapper.ComponentMapper;
import cn.com.xuct.calendar.common.db.service.IBaseService;
import io.swagger.v3.oas.models.security.SecurityScheme;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/21
 * @since 1.0.0
 */
public interface IComponentService extends IBaseService<ComponentMapper, Component> {

    List<ComponentListVo> listDaysComponentByCalendar(final Long calendarId, final Long startDate, final Long endDate);

    List<ComponentListVo> listDaysComponentByComponentId(final Long componentId);

    ComponentSearchVo searchComponentPageByWord(final Long userId , final String word, final Integer limit, final Integer page);

    CalendarComponentVo getComponentById(final Long userId , final Long componentId);

    List<ComponentAlarm> addComponent(final Long memberId, final String timeZone, final Long calendarId, final Component component, final List<String> memberIds, final String alarmType, final List<Integer> alarmTimes);

    List<ComponentAlarm> updateComponent(final Long oldCalendarId, final Long memberId, final String timeZone, final Component component, final List<String> memberIds, final String alarmType, final List<Integer> alarmTimes, boolean change);

    List<Long> deleteByComponentId(final Long memberId, final Long componentId);
}