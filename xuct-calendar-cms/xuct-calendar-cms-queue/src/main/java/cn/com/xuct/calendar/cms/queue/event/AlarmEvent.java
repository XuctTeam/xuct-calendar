/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: IComponentAlarmEvent
 * Author:   Derek Xu
 * Date:     2022/1/4 10:54
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.queue.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/4
 * @since 1.0.0
 */
@Slf4j
public class AlarmEvent extends ApplicationEvent {

    /**
     * 接受信息
     */
    private String message;

    public AlarmEvent(String message) {
        super(message);
        this.message = message;
        log.info("add event success! message: {}", message);
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "AlarmEvent{" +
                "message='" + message + '\'' +
                ", source=" + source +
                '}';
    }
}