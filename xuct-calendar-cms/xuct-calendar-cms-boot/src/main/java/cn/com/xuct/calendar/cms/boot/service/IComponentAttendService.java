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
import cn.com.xuct.calendar.cms.api.vo.ComponentAttendVo;
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

    List<Component> listByCalendarId(final Long calendarId, final Long start, Long end);

    List<CalendarComponentVo> searchWord(final Long memberId, final String word, Integer page, Integer limit);

    List<ComponentAttend> listByComponentIdNoMemberId(final Long memberId, final Long componentId);

    void updateMemberAttendCalendarId(final Long memberId, final Long oldCalendarId, final Long calendarId, final Long componentId);

    void batchUpdateAttendMemberCalendarId(final Long componentId, final Long calendarId, final List<Long> memberIds);

    void acceptAttend(final Long memberId, final Long calendarId, final Long attendCalendarId, final Long componentId);

    void updateAttendMarjoCalendarId(final Long oldCalendarId, final Long newCalendarId);

    void updateAttendCalendarId(final Long oldCalendarId, final Long newCalendarId);

    CalendarAttendCountVo statistics(final Long componentId);

    List<ComponentAttendVo> listByComponentId(final Long componentId, final Long creatorMemberId);

    /**
     * 更新事件邀请状态
     *
     * @param componentId
     * @param userId
     * @param status      邀请状态
     */
    void updateComponentAttendStatus(final Long componentId, final Long userId, final Integer status);


}