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

import cn.com.xuct.calendar.cms.api.dodo.MemberMarjoCalendarDo;
import cn.com.xuct.calendar.cms.api.entity.Calendar;
import cn.com.xuct.calendar.cms.api.entity.MemberCalendar;
import cn.com.xuct.calendar.cms.boot.mapper.ComponentMapper;
import cn.com.xuct.calendar.cms.boot.mapper.MemberCalendarMapper;
import cn.com.xuct.calendar.cms.boot.service.ICalendarService;
import cn.com.xuct.calendar.cms.boot.service.IComponentAttendService;
import cn.com.xuct.calendar.cms.boot.service.IMemberCalendarService;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.db.service.BaseServiceImpl;
import cn.com.xuct.calendar.common.module.params.MemberCalendarUpdateParam;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final ICalendarService calendarService;

    private final IComponentAttendService componentAttendService;

    private final ComponentMapper componentMapper;

    @Override
    public List<MemberCalendar> queryMemberCalendar(Long memberId) {
        return ((MemberCalendarMapper) super.getBaseMapper()).queryMemberCalendar(memberId);
    }

    @Override
    public List<MemberMarjoCalendarDo> queryMarjoCalendarIds(List<String> memberIds) {
        return ((MemberCalendarMapper) super.getBaseMapper()).queryMarjoCalendarIds(memberIds);
    }

    @Override
    public MemberCalendar getMemberCalendar(Long id) {
        return ((MemberCalendarMapper) super.getBaseMapper()).getMemberCalendar(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMemberCalendar(Long memberId, MemberCalendarUpdateParam memberCalendarUpdateParam, final boolean major) {
        Calendar calendar = new Calendar();
        calendar.setMemberId(memberId);
        calendar.setAlarmTime(Integer.parseInt(memberCalendarUpdateParam.getAlarmTime()));
        calendar.setAlarmType(Integer.parseInt(memberCalendarUpdateParam.getAlarmType()));
        calendarService.save(calendar);
        MemberCalendar memberCalendar = new MemberCalendar();
        memberCalendar.setName(memberCalendarUpdateParam.getName());
        memberCalendar.setMemberId(calendar.getMemberId());
        memberCalendar.setCalendarId(calendar.getId());
        memberCalendar.setCreateMemberId(calendar.getMemberId());
        memberCalendar.setDescription(memberCalendarUpdateParam.getDescription());
        memberCalendar.setCreateMemberName(memberCalendarUpdateParam.getCreateMemberName());
        memberCalendar.setColor(memberCalendarUpdateParam.getColor());
        memberCalendar.setDisplay(memberCalendarUpdateParam.getDisplay());
        memberCalendar.setMajor(major ? 1 : 0);
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
    public void updateMemberCalendar(Long memberId, MemberCalendar memberCalendar, MemberCalendarUpdateParam memberCalendarUpdateParam) {
        memberCalendar.setName(memberCalendarUpdateParam.getName());
        memberCalendar.setColor(memberCalendarUpdateParam.getColor());
        memberCalendar.setDescription(memberCalendarUpdateParam.getDescription());
        memberCalendar.setDisplay(memberCalendarUpdateParam.getDisplay());
        memberCalendar.setIsShare(memberCalendarUpdateParam.getIsShare());
        this.updateById(memberCalendar);
        if (memberId.toString().equals(memberCalendar.getCreateMemberId().toString())) {
            Calendar calendar = new Calendar();
            calendar.setId(memberCalendar.getCalendarId());
            calendar.setAlarmTime(Integer.parseInt(memberCalendarUpdateParam.getAlarmTime()));
            calendar.setAlarmType(Integer.parseInt(memberCalendarUpdateParam.getAlarmType()));
            calendar.setIsShare(memberCalendar.getIsShare());
            calendarService.updateById(calendar);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mergeMemberCalendar(Long fromMemberId, Long memberId) {
        List<MemberCalendar> memberCalendars = this.find(Column.of("member_id", fromMemberId));
        if (CollectionUtils.isEmpty(memberCalendars)){
            return;
        }
        /* 1. 更新非主日历到新ID下 */
        List<MemberCalendar> notMajorCalendars = memberCalendars.stream().filter(calendar -> calendar.getMajor() == 0).collect(Collectors.toList());
        Date updateTime = new Date();
        notMajorCalendars.forEach(calendar -> {
            calendar.setMemberId(memberId);
            calendar.setCreateMemberId(memberId);
            calendar.setUpdateTime(updateTime);
        });
        this.updateBatchById(notMajorCalendars);
        /*2. 更新邀请数据 */
        Optional<MemberCalendar> majorCalendarOpt = memberCalendars.stream().filter(calendar -> calendar.getMajor() == 1).findFirst();
        if (!majorCalendarOpt.isPresent()) {
            return;
        }
        MemberCalendar currentMajorCalendar = this.get(Lists.newArrayList(Column.of("member_id", memberId), Column.of("major", 1)));
        if (currentMajorCalendar == null){
            throw new RuntimeException("member calendar service:: get current major calendar is null , member id = ".concat(String.valueOf(memberId)));
        }
        /*3. 更新邀请日历*/
        componentAttendService.updateAttendCalendarId(majorCalendarOpt.get().getCalendarId(), currentMajorCalendar.getCalendarId());
        componentAttendService.updateAttendMarjoCalendarId(majorCalendarOpt.get().getCalendarId(), currentMajorCalendar.getCalendarId());
        /*4. 更新事件的日历 */
        componentMapper.updateCalendarIdByCalendarId(majorCalendarOpt.get().getCalendarId(), currentMajorCalendar.getCalendarId());
        /*5. 删除日历 */
        this.removeById(majorCalendarOpt.get().getId());
        calendarService.delete(Column.of("id", majorCalendarOpt.get().getCalendarId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCalendar(Long memberCalendarId, Long calendarId) {
        this.removeById(memberCalendarId);
        calendarService.removeById(calendarId);
    }
}