/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ComponentAddParam
 * Author:   Administrator
 * Date:     2022/1/3 16:32
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params;

import cn.com.xuct.calendar.common.module.enums.ComponentRepeatTypeEnum;
import cn.com.xuct.calendar.common.module.params.data.ComponentAddAlarmData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/3
 * @since 1.0.0
 */
@Data
public class ComponentAddParam implements Serializable {

    @ApiModelProperty("事件ID")
    private String id;

    @NotNull
    @ApiModelProperty("标题")
    private String summary;

    @NotNull
    @ApiModelProperty("日历ID")
    private String calendarId;

    @ApiModelProperty("地点")
    private String location;

    @ApiModelProperty("描述")
    private String description;

    @NotNull
    @ApiModelProperty("日程的开始时间")
    private Date dtstart;

    @NotNull
    @ApiModelProperty("日程的结束时间")
    private Date dtend;

    @NotNull
    @ApiModelProperty("是否全天")
    private int fullDay;

    @ApiModelProperty("是否循环")
    private String repeatStatus;

    @ApiModelProperty("循环类型")
    private String repeatType;


    @ApiModelProperty(value = "提醒设置", notes = "不提醒, ")
    private String alarmType;

    @ApiModelProperty(value = "提醒时间", notes = "时间逗号分割")
    private List<Integer> alarmTimes;


    /** 循环指定某个月份的第几周 */
    /**
     * 每周的周几 存 0:1 0：2
     * 每个月第几个周几 存 2:2
     */

    private String repeatByday;

    /**
     * 循环指定某几个月份
     * <p>
     * 每年的第几月
     */
    @ApiModelProperty("循环指定某几个月份")
    private String repeatBymonth;

    /** 循环指定一个月中的某几日 */
    /**
     * 每个月的第多少天
     * 每年的哪个月的第几天
     */
    @ApiModelProperty("循环指定一个月中的某几日")
    private String repeatBymonthday;

    /**
     * 循环为周 1 3 5 步长
     * 循环为月 1 3 4月
     */
    @ApiModelProperty("循环步长")
    private String repeatBysetpos;

    @ApiModelProperty("循环间隔")
    private int repeatInterval;

    @ApiModelProperty("循环截止时间")
    private Date repeatUntil;


}