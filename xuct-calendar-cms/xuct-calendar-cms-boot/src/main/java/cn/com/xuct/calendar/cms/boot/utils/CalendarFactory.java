/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: CalendarFactory
 * Author:   Derek Xu
 * Date:     2022/1/6 15:37
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.utils;

import biweekly.component.VEvent;
import biweekly.util.DayOfWeek;
import biweekly.util.Frequency;
import biweekly.util.Recurrence;
import cn.com.xuct.calendar.cms.api.entity.Component;
import cn.com.xuct.calendar.common.core.utils.EnumUtil;
import cn.com.xuct.calendar.common.module.enums.ComponentRepeatTypeEnum;
import com.google.common.collect.Sets;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/6
 * @since 1.0.0
 */
public class CalendarFactory {

    public static final String BY_MONTH = "bymonth";
    public static final String BY_MONTHDAY = "bymonthday";
    public static final Date MAX_DATE = new Date(1893427200000L);


    public static void parseRepeatDate(final Component info, final VEvent event) {
        Recurrence rrule = new Recurrence.Builder(Frequency.DAILY).interval(Integer.valueOf(1)).build();
        Recurrence.Builder builder = new Recurrence.Builder(rrule);

        if (info.getRepeatType().equals(ComponentRepeatTypeEnum.DAILY)) {
            builder.frequency(Frequency.DAILY);
        } else if (info.getRepeatType().equals(ComponentRepeatTypeEnum.WEEKLY)) {
            builder.frequency(Frequency.WEEKLY);
            String byday = info.getRepeatByday();
            Set<Integer> pos = formatRepeatBysetpos(info.getRepeatBysetpos());
            parseByDayData(pos, byday, builder);

        } else if (info.getRepeatType().equals(ComponentRepeatTypeEnum.MONTHLY)) {
            builder.frequency(Frequency.MONTHLY);
            parseByMonthData(BY_MONTHDAY, formatRepeatDayToSet(info.getRepeatBymonthday()), builder);
            // byday
            String byday = info.getRepeatByday();
            Set<Integer> pos = formatRepeatBysetpos(info.getRepeatBysetpos());
            parseByDayData(pos, byday, builder);
        } else if (info.getRepeatType().equals(ComponentRepeatTypeEnum.YEARLY)) {
            builder.frequency(Frequency.YEARLY);
            // bymonth
            parseByMonthData(BY_MONTH, formatRepeatDayToSet(info.getRepeatBymonth()), builder);
            /* 年的日循环 */
            parseByMonthData(BY_MONTHDAY, formatRepeatDayToSet(info.getRepeatBymonthday()), builder);
        } else {
            rrule = null;
        }
        if (null != info.getRepeatInterval() && info.getRepeatInterval().intValue() != 0) {
            builder.interval(info.getRepeatInterval());
        }
        if (info.getRepeatUntil() == null || info.getRepeatUntil().getTime() > MAX_DATE.getTime()) {
            builder.until(MAX_DATE);
        } else {
            builder.until(info.getRepeatUntil());
        }
        if (rrule != null) {
            rrule = builder.build();
        }
        event.setRecurrenceRule(rrule);
    }

    /**
     * 功能描述: <br>
     * 〈〉
     *
     * @param pos
     * @param byday
     * @param builder
     * @return:void
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/1/6 15:45
     */
    public static void parseByDayData(final Set<Integer> pos, final String byday, final Recurrence.Builder builder) {
        // 设置pos
        if (null != pos && !pos.isEmpty()) {
            for (Integer p : pos) {
                builder.bySetPos(p);
            }
        }
        // 设置byday
        if (null != byday && !"0".equals(byday) && !"".equals(byday)) {
            String[] bydayarr = byday.split(",");
            for (String str : bydayarr) {
                if ("0".equals(str.split(":")[0])) {
                    builder.byDay(EnumUtil.evalueOf(DayOfWeek.class, str.split(":")[1]));
                } else {
                    builder.byDay(Integer.valueOf(str.split(":")[0]), EnumUtil.evalueOf(DayOfWeek.class, str.split(":")[1]));
                }
            }
        }
    }

    public static void parseByMonthData(final String type, final HashSet<Integer> bymonth, final Recurrence.Builder builder) {
        if (null == bymonth || bymonth.isEmpty()) {
            return;
        }
        for (Integer day : bymonth) {
            if (BY_MONTHDAY.equals(type)) {
                builder.byMonthDay(day);
            }
            if (BY_MONTH.equals(type)) {
                builder.byMonth(day);
            }
        }
    }

    private static Set<Integer> formatRepeatBysetpos(final String repeatBysetpos) {
        if (!StringUtils.hasLength(repeatBysetpos)) return Sets.newHashSet();
        return Stream.of(repeatBysetpos.split(",")).map(string -> {
            return Integer.parseInt(string);
        }).collect(Collectors.toSet());
    }

    /**
     * 功能描述: <br>
     * 〈格式化年的天循环〉
     *
     * @param repeatDay
     * @return:java.util.Set<java.lang.Integer>
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/1/7 10:41
     */
    private static HashSet<Integer> formatRepeatDayToSet(final String repeatDay) {
        HashSet<Integer> bymonthday = new HashSet<Integer>();
        if (StringUtils.hasLength(repeatDay)) {
            bymonthday.add(Integer.parseInt(repeatDay));
        }
        return bymonthday;
    }
}