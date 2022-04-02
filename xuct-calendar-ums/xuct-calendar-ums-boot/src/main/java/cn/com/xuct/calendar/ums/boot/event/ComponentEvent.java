/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ComponentEvent
 * Author:   Derek Xu
 * Date:     2022/4/2 17:54
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.event;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/4/2
 * @since 1.0.0
 */
@Setter
@Getter
public class ComponentEvent extends ApplicationEvent {

    private String summary;

    private String startDate;

    private String location;

    private Long componentId;

    private Long createMemberId;

    private String createMemberName;

    private Integer repeat;

    private String triggerSec;


    public ComponentEvent(Object source, String summary, String startDate, String location, Long componentId, Long createMemberId, String createMemberName, Integer repeat, String triggerSec) {
        super(source);
        this.summary = summary;
        this.startDate = startDate;
        this.location = location;
        this.componentId = componentId;
        this.createMemberId = createMemberId;
        this.createMemberName = createMemberName;
        this.repeat = repeat;
        this.triggerSec = triggerSec;
    }
}