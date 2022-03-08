/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: GroupLeaveEvent
 * Author:   Derek Xu
 * Date:     2022/3/8 20:58
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/8
 * @since 1.0.0
 */
@Getter
@Setter
public class GroupLeaveEvent extends ApplicationEvent {

    private Long groupId;

    private String name;

    private Long memberId;

    private Integer operate;

    public GroupLeaveEvent(Object source, Long groupId, String name, Long memberId, Integer operate) {
        super(source);
        this.groupId = groupId;
        this.name = name;
        this.memberId = memberId;
        this.operate = operate;
    }
}