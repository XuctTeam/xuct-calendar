/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberRegisterEvent
 * Author:   Derek Xu
 * Date:     2022/3/24 9:27
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
 * @create 2022/3/24
 * @since 1.0.0
 */
@Setter
@Getter
public class MemberRegisterEvent extends ApplicationEvent {

    private String userName;

    private Long memberId;

    public MemberRegisterEvent(Object source, String userName, Long memberId) {
        super(source);
        this.userName = userName;
        this.memberId = memberId;
    }
}