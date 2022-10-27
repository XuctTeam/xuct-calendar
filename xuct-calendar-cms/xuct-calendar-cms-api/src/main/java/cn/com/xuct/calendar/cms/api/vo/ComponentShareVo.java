/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: ComponentShareVo
 * Author:   Derek Xu
 * Date:     2022/4/20 20:34
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.api.vo;

import cn.com.xuct.calendar.common.module.enums.CommonPowerEnum;
import cn.com.xuct.calendar.common.module.enums.CommonStatusEnum;
import cn.com.xuct.calendar.common.module.enums.ComponentAlarmEnum;
import cn.com.xuct.calendar.common.module.enums.ComponentRepeatTypeEnum;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈事件共享对象〉
 *
 * @author Derek Xu
 * @create 2022/4/20
 * @since 1.0.0
 */
@Data
public class ComponentShareVo implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    
    @Schema(title ="日历ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long calendarId;

    @Schema(title ="创建者ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long creatorMemberId;

    @Schema(title ="创建者名称")
    private String createMemberName;

    /* 状态*/
    @Schema(title = "状态", description = "正常、锁定、删除")
    private CommonStatusEnum status;

    /* 数据权限*/
    private CommonPowerEnum power;

    @Schema(title ="标题")
    private String summary;

    @Schema(title ="地点")
    private String location;

    @Schema(title ="描述")
    private String description;

    @Schema(title ="事件的开始时间")
    private Date dtstart;

    @Schema(title ="事件的结束时间")
    private Date dtend;

    @Schema(title ="整个事件的开始时间(用于查询)")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long startTime;

    @Schema(title ="整个事件的结束时间(用于查询)")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long endTime;

    @Schema(title ="是否全天")
    private int fullDay;

    @Schema(title ="是否循环")
    private String repeatStatus;

    @Schema(title = "循环类型", description = "日、周、月、年")
    private ComponentRepeatTypeEnum repeatType;

    @Schema(title ="提醒方式")
    private ComponentAlarmEnum alarmType;

    @Schema(title ="提醒时间")
    private String alarmTimes;

    @Schema(title ="循环间隔")
    private Integer repeatInterval;


    /* 循环指定某个月份的第几周 */
    /*
     * 每周的周几 存 0:1 0：2
     * 每个月第几个周几 存 2:2
     *
     * */
    private String repeatByday;

    /* 循环指定某几个月份
     *
     *  每年的第几月
     * */
    private String repeatBymonth;

    /* 循环指定一个月中的某几日 */
    /*
     * 每个月的第多少天
     * 每年的哪个月的第几天
     */
    private String repeatBymonthday;

    @Schema(title ="循环截至时间")
    private Date repeatUntil;

    @Schema(title ="是否被邀请者")
    private Boolean attend;
}