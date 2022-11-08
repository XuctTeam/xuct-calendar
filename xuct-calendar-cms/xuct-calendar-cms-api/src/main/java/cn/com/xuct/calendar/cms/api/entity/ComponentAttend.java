/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: ComponentAttend
 * Author:   Derek Xu
 * Date:     2022/3/13 13:02
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.api.entity;

import cn.com.xuct.calendar.common.db.dao.base.SuperEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈事件邀请人〉
 *
 * @author Derek Xu
 * @create 2022/3/13
 * @since 1.0.0
 */
@Data
@TableName("cms_component_attend")
public class ComponentAttend extends SuperEntity<ComponentAttend> {

    /* 创建者日历ID */
    @TableField("calendar_id")
    private Long calendarId;

    /* 邀请者日历ID */
    @TableField("attend_calendar_id")
    private Long attendCalendarId;

    @TableField("component_id")
    private Long componentId;

    @TableField("member_id")
    private Long memberId;

    /*0待定 1接受 */
    private Integer status;



}