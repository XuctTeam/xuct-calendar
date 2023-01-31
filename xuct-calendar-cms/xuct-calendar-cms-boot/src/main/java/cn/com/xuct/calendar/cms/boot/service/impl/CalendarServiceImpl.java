/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: CalendarServiceImpl
 * Author:   Derek Xu
 * Date:     2021/11/24 15:55
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.service.impl;

import cn.com.xuct.calendar.cms.api.entity.Calendar;
import cn.com.xuct.calendar.cms.api.feign.BasicServicesFeignClient;
import cn.com.xuct.calendar.cms.api.vo.CalendarSharedVo;
import cn.com.xuct.calendar.cms.boot.config.DomainConfiguration;
import cn.com.xuct.calendar.cms.boot.mapper.CalendarMapper;
import cn.com.xuct.calendar.cms.boot.service.ICalendarService;
import cn.com.xuct.calendar.cms.boot.utils.CmsConstant;
import cn.com.xuct.calendar.common.core.exception.SvrException;
import cn.com.xuct.calendar.common.core.res.RetOps;
import cn.com.xuct.calendar.common.core.res.SvrResCode;
import cn.com.xuct.calendar.common.db.service.BaseServiceImpl;
import cn.com.xuct.calendar.common.module.feign.req.ShortChainFeignInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/24
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class CalendarServiceImpl extends BaseServiceImpl<CalendarMapper, Calendar> implements ICalendarService {

    private final BasicServicesFeignClient basicServicesFeignClient;

    private final DomainConfiguration domainConfiguration;

    @Override
    public CalendarSharedVo getCalendarShared(Long calendarId) {
        Calendar calendar = this.getById(calendarId);
        if (calendar == null) {
            throw new SvrException(SvrResCode.CMS_CALENDAR_NOT_FOUND);
        }
        CalendarSharedVo calendarSharedVo = new CalendarSharedVo();

        Optional<DomainConfiguration.Short> optionalShort = domainConfiguration.getShortList().stream().filter(x -> CmsConstant.ShortDomain.CALENDAR.equals(x.getType())).findAny();
        if (!optionalShort.isPresent()) {
            throw new SvrException(SvrResCode.CMS_SERVER_ERROR);
        }
        String domain = RetOps.of(basicServicesFeignClient.shortChain(ShortChainFeignInfo.builder()
                .url(optionalShort.get().getDomain().concat("?calendarId=" + calendarId))
                .type(CmsConstant.ShortDomain.CALENDAR).expire(7200000L).build())).getData().orElse(null);

        calendarSharedVo.setId(calendarId);
        //calendarSharedVo.setName(calendar.get);
        return calendarSharedVo;
    }
}