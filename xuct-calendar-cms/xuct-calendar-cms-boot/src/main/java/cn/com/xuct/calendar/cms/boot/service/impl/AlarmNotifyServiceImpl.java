/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: AlarmNotifyServiceImpl
 * Author:   Derek Xu
 * Date:     2022/3/29 15:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.service.impl;

import cn.com.xuct.calendar.cms.api.entity.Component;
import cn.com.xuct.calendar.cms.api.entity.ComponentAttend;
import cn.com.xuct.calendar.cms.boot.service.IAlarmNotifyService;
import cn.com.xuct.calendar.cms.boot.service.IComponentAttendService;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.enums.ComponentAlarmEnum;
import cn.com.xuct.calendar.common.module.enums.ComponentRepeatTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/29
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmNotifyServiceImpl implements IAlarmNotifyService {


    private final IComponentAttendService componentAttendService;

    @Async("taskExecutor")
    @Override
    public void noRepeatAlarmNotify(Component component) {
        List<ComponentAttend> componentAttendList = componentAttendService.find(Column.of("component_id", component.getId()));
        if (CollectionUtils.isEmpty(componentAttendList)) {
            log.error("alarm notify service::component attend list empty , calendar id = {} , component id = {}", component.getCalendarId(), component.getId());
            return;
        }




    }
}