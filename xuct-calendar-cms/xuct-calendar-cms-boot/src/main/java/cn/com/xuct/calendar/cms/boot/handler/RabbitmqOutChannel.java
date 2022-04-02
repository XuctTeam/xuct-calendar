/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: RabbitmqOutChannel
 * Author:   Administrator
 * Date:     2022/1/3 19:51
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.handler;

import cn.com.xuct.calendar.cms.boot.config.RabbitmqSource;
import cn.com.xuct.calendar.common.module.dto.RabbitmqMessageBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Date;

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
public class RabbitmqOutChannel {

    private final StreamBridge streamBridge;

    /**
     * 功能描述: <br>
     * 〈发送日程提醒〉
     *
     * @param type
     * @param payload
     * @param delay
     * @return:void
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/1/4 10:30
     */
    public void pushAlarmDelayedMessage(String type, String payload, long delay) {
        log.debug("rabbitmq channel:: type = {} , delay = {}", type, delay);
        RabbitmqMessageBody body = new RabbitmqMessageBody();
        body.setType(type);
        body.setPayload(payload);
        streamBridge.send(RabbitmqSource.ALARM_CHANNEL_OUTPUT,
                MessageBuilder.withPayload(body).setHeader("x-delay", delay).build());
    }
}