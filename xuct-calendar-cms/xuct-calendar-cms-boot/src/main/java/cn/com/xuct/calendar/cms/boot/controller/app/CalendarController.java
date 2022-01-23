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
import cn.com.xuct.calendar.common.module.req.MemberCalendarUpdateReq;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.web.utils.JwtUtils;
import cn.com.xuct.calendar.cms.boot.service.IMemberCalendarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Api(tags = "【移动端】日历服务")
@RequestMapping("/api/app/v1/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final IMemberCalendarService memberCalendarService;

    @ApiOperation(value = "日历列表")
    @GetMapping("/list")
    public R<List<MemberCalendar>> list() {
        return R.data(memberCalendarService.queryMemberCalendar(JwtUtils.getUserId()));
    }

    @ApiOperation(value = "日历信息")
    @GetMapping
    public R<MemberCalendar> get(@RequestParam("id") Long id) {
        return R.data(memberCalendarService.getMemberCalendar(id));
    }

    @ApiOperation(value = "新增日历")
    @PostMapping("")
    public R<String> createMemberCalendar(@Validated @RequestBody MemberCalendarUpdateReq memberCalendarUpdateReq) {
        memberCalendarService.createMemberCalendar(JwtUtils.getUserId(), memberCalendarUpdateReq);
        return R.status(true);
    }

    @ApiOperation(value = "修改日历")
    @PutMapping("")
    public R<String> updateMemberCalendar(@Validated @RequestBody MemberCalendarUpdateReq memberCalendarUpdateReq) {
        MemberCalendar memberCalendar = memberCalendarService.getById(memberCalendarUpdateReq.getId());
        if (memberCalendar == null) return R.fail("未找到日历");
        if (!JwtUtils.getUserId().toString().equals(memberCalendar.getMemberId().toString())) return R.fail("无权限修改");
        memberCalendarService.updateMemberCalendar(JwtUtils.getUserId(), memberCalendar, memberCalendarUpdateReq);
        return R.status(true);
    }
}