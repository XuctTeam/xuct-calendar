/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: TimeZoneCacheManager
 * Author:   Derek Xu
 * Date:     2022/1/7 11:14
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.config;

import com.google.common.collect.Maps;

import java.util.TimeZone;
import java.util.concurrent.ConcurrentMap;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/7
 * @since 1.0.0
 */
public class TimeZoneCacheManager {

    private static ConcurrentMap<String, TimeZone> timeZoneConcurrentMap = Maps.newConcurrentMap();

    private TimeZoneCacheManager() {
    }

    private static class SingletonHolder {
        private static TimeZoneCacheManager INSTANCE = new TimeZoneCacheManager();
    }

    public static TimeZoneCacheManager getInstance() {
        return TimeZoneCacheManager.SingletonHolder.INSTANCE;
    }


    public static void add(String timeZoneId, TimeZone timeZone) {
        timeZoneConcurrentMap.put(timeZoneId, timeZone);
    }

    public static TimeZone get(String timeZoneId) {
        return timeZoneConcurrentMap.get(timeZoneId);
    }
}