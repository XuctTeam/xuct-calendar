/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: IComponentAttendService
 * Author:   Derek Xu
 * Date:     2022/3/13 16:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.service;

import cn.com.xuct.calendar.cms.api.entity.Component;
import cn.com.xuct.calendar.cms.api.entity.ComponentAttend;
import cn.com.xuct.calendar.cms.api.vo.CalendarAttendCountVo;
import cn.com.xuct.calendar.cms.api.vo.CalendarComponentVo;
import cn.com.xuct.calendar.cms.boot.mapper.ComponentAttendMapper;
import cn.com.xuct.calendar.common.db.service.IBaseService;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/13
 * @since 1.0.0
 */
public interface IComponentAttendService extends IBaseService<ComponentAttendMapper, ComponentAttend> {

    /**
     * 查询被邀请事件
     *
     * @param calendarId
     * @param start
     * @param end
     * @return
     */
    List<Component> listByCalendarId(final Long calendarId, final Long start, Long end);

    /**
     * 通过关键词查询
     *
     * @param memberId
     * @param word
     * @param page
     * @param limit
     * @return
     */
    List<CalendarComponentVo> searchWord(final Long memberId, final String word, Integer page, Integer limit);

    /**
     * 查询不包括memberId的邀请
     *
     * @param memberId
     * @param componentId
     * @return
     */
    List<ComponentAttend> listByComponentIdNoMemberId(final Long memberId, final Long componentId);

    /**
     * 更新用户自己事件的邀请日历
     *
     * @param memberId
     * @param oldCalendarId
     * @param calendarId
     * @param componentId
     */
    void updateMemberAttendCalendarId(final Long memberId, final Long oldCalendarId, final Long calendarId, final Long componentId);

    /**
     * 批量更新被邀请人的日历
     *
     * @param componentId
     * @param calendarId
     * @param memberIds
     */
    void batchUpdateAttendMemberCalendarId(final Long componentId, final Long calendarId, final List<Long> memberIds);

    /**
     * 加入邀请
     *
     * @param memberId
     * @param calendarId
     * @param attendCalendarId
     * @param componentId
     */
    void acceptAttend(final Long memberId, final Long calendarId, final Long attendCalendarId, final Long componentId);

    /**
     * 更新被邀请日历到新日历
     *
     * @param oldCalendarId
     * @param newCalendarId
     */
    void updateAttendMarjoCalendarId(final Long oldCalendarId, final Long newCalendarId);

    /**
     * 更新邀请的日历到新日历
     *
     * @param oldCalendarId
     * @param newCalendarId
     */
    void updateAttendCalendarId(final Long oldCalendarId, final Long newCalendarId);

    /**
     * 日程邀请统计
     *
     * @param componentId
     * @return
     */
    CalendarAttendCountVo statistics(final Long componentId);


}