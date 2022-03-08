/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: GroupApplyOptionEvent
 * Author:   Derek Xu
 * Date:     2022/3/8 18:02
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
public class GroupApplyOptionEvent extends ApplicationEvent {

    private Long memberId;

    private Integer operate;

    private Long groupId;

    public GroupApplyOptionEvent(Object source, Long groupId, Long memberId, Integer operate) {
        super(source);
        this.memberId = memberId;
        this.operate = operate;
        this.groupId = groupId;
    }
}