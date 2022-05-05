/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberModifyNameEvent
 * Author:   Derek Xu
 * Date:     2022/3/14 11:15
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
 * @create 2022/3/14
 * @since 1.0.0
 */
@Setter
@Getter
public class MemberEvent extends ApplicationEvent {

    private Long memberId;

    private String name;

    private Integer type;

    public MemberEvent(Object source, Long memberId, String name, Integer type) {
        super(source);
        this.memberId = memberId;
        this.name = name;
        this.type = type;
    }
}