/**
 * Copyright (C), 2021-2021, 263
 * FileName: DateConstant
 * Author:   Derek xu
 * Date:     2021/6/28 16:40
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.core.constant;

import java.time.format.DateTimeFormatter;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Administrator
 * @create 2021/6/28
 * @since 1.0.0
 */
public class DateConstants {

    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";

    public static final String DEFAULT_TIMEZONE = "Asia/Shanghai";
    /**
     * java 8 时间格式化
     */
    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern(DateConstants.PATTERN_DATETIME);
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DateConstants.PATTERN_DATE);
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern(DateConstants.PATTERN_TIME);

    /**
     * 一天
     */
    public static final Long DAY_SECONDS = 1000 * 60 * 60 * 24L;

    /**
     * 1小时
     */
    public static final Long HOUR_SECONDS = 1000 * 60L * 60;

    /**
     *  2分钟
     */
    public static final Long TWO_MINUTES_SECONDS = 60 * 2L;

}