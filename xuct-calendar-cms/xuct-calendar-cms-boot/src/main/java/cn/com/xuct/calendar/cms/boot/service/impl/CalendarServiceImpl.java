/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: CalendarServiceImpl
 * Author:   Derek Xu
 * Date:     2021/11/24 15:55
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.service.impl;

import cn.com.xuct.calendar.cms.api.entity.Calendar;
import cn.com.xuct.calendar.cms.api.vo.CalendarSharedVo;
import cn.com.xuct.calendar.cms.boot.mapper.CalendarMapper;
import cn.com.xuct.calendar.cms.boot.service.ICalendarService;
import cn.com.xuct.calendar.common.core.exception.SvrException;
import cn.com.xuct.calendar.common.core.res.SvrResCode;
import cn.com.xuct.calendar.common.db.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/24
 * @since 1.0.0
 */
@Service
public class CalendarServiceImpl extends BaseServiceImpl<CalendarMapper, Calendar> implements ICalendarService {

    @Override
    public CalendarSharedVo getCalendarShared(Long calendarId) {
        Calendar calendar = this.getById(calendarId);
        if (calendar == null) {
            throw new SvrException(SvrResCode.CMS_CALENDAR_NOT_FOUND);
        }
        return null;
    }
}