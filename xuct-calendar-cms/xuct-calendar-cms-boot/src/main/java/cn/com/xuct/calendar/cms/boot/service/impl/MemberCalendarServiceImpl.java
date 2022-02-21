/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MemberCalendarServiceImpl
 * Author:   Derek Xu
 * Date:     2021/12/6 13:31
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.service.impl;

import cn.com.xuct.calendar.cms.api.entity.Calendar;
import cn.com.xuct.calendar.cms.api.entity.MemberCalendar;
import cn.com.xuct.calendar.cms.boot.mapper.MemberCalendarMapper;
import cn.com.xuct.calendar.cms.boot.service.ICalendarService;
import cn.com.xuct.calendar.cms.boot.service.IMemberCalendarService;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.req.MemberCalendarUpdateReq;
import cn.com.xuct.calendar.service.base.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/6
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberCalendarServiceImpl extends BaseServiceImpl<MemberCalendarMapper, MemberCalendar> implements IMemberCalendarService {

    private final MemberCalendarMapper memberCalendarMapper;

    private final ICalendarService calendarService;

    @Override
    public List<MemberCalendar> queryMemberCalendar(Long memberId) {
        return memberCalendarMapper.queryMemberCalendar(memberId);
    }

    @Override
    public MemberCalendar getMemberCalendar(Long id) {
        return memberCalendarMapper.getMemberCalendar(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMemberCalendar(Long memberId, MemberCalendarUpdateReq memberCalendarUpdateReq) {
        Calendar calendar = new Calendar();
        calendar.setMemberId(memberId);
        calendar.setAlarmTime(Integer.parseInt(memberCalendarUpdateReq.getAlarmTime()));
        calendar.setAlarmType(Integer.parseInt(memberCalendarUpdateReq.getAlarmType()));
        calendarService.save(calendar);
        MemberCalendar memberCalendar = new MemberCalendar();
        memberCalendar.setName(memberCalendarUpdateReq.getName());
        memberCalendar.setMemberId(calendar.getMemberId());
        memberCalendar.setCalendarId(calendar.getId());
        memberCalendar.setCreateMemberId(calendar.getMemberId());
        memberCalendar.setCreateMemberName(memberCalendarUpdateReq.getCreateMemberName());
        memberCalendar.setColor(memberCalendarUpdateReq.getColor());
        memberCalendar.setDisplay(memberCalendarUpdateReq.getDisplay());
        memberCalendar.setMajor(1);
        this.save(memberCalendar);
    }

    @Override
    public void updateMemberCalendarName(Long createMemberId, String createMemberName) {
        MemberCalendar updateMemberCalendar = new MemberCalendar();
        updateMemberCalendar.setCreateTime(null);
        updateMemberCalendar.setUpdateTime(null);
        updateMemberCalendar.setCreateMemberName(createMemberName);
        this.update(updateMemberCalendar, Column.of("create_member_id", createMemberId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMemberCalendar(Long memberId, MemberCalendar memberCalendar, MemberCalendarUpdateReq memberCalendarUpdateReq) {
        memberCalendar.setName(memberCalendarUpdateReq.getName());
        memberCalendar.setColor(memberCalendarUpdateReq.getColor());
        memberCalendar.setDescription(memberCalendarUpdateReq.getDescription());
        memberCalendar.setDisplay(memberCalendarUpdateReq.getDisplay());
        memberCalendar.setIsShare(memberCalendarUpdateReq.getIsShare());
        this.updateById(memberCalendar);
        if (memberId.toString().equals(memberCalendar.getCreateMemberId().toString())) {
            Calendar calendar = new Calendar();
            calendar.setId(memberCalendar.getCalendarId());
            calendar.setAlarmTime(Integer.parseInt(memberCalendarUpdateReq.getAlarmTime()));
            calendar.setAlarmType(Integer.parseInt(memberCalendarUpdateReq.getAlarmType()));
            calendar.setIsShare(memberCalendar.getIsShare());
            calendarService.updateById(calendar);
        }
    }
}