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
import cn.com.xuct.calendar.cms.api.vo.CalendarComponentVo;
import cn.com.xuct.calendar.cms.api.vo.ComponentAttendVo;
import cn.com.xuct.calendar.cms.boot.mapper.ComponentAttendMapper;
import cn.com.xuct.calendar.cms.boot.service.IComponentAttendService;
import cn.com.xuct.calendar.common.db.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/13
 * @since 1.0.0
 */
@Service
public class ComponentAttendServiceImpl extends BaseServiceImpl<ComponentAttendMapper, ComponentAttend> implements IComponentAttendService {

    @Override
    public List<Component> listByCalendarId(Long calendarId, Long start, Long end) {
        return ((ComponentAttendMapper) super.getBaseMapper()).listByCalendarId(calendarId, start, end);
    }

    @Override
    public List<CalendarComponentVo> searchWord(Long memberId, String word, Integer page, Integer limit) {
        return ((ComponentAttendMapper) super.getBaseMapper()).searchWord(memberId, word, page, limit);
    }

    @Override
    public List<Long> listByComponentIdNoMemberId(Long memberId, Long componentId) {
        return ((ComponentAttendMapper) super.getBaseMapper()).listByComponentIdNoMemberId(memberId, componentId);
    }

    @Override
    public void updateMemberAttendCalendarId(Long memberId, Long oldCalendarId, Long calendarId, Long componentId) {
        ((ComponentAttendMapper) super.getBaseMapper()).updateMemberAttendCalendarId(memberId, oldCalendarId, calendarId, componentId);
    }

    @Override
    public void batchUpdateAttendMemberCalendarId(Long componentId, Long calendarId, List<Long> memberIds) {
        ((ComponentAttendMapper) super.getBaseMapper()).batchUpdateAttendMemberCalendarId(componentId, calendarId, memberIds);
    }
}