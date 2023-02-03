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
import cn.com.xuct.calendar.cms.api.feign.BasicServicesFeignClient;
import cn.com.xuct.calendar.cms.api.vo.CalendarSharedVo;
import cn.com.xuct.calendar.cms.boot.config.DomainConfiguration;
import cn.com.xuct.calendar.cms.boot.mapper.ComponentMapper;
import cn.com.xuct.calendar.cms.boot.mapper.MemberCalendarMapper;
import cn.com.xuct.calendar.cms.boot.service.ICalendarService;
import cn.com.xuct.calendar.cms.boot.service.IComponentAttendService;
import cn.com.xuct.calendar.cms.boot.service.IMemberCalendarService;
import cn.com.xuct.calendar.cms.boot.utils.CmsConstant;
import cn.com.xuct.calendar.common.core.exception.SvrException;
import cn.com.xuct.calendar.common.core.res.RetOps;
import cn.com.xuct.calendar.common.core.res.SvrResCode;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.db.service.BaseServiceImpl;
import cn.com.xuct.calendar.common.module.feign.req.ShortChainFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.WxQrCodeInfo;
import cn.com.xuct.calendar.common.module.params.MemberCalendarUpdateParam;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final BasicServicesFeignClient basicServicesFeignClient;
    private final DomainConfiguration domainConfiguration;
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
    public void updateDisplayStatus(final Long calendarId, final Integer display) {
        MemberCalendar memberCalendar = this.getById(calendarId);
        if (memberCalendar == null) {
            throw new SvrException(SvrResCode.CMS_CALENDAR_NOT_FOUND);
        }
        memberCalendar.setDisplay(display);
        this.updateById(memberCalendar);
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
    public void mergeMemberCalendar(Long fromMemberId, Long memberId, final List<MemberCalendar> memberCalendars) {
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
        MemberCalendar memberCalendar = majorCalendarOpt.get();
        MemberCalendar currentMajorCalendar = this.get(Lists.newArrayList(Column.of("member_id", memberId), Column.of("major", 1)));
        if (currentMajorCalendar == null) {
            throw new RuntimeException("member calendar service:: get current major calendar is null , member id = ".concat(String.valueOf(memberId)));
        }
        /*3. 更新邀请日历*/
        componentAttendService.updateAttendCalendarId(memberCalendar.getCalendarId(), currentMajorCalendar.getCalendarId());
        componentAttendService.updateAttendMarjoCalendarId(memberCalendar.getCalendarId(), currentMajorCalendar.getCalendarId());
        /*4. 更新事件的日历 */
        componentMapper.updateCalendarIdByCalendarId(memberCalendar.getCalendarId(), currentMajorCalendar.getCalendarId());
        /*5. 删除日历 */
        this.removeById(majorCalendarOpt.get().getId());
        calendarService.delete(Column.of("id", majorCalendarOpt.get().getCalendarId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCalendar(final Long memberId, Long calendarId) {
        MemberCalendar memberCalendar = this.get(Lists.newArrayList(Column.of("member_id", memberId), Column.of("calendar_id", calendarId)));
        if (memberCalendar == null) {
            throw new SvrException(SvrResCode.CMS_CALENDAR_NOT_FOUND);
        }
        if (memberCalendar.getMajor() == 1) {
            throw new SvrException(SvrResCode.CMS_CALENDAR_MAJOR_CALENDAR_ENABLE_DELETE);
        }
        /* 不是自己创建创建 则删除对应关系 */
        if (!memberCalendar.getCreateMemberId().toString().equals(memberId.toString())) {
            this.removeById(memberCalendar.getId());
            return;
        }
        /* 是自己日历，则查询日历下事件 */
        Long existComponentNumber = componentMapper.countByCalendarId(calendarId);
        if (existComponentNumber > 0) {
            throw new SvrException(SvrResCode.CMS_COMPONENT_HAS_EVENT);
        }
        this.removeById(memberCalendar.getId());
        calendarService.removeById(calendarId);
    }

    @Override
    public CalendarSharedVo getCalendarShared(final Long memberId, final Long calendarId) {
        Calendar calendar = calendarService.getById(calendarId);
        if (calendar == null) {
            throw new SvrException(SvrResCode.CMS_CALENDAR_NOT_FOUND);
        }
        MemberCalendar memberCalendar = this.get(Lists.newArrayList(Column.of("calendar_id", calendarId), Column.of("member_id", memberId)));
        if (memberCalendar == null) {
            throw new SvrException(SvrResCode.CMS_CALENDAR_NOT_FOUND);
        }
        CalendarSharedVo calendarSharedVo = new CalendarSharedVo();

        Optional<DomainConfiguration.Short> optionalShort = domainConfiguration.getShortDomains().stream().filter(x -> CmsConstant.ShortDomain.CALENDAR.equals(x.getType())).findAny();
        if (!optionalShort.isPresent()) {
            throw new SvrException(SvrResCode.CMS_SERVER_ERROR);
        }
        String domain = RetOps.of(basicServicesFeignClient.shortChain(ShortChainFeignInfo.builder()
                .url(optionalShort.get().getDomain().concat("?calendarId=" + calendarId))
                .type(CmsConstant.ShortDomain.CALENDAR).expire(7200000L).build())).getData().orElseThrow();

        String qrCode =  RetOps.of(basicServicesFeignClient.getMaQrCode(WxQrCodeInfo.builder().scene("1").page("/pages/index").envVersion("123").width(200).build())).getData().orElse(null);

        calendarSharedVo.setShortUrl(domain);
        calendarSharedVo.setId(calendarId);
        calendarSharedVo.setName(memberCalendar.getName());
        return calendarSharedVo;
    }
}