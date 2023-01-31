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

    /**
     * 通过日历ID按天分组
     *
     * @param calendarId
     * @param startDate
     * @param endDate
     * @return
     */
    List<ComponentListVo> listDaysComponentByCalendar(final Long calendarId, final Long startDate, final Long endDate);

    /**
     * 通过事件ID按天分组
     *
     * @param componentId
     * @return
     */
    List<ComponentListVo> listDaysComponentByComponentId(final Long componentId);

    /**
     * 通过关键字查询事件
     *
     * @param userId
     * @param word
     * @param limit
     * @param page
     * @return
     */
    ComponentSearchVo searchComponentPageByWord(final Long userId, final String word, final Integer limit, final Integer page);

    /**
     * 通过事件ID查询
     *
     * @param userId
     * @param componentId
     * @return
     */
    CalendarComponentVo getComponentById(final Long userId, final Long componentId);

    /**
     * 添加事件
     *
     * @param memberId
     * @param timeZone
     * @param calendarId
     * @param component
     * @param memberIds
     * @param alarmType
     * @param alarmTimes
     * @return
     */
    List<ComponentAlarm> addComponent(final Long memberId, final String timeZone, final Long calendarId, final Component component, final List<String> memberIds, final String alarmType, final List<Integer> alarmTimes);

    /**
     * 更新事件
     *
     * @param oldCalendarId
     * @param memberId
     * @param timeZone
     * @param component
     * @param memberIds
     * @param alarmType
     * @param alarmTimes
     * @param change
     * @return
     */
    List<ComponentAlarm> updateComponent(final Long oldCalendarId, final Long memberId, final String timeZone, final Component component, final List<String> memberIds, final String alarmType, final List<Integer> alarmTimes, boolean change);

    /**
     * 通过事件ID删除
     *
     * @param memberId
     * @param componentId
     * @return
     */
    List<Long> deleteByComponentId(final Long memberId, final Long componentId);
}