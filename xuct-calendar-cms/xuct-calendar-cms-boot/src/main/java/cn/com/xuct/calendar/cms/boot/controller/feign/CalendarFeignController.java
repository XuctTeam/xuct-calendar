/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: CalendarFeginController
 * Author:   Derek Xu
 * Date:     2021/12/2 14:58
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.controller.feign;

import cn.com.xuct.calendar.cms.boot.config.DictCacheManager;
import cn.com.xuct.calendar.common.core.constant.DictConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.CalendarInitFeignInfoReq;
import cn.com.xuct.calendar.common.module.dto.CalendarMergeDto;
import cn.com.xuct.calendar.common.module.req.MemberCalendarUpdateReq;
import cn.com.xuct.calendar.cms.boot.service.IMemberCalendarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/2
 * @since 1.0.0
 */
@Api(tags = "【远程调用】日历服务")
@RestController
@RequestMapping("/api/feign/v1/calendar")
@RequiredArgsConstructor
public class CalendarFeignController {

    private final IMemberCalendarService memberCalendarService;


    @ApiOperation(value = "新建主日历")
    @PostMapping
    @ApiImplicitParam(value = "实体JSON对象", required = true, paramType = "body", dataType = "CalendarInitDto")
    public R<String> createCalendar(@RequestBody CalendarInitFeignInfoReq calendarInitFeignInfoReq) {
        MemberCalendarUpdateReq updateReq = new MemberCalendarUpdateReq();
        updateReq.setName("日程");
        updateReq.setColor(DictCacheManager.getDictByCode(DictConstants.COLOR_TYPE, DictConstants.RED_COLOR_CODE).getValue());
        updateReq.setAlarmTime("0");
        updateReq.setAlarmType("0");
        updateReq.setCreateMemberName(calendarInitFeignInfoReq.getMemberNickName());
        updateReq.setDisplay(1);
        memberCalendarService.createMemberCalendar(calendarInitFeignInfoReq.getMemberId(), updateReq);
        return R.status(true);
    }

    @ApiOperation(value = "更新日历创建用户名称")
    @PostMapping("/modify/name")
    @ApiImplicitParam(value = "实体JSON对象", required = true, paramType = "body", dataType = "CalendarInitDto")
    public R<String> updateMemberCalendarName(@RequestBody CalendarInitFeignInfoReq calendarInitFeignInfoReq) {
        memberCalendarService.updateMemberCalendarName(calendarInitFeignInfoReq.getMemberId(), calendarInitFeignInfoReq.getMemberNickName());
        return R.status(true);
    }


    @ApiOperation(value = "日历合并")
    @PostMapping("/merge")
    @ApiImplicitParam(value = "实体JSON对象", required = true, paramType = "body", dataType = "CalendarMergeDto")
    public R<String> mergeCalendar(@RequestBody CalendarMergeDto calendarMergeDto) {
        //memberCalendarService.mergeMemberCalendar(calendarMergeDto.getMemberId(), calendarMergeDto.getFromMemberId());
        return R.status(true);
    }


}