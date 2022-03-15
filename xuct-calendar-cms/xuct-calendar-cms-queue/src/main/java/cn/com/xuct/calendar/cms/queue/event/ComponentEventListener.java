/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ComponentEventListener
 * Author:   Derek Xu
 * Date:     2022/3/15 10:09
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
 * @create 2022/3/15
 * @since 1.0.0
 */
@Slf4j
@Component
public class ComponentEventListener {

    /**
     * 功能描述: <br>
     * 〈事件被删除〉
     *
     * @param delEvent
     * @return:void
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/3/15 10:21
     */
    @Async
    @EventListener(classes = ComponentDelEvent.class)
    public void listenerComponentDelEvent(ComponentDelEvent delEvent) {

    }

    /**
     * 功能描述: <br>
     * 〈提醒事件〉
     *
     * @param alarmEvent
     * @return:void
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/3/15 10:30
     */
    @Async
    @EventListener(classes = AlarmEvent.class)
    public void onEvent(AlarmEvent alarmEvent) {
        log.info("alarm event....");
        //cn.com.xuct.calendar.cms.api.entity.Component = componentService.getById(alarmEvent.getMessage())
    }
}