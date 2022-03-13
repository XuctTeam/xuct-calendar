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
     * 通过事件ID查询
     *
     * @param memberId
     * @param componentId
     * @return
     */
    List<Long> listByComponentId(@Param("memberId") Long memberId, @Param("componentId") Long componentId);
}