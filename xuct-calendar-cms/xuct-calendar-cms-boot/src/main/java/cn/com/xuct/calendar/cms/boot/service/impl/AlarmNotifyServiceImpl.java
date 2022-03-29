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
import cn.com.xuct.calendar.cms.boot.service.IAlarmNotifyService;
import cn.com.xuct.calendar.cms.boot.service.IComponentAttendService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/29
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class AlarmNotifyServiceImpl implements IAlarmNotifyService {


    private final IComponentAttendService componentAttendService;

    @Async("taskExecutor")
    @Override
    public void noRepeatAlarmNotify(Component component) {

    }
}