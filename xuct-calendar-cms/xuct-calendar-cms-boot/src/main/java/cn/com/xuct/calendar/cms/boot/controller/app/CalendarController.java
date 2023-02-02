/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: CalendarController
 * Author:   Derek Xu
 * Date:     2021/11/24 15:48
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.controller.app;

import cn.com.xuct.calendar.cms.api.entity.MemberCalendar;
import cn.com.xuct.calendar.cms.api.vo.CalendarSharedVo;
import cn.com.xuct.calendar.cms.boot.service.ICalendarService;
import cn.com.xuct.calendar.cms.boot.service.IMemberCalendarService;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.params.CalendarUpdateDisplayStatusParam;
import cn.com.xuct.calendar.common.module.params.MemberCalendarUpdateParam;
import cn.com.xuct.calendar.common.security.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/24
 * @since 1.0.0
 */
@Slf4j
@RestController
@Tag(name = "【移动端】日历服务")
@RequestMapping("/api/app/v1/calendar")
@RequiredArgsConstructor
public class CalendarController {


    private final IMemberCalendarService memberCalendarService;


    @Operation(summary = "日历列表")
    @GetMapping("/list")
    public R<List<MemberCalendar>> list() {
        return R.data(memberCalendarService.queryMemberCalendar(SecurityUtils.getUserId()));
    }

    @Operation(summary = "日历信息")
    @GetMapping
    public R<MemberCalendar> get(@RequestParam("id") Long id) {
        return R.data(memberCalendarService.getMemberCalendar(id));
    }

    @Operation(summary = "新增日历")
    @PostMapping("")
    public R<String> createMemberCalendar(@Validated @RequestBody MemberCalendarUpdateParam memberCalendarUpdateParam) {
        memberCalendarService.createMemberCalendar(SecurityUtils.getUserId(), memberCalendarUpdateParam, false);
        return R.status(true);
    }

    @Operation(summary = "修改日历")
    @PutMapping("")
    public R<String> updateMemberCalendar(@Validated @RequestBody MemberCalendarUpdateParam memberCalendarUpdateParam) {
        MemberCalendar memberCalendar = memberCalendarService.getById(memberCalendarUpdateParam.getId());
        Assert.notNull(memberCalendar, "未找到日历");
        if (!SecurityUtils.getUserId().toString().equals(memberCalendar.getMemberId().toString())) {
            return R.fail("无权限修改");
        }
        memberCalendarService.updateMemberCalendar(SecurityUtils.getUserId(), memberCalendar, memberCalendarUpdateParam);
        return R.status(true);
    }

    @Operation(summary = "更新日历显示状态")
    @PostMapping("/display/status")
    public R<String> updateDisplayStatus(@Validated @RequestBody CalendarUpdateDisplayStatusParam params) {
        memberCalendarService.updateDisplayStatus(Long.valueOf(params.getCalendarId()), params.getDisplay());
        return R.status(true);
    }

    @Operation(summary = "删除日历")
    @DeleteMapping
    public R<String> delete(@RequestParam("calendarId") Long calendarId) {
        memberCalendarService.deleteCalendar(SecurityUtils.getUserId() , calendarId);
        return R.status(true);
    }

    @Operation(summary = "日历分享")
    @GetMapping("/shared")
    public R<CalendarSharedVo> sharedInfo(@RequestParam("calendarId") Long calendarId) {
        return R.data(memberCalendarService.getCalendarShared(SecurityUtils.getUserId() , Long.valueOf(calendarId)));
    }
}