/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: AlarmEventPublisher
 * Author:   Derek Xu
 * Date:     2022/1/4 10:57
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.queue.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/4
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class AlarmEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publish(String message) {
        AlarmEvent springEvent = new AlarmEvent(message);
        publisher.publishEvent(springEvent);
    }
}