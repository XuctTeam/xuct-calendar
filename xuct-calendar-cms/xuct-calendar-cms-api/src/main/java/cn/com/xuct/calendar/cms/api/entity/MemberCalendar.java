/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MemberCalendar
 * Author:   Derek Xu
 * Date:     2021/12/6 13:14
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.api.entity;

import cn.com.xuct.calendar.common.db.dao.base.SuperEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/6
 * @since 1.0.0
 */
@Data
@TableName("cms_member_calendar")
public class MemberCalendar extends SuperEntity<MemberCalendar> {

    private String name;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("member_id")
    private Long memberId;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("calendar_id")
    private Long calendarId;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("create_member_id")
    private Long createMemberId;

    @TableField("create_member_name")
    private String createMemberName;

    /* 组织ID */
    private String organization;

    /* 颜色 */
    private String color;

    /* 显示状态 0不显示 1显示 */
    private Integer display;

    /* 是否主日历 0不是 1是*/
    @TableField("major")
    private Integer major;

    private String description;

    /* 提醒方式 0不提醒  1 站内信 2 邮箱 3 公众号*/
    @TableField(exist = false)
    private int alarmType;

    /* 提醒时间 15 30 60 */
    @TableField(exist = false)
    private int alarmTime;

    /* 是否可分享 */
    @TableField(exist = false)
    private int isShare;

}