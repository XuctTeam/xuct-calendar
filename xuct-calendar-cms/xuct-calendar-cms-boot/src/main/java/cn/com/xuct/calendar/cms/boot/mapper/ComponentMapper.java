/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: ComponentMapper
 * Author:   Derek Xu
 * Date:     2021/12/21 16:42
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.mapper;


import cn.com.xuct.calendar.cms.api.entity.Component;
import cn.com.xuct.calendar.cms.api.vo.CalendarComponentVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/21
 * @since 1.0.0
 */
public interface ComponentMapper extends BaseMapper<Component> {

    /**
     * 通过关键词分页查询
     *
     * @param word
     * @param page
     * @param limit
     * @return
     */
    List<CalendarComponentVo> searchByWord(@Param("word") String word, @Param("page") Integer page, @Param("limit") Integer limit);

    /**
     * 更新事件到新日历
     *
     * @param oldCalendarId
     * @param newCalendarId
     */
    void updateCalendarIdByCalendarId(@Param("oldCalendarId") Long oldCalendarId, @Param("newCalendarId") Long newCalendarId);
}