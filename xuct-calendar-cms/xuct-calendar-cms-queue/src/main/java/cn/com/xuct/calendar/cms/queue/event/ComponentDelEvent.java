/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ComponentDelEvent
 * Author:   Derek Xu
 * Date:     2022/3/15 10:09
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.queue.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/15
 * @since 1.0.0
 */
@Setter
@Getter
public class ComponentDelEvent extends ApplicationEvent {


    private Long componentId;

    private String summary;

    private String startDate;

    private Long createMemberId;

    private String location;

    private String repeat;

    private String triggerSec;

    private List<Long> ids;

    public ComponentDelEvent(Object source, Long componentId, String summary, String startDate, Long createMemberId, String location, String repeat, String triggerSec, List<Long> ids) {
        super(source);
        this.componentId = componentId;
        this.summary = summary;
        this.startDate = startDate;
        this.createMemberId = createMemberId;
        this.location = location;
        this.repeat = repeat;
        this.triggerSec = triggerSec;
        this.ids = ids;
    }
}