/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: ComponentAttendMapper
 * Author:   Derek Xu
 * Date:     2022/3/13 16:50
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.mapper;

import cn.com.xuct.calendar.cms.api.entity.Component;
import cn.com.xuct.calendar.cms.api.entity.ComponentAttend;
import cn.com.xuct.calendar.cms.api.vo.CalendarComponentVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/13
 * @since 1.0.0
 */
public interface ComponentAttendMapper extends BaseMapper<ComponentAttend> {

    /**
     * 通过
     *
     * @param calendarId
     * @param start
     * @param end
     * @return
     */
    List<Component> listByCalendarId(@Param("calendar") Long calendarId, @Param("start") Long start, @Param("end") Long end);

    /**
     * 通过关键词查询
     *
     * @param memberId
     * @param word
     * @param page
     * @param limit
     * @return
     */
    List<CalendarComponentVo> searchWord(@Param("memberId") Long memberId, @Param("word") String word, @Param("page") Integer page, @Param("limit") Integer limit);

    /**
     * 查询不包括memberId的邀请
     *
     * @param memberId
     * @param componentId
     * @return
     */
    List<Long> listByComponentIdNoMemberId(@Param("memberId") Long memberId, @Param("componentId") Long componentId);

    /**
     * 更新用户自己事件的邀请日历
     *
     * @param memberId
     * @param oldCalendarId
     * @param calendarId
     */
    void updateMemberAttendCalendarId(@Param("memberId") Long memberId, @Param("oldCalendarId") Long oldCalendarId, @Param("calendarId") Long calendarId, @Param("componentId") Long componentId);

    /**
     * 批量更新被邀请人的日历
     *
     * @param componentId
     * @param calendarId
     * @param memberIds
     */
    void batchUpdateAttendMemberCalendarId(@Param("componentId") Long componentId, @Param("calendarId") Long calendarId, @Param("memberIds") List<Long> memberIds);
}