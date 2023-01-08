/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: DataHelper
 * Author:   Derek Xu
 * Date:     2022/1/6 15:34
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.utils;

import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.io.TimezoneAssignment;
import biweekly.io.TimezoneInfo;
import biweekly.property.DateStart;
import biweekly.util.com.google.ical.compat.javautil.DateIterator;
import cn.com.xuct.calendar.cms.api.entity.Component;
import cn.com.xuct.calendar.cms.boot.config.TimeZoneCacheManager;
import cn.com.xuct.calendar.common.core.constant.DateConstants;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/6
 * @since 1.0.0
 */
@Slf4j
public class DateHelper {


    public static List<DateTime> getRangeDateList(Date start, Date end) {
        return DateUtil.rangeToList(start, end, DateField.DAY_OF_MONTH);
    }

    /**
     * 功能描述: <br>
     * 〈获取时间循环的所有天〉
     *
     * @param timeZoneId
     * @param component
     * @return:java.util.List<java.util.Date>
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/1/7 11:07
     */
    public static List<DateTime> getRepeatRangeDataList(Component component, String timeZoneId) {
        return DateHelper.getRepeatRangeDataList(component, timeZoneId, null);
    }

    /**
     * 功能描述: <br>
     * 〈获取时间循环的所有天〉
     *
     * @param component
     * @param timeZoneId
     * @param begin      开始时间
     * @return:java.util.List<cn.hutool.core.date.DateTime>
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/4/2 15:49
     */
    public static List<DateTime> getRepeatRangeDataList(Component component, String timeZoneId, Date begin) {
        VEvent event = new VEvent();
        event.setDateStart(component.getDtstart());
        TimeZone timeZone = DateHelper.getTimeZone(timeZoneId, event.getDateStart());
        if (timeZone == null){
            return Lists.newArrayList();
        }
        CalendarFactory.parseRepeatDate(component, event);
        DateIterator it = event.getDateIterator(timeZone);
        List<DateTime> rangeList = Lists.newArrayList();
        while (it.hasNext()) {
            if (begin != null && it.next().after(begin)) {
                rangeList.add(DateUtil.date(it.next()));
            } else {
                rangeList.add(DateUtil.date(it.next()));
            }
        }
        return rangeList;
    }

    /**
     * 功能描述: <br>
     * 〈获取循环事件的时区〉
     *
     * @param timeZoneId
     * @param dtstart
     * @return:java.util.TimeZone
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/1/7 11:32
     */
    private static TimeZone getTimeZone(String timeZoneId, DateStart dtstart) {
        ICalendar ical = new ICalendar();
        TimeZone timeZone = TimeZoneCacheManager.get(timeZoneId);
        if (timeZone == null) {
            TimezoneInfo tinfoil = ical.getTimezoneInfo();
            if (tinfoil.isFloating(dtstart)) {
                timeZone = TimeZone.getDefault();
            } else {
                TimezoneAssignment dtstartTimezone = tinfoil.getTimezone(dtstart);
                timeZone = (dtstartTimezone == null) ? TimeZone.getTimeZone(DateConstants.DEFAULT_TIMEZONE) : dtstartTimezone.getTimeZone();
            }
            if (timeZone != null) {
                TimeZoneCacheManager.add(timeZoneId, timeZone);
            }
        }
        return timeZone;
    }
}