/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: AlarmDelayedHandler
 * Author:   Administrator
 * Date:     2022/1/3 20:02
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.queue.handler;

import cn.com.xuct.calendar.cms.queue.event.AlarmEventPublisher;
import cn.com.xuct.calendar.common.module.dto.RabbitmqMessageBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/3
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmDelayedHandler {


    private final AlarmEventPublisher alarmEventPublisher;

    // 消费延迟消息
    @Bean
    public Consumer<RabbitmqMessageBody> alarmDelayed() {  // 方法名必须与生产消息时自定义的绑定名称一致
        return message -> {
            log.info("接收延迟消息：{}", message.getPayload());
            alarmEventPublisher.publish(message.getPayload());
        };
    }
}