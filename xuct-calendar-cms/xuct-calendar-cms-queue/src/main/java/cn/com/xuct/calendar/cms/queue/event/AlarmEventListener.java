/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: AlarmEventListener
 * Author:   Derek Xu
 * Date:     2022/1/4 10:55
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.queue.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/4
 * @since 1.0.0
 */
@Slf4j
@Component
public class AlarmEventListener {

    @Async
    @EventListener(classes = AlarmEvent.class)
    public void onEvent(AlarmEvent alarmEvent) {
        log.info("alarm event:: get message = {}", alarmEvent.toString());
    }
}