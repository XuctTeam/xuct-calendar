/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: IAlarmNotifyService
 * Author:   Derek Xu
 * Date:     2022/3/29 15:45
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.service;

import cn.com.xuct.calendar.cms.api.entity.Component;

/**
 * 〈一句话功能简述〉<br>
 * 〈异步通知用户提醒〉
 *
 * @author Derek Xu
 * @create 2022/3/29
 * @since 1.0.0
 */
public interface IAlarmNotifyService {


    /**
     * 异步通知用户
     *
     * @param component
     */
    void timerOverAlarmNotify(Component component);


    /**
     * 异步处理非重复事件下次提醒时间
     *
     * @param component
     * @param triggerSec
     */
    void noRepeatAlarmPushToQueue(Component component, Long alarmId, Integer triggerSec);


    /**
     * 异步处理重复事件下次提醒时间
     *
     * @param component
     * @param alarmId
     * @param triggerSec
     */
    void repeatAlarmPushToQueue(Component component, Long alarmId, Integer triggerSec);
}