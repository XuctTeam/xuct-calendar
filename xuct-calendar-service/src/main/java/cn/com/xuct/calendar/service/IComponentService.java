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
package cn.com.xuct.calendar.service;

import cn.com.xuct.calendar.dao.entity.Component;
import cn.com.xuct.calendar.dao.entity.ComponentAlarm;
import cn.com.xuct.calendar.dao.mapper.ComponentMapper;
import cn.com.xuct.calendar.service.base.IBaseService;

import java.util.Date;
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
     * 查询日程列表
     *
     * @param calendarId
     * @param start
     * @param end
     * @return
     */
    List<Component> query(final Long calendarId, final Long start, Long end);


    /**
     * 添加日程
     *
     * @param memberId
     * @param calendarId
     * @param component
     * @param alarmType
     * @param alarmTimes
     */
    List<ComponentAlarm> addComponent(final Long memberId, final Long calendarId, final Component component, final String alarmType, final List<Integer> alarmTimes);

    /**
     * 更新日程
     *
     * @param memberId
     * @param component
     * @param alarmType
     * @param alarmTimes
     * @param change     是否有提醒更新
     * @return
     */
    List<ComponentAlarm> updateComponent(final Long memberId , final Component component, final String alarmType, final List<Integer> alarmTimes, boolean change);

    /**
     * 删除日程
     *
     * @param componentId
     */
    void delete(Long componentId);


}