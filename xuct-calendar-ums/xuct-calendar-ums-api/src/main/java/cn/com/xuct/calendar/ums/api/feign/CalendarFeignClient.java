/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: DavFeignClient
 * Author:   Derek Xu
 * Date:     2021/11/24 15:06
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.api.feign;


import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.dto.CalendarMergeDto;
import cn.com.xuct.calendar.common.module.feign.req.CalendarCountFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.CalendarInitFeignInfo;
import cn.com.xuct.calendar.common.web.web.FeignConfiguration;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/24
 * @since 1.0.0
 */
@FeignClient(name = "dav-cms", contextId = "cms", configuration = FeignConfiguration.class)
public interface CalendarFeignClient {

    /**
     * 新增日历
     *
     * @param calendarInitFeignInfo
     * @return
     */
    @PostMapping("/api/feign/v1/calendar")
    R<String> addCalendar(@RequestBody CalendarInitFeignInfo calendarInitFeignInfo);

    /**
     * 更新用户日历的显示名称
     *
     * @param calendarInitFeignInfo
     * @return
     */
    @PostMapping("/api/feign/v1/calendar/modify/name")
    R<String> updateMemberCalendarName(@RequestBody CalendarInitFeignInfo calendarInitFeignInfo);

    /**
     * 查询账号下日历的总数
     *
     * @param calendarCountFeignInfo
     * @return
     */
    @PostMapping("/api/feign/v1/calendar/member/ids/count")
    R<Long> countCalendarNumberByMemberIds(@RequestBody CalendarCountFeignInfo calendarCountFeignInfo);

    /**
     * 合并日历
     *
     * @param calendarMergeDto
     * @return
     */
    @PostMapping("/api/feign/v1/calendar/merge")
    R<String> mergeCalendar(@RequestBody CalendarMergeDto calendarMergeDto);
}

