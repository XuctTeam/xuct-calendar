/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: AlarmNotifyEvent
 * Author:   Derek Xu
 * Date:     2022/3/30 11:03
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/30
 * @since 1.0.0
 */
@Setter
@Getter
public class AlarmNotifyEvent extends ComponentEvent {


    private List<Long> ids;


    public AlarmNotifyEvent(Object source, String summary, String startDate, String location, Long componentId, Long createMemberId, String createMemberName, Integer repeat, String triggerSec, List<Long> ids) {
        super(source, summary, startDate, location, componentId, createMemberId, createMemberName, repeat, triggerSec);
        this.ids = ids;
    }
}