/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: ComponentAttendServiceImpl
 * Author:   Derek Xu
 * Date:     2022/3/13 16:49
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.service.impl;

import cn.com.xuct.calendar.cms.api.entity.Component;
import cn.com.xuct.calendar.cms.api.entity.ComponentAttend;
import cn.com.xuct.calendar.cms.api.vo.CalendarAttendCountVo;
import cn.com.xuct.calendar.cms.api.vo.CalendarComponentVo;
import cn.com.xuct.calendar.cms.api.vo.ComponentAttendVo;
import cn.com.xuct.calendar.cms.boot.mapper.ComponentAttendMapper;
import cn.com.xuct.calendar.cms.boot.service.IComponentAttendService;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.res.RetOps;
import cn.com.xuct.calendar.common.db.service.BaseServiceImpl;
import cn.com.xuct.calendar.common.module.feign.PersonInfo;
import cn.com.xuct.calendar.ums.oauth.client.MemberFeignClient;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/13
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class ComponentAttendServiceImpl extends BaseServiceImpl<ComponentAttendMapper, ComponentAttend> implements IComponentAttendService {

    private final MemberFeignClient memberFeignClient;

    @Override
    public List<Component> listByCalendarId(final Long calendarId, final Long start, final Long end) {
        return ((ComponentAttendMapper) super.getBaseMapper()).listByCalendarId(calendarId, start, end);
    }

    @Override
    public List<CalendarComponentVo> searchWord(final Long memberId, final String word, final Integer page, final Integer limit) {
        return ((ComponentAttendMapper) super.getBaseMapper()).searchWord(memberId, word, page, limit);
    }

    @Override
    public List<ComponentAttend> listByComponentIdNoMemberId(final Long memberId, final Long componentId) {
        return ((ComponentAttendMapper) super.getBaseMapper()).listByComponentIdNoMemberId(memberId, componentId);
    }

    @Override
    public void updateMemberAttendCalendarId(final Long memberId, final Long oldCalendarId, final Long calendarId, final Long componentId) {
        ((ComponentAttendMapper) super.getBaseMapper()).updateMemberAttendCalendarId(memberId, oldCalendarId, calendarId, componentId);
    }

    @Override
    public void batchUpdateAttendMemberCalendarId(final Long componentId, final Long calendarId, final List<Long> memberIds) {
        ((ComponentAttendMapper) super.getBaseMapper()).batchUpdateAttendMemberCalendarId(componentId, calendarId, memberIds);
    }

    @Override
    public void acceptAttend(final Long memberId, final Long calendarId, final Long attendCalendarId, final Long componentId) {
        ComponentAttend componentAttend = new ComponentAttend();
        componentAttend.setComponentId(componentId);
        componentAttend.setCalendarId(calendarId);
        componentAttend.setAttendCalendarId(attendCalendarId);
        componentAttend.setStatus(1);
        componentAttend.setMemberId(memberId);
        this.save(componentAttend);
    }

    @Override
    public void updateAttendMarjoCalendarId(final Long oldCalendarId, final Long newCalendarId) {
        ((ComponentAttendMapper) super.getBaseMapper()).updateAttendMarjoCalendarId(oldCalendarId, newCalendarId);
    }

    @Override
    public void updateAttendCalendarId(final Long oldCalendarId, final Long newCalendarId) {
        ((ComponentAttendMapper) super.getBaseMapper()).updateAttendCalendarId(oldCalendarId, newCalendarId);
    }

    @Override
    public CalendarAttendCountVo statistics(final Long componentId) {
        return ((ComponentAttendMapper) super.getBaseMapper()).statistics(componentId);
    }

    @Override
    public List<ComponentAttendVo> listByComponentId(Long componentId, Long creatorMemberId) {
        List<ComponentAttend> members = this.listByComponentIdNoMemberId(creatorMemberId, componentId);
        if (CollectionUtils.isEmpty(members)) return Lists.newArrayList();
        R<List<PersonInfo>> memberInfoResult = memberFeignClient.listMemberByIds(members.stream().map(ComponentAttend::getMemberId).collect(Collectors.toList()));
        List<PersonInfo> personInfos = RetOps.of(memberInfoResult).getData().orElse(Lists.newArrayList());
        if (CollectionUtils.isEmpty(personInfos)) return Lists.newArrayList();
        Map<Long, ComponentAttend> attendMap = members.stream().collect(Collectors.toMap(ComponentAttend::getMemberId, attend -> attend, (oldValue, newValue) -> newValue));
        return memberInfoResult.getData().stream().map(info -> {
            ComponentAttendVo attendVo = new ComponentAttendVo();
            attendVo.setAvatar(info.getAvatar());
            attendVo.setMemberId(String.valueOf(info.getUserId()));
            attendVo.setName(info.getName());
            attendVo.setStatus(attendMap.get(Long.valueOf(attendVo.getMemberId())).getStatus());
            return attendVo;
        }).collect(Collectors.toList());
    }
}