/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: GroupEvent
 * Author:   Derek Xu
 * Date:     2022/3/5 17:59
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/5
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class GroupDeleteEvent extends ApplicationEvent {

    private String groupName;

    private Long groupId;

    private Long createMemberId;

    private List<Long> memberIds;

    public GroupDeleteEvent(Object source) {
        super(source);
    }

    public GroupDeleteEvent(Object source, String groupName, Long groupId, Long createMemberId, List<Long> memberIds) {
        super(source);
        this.groupName = groupName;
        this.groupId = groupId;
        this.createMemberId = createMemberId;
        this.memberIds = memberIds;
    }
}