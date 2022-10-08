/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: Component
 * Author:   Derek Xu
 * Date:     2021/12/21 13:11
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.api.entity;

import cn.com.xuct.calendar.common.db.dao.base.SuperEntity;
import cn.com.xuct.calendar.common.module.enums.CommonPowerEnum;
import cn.com.xuct.calendar.common.module.enums.CommonStatusEnum;
import cn.com.xuct.calendar.common.module.enums.ComponentAlarmEnum;
import cn.com.xuct.calendar.common.module.enums.ComponentRepeatTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/21
 * @since 1.0.0
 */
@Data
@TableName("cms_component")
public class Component extends SuperEntity<Component> {

    @Schema(name = "日历ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("calendar_id")
    private Long calendarId;

    @Schema(name ="创建者ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("creator_member_id")
    private Long creatorMemberId;

    /* 状态*/
    @Schema(name = "状态", description = "正常、锁定、删除")
    private CommonStatusEnum status;

    /* 数据权限*/
    private CommonPowerEnum power;

    @Schema(name ="标题")
    private String summary;

    @Schema(name ="地点")
    private String location;

    @Schema(name ="描述")
    private String description;

    @Schema(name ="事件的开始时间")
    private Date dtstart;

    @Schema(name ="事件的结束时间")
    private Date dtend;

    @Schema(name ="整个事件的开始时间(用于查询)")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("start_time")
    private Long startTime;

    @Schema(name ="整个事件的结束时间(用于查询)")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("end_time")
    private Long endTime;

    @Schema(name ="是否全天")
    @TableField("full_day")
    private int fullDay;

    @Schema(name ="是否循环")
    @TableField("repeat_status")
    private String repeatStatus;

    @Schema(name = "循环类型", description = "日、周、月、年")
    @TableField("repeat_type")
    private ComponentRepeatTypeEnum repeatType;

    @Schema(name ="提醒方式")
    @TableField("alarm_type")
    private ComponentAlarmEnum alarmType;

    @Schema(name ="提醒时间")
    @TableField("alarm_times")
    private String alarmTimes;


    @Schema(name ="循环间隔")
    @TableField("repeat_interval")
    private Integer repeatInterval;


    /* 循环指定某个月份的第几周 */
    /*
     * 每周的周几 存 0:1 0：2
     * 每个月第几个周几 存 2:2
     *
     * */
    @TableField("repeat_byday")
    private String repeatByday;

    /* 循环指定某几个月份
     *
     *  每年的第几月
     * */
    @TableField("repeat_bymonth")
    private String repeatBymonth;

    /* 循环指定一个月中的某几日 */
    /*
     * 每个月的第多少天
     * 每年的哪个月的第几天
     */
    @TableField("repeat_bymonthday")
    private String repeatBymonthday;

    /**
     * 循环为周 1 3 5 步长
     * 循环为月 1 3 4月
     */
    @TableField("repeat_bysetpos")
    private String repeatBysetpos;

    @Schema(name ="循环截至时间")
    @TableField("repeat_until")
    private Date repeatUntil;

    @Schema(name ="事件时区")
    @TableField("time_zone")
    private String timeZone;
}