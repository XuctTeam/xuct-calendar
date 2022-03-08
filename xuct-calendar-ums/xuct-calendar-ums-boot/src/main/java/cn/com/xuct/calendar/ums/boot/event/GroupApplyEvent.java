/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: GroupApplyEvent
 * Author:   Derek Xu
 * Date:     2022/3/8 17:40
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

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/8
 * @since 1.0.0
 */
@Setter
@Getter
public class GroupApplyEvent extends ApplicationEvent {

    /* 申请用户 */
    private Long applyMemberId;

    private Long groupId;

    private String groupName;

    private Long groupCreateMemberId;


    public GroupApplyEvent(Object source, Long applyMemberId, Long groupId, String groupName, Long groupCreateMemberId) {
        super(source);
        this.applyMemberId = applyMemberId;
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupCreateMemberId = groupCreateMemberId;
    }
}