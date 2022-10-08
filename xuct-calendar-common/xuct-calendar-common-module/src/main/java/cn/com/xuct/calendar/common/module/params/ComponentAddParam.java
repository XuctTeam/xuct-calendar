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

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(name = "事件ID")
    private String id;

    @NotNull
    @Schema(name ="标题")
    private String summary;

    @NotNull
    @Schema(name ="日历ID")
    private String calendarId;

    @Schema(name ="地点")
    private String location;

    @Schema(name ="描述")
    private String description;

    @NotNull
    @Schema(name ="日程的开始时间")
    private Date dtstart;

    @NotNull
    @Schema(name ="日程的结束时间")
    private Date dtend;

    @NotNull
    @Schema(name ="是否全天")
    private int fullDay;

    @Schema(name ="是否循环")
    private String repeatStatus;

    @Schema(name ="循环类型")
    private String repeatType;

    @Schema(name = "提醒设置", description = "不提醒, ")
    private String alarmType;

    @Schema(name = "提醒时间", description = "时间逗号分割")
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
    @Schema(name = "循环指定某几个月份")
    private String repeatBymonth;

    /** 循环指定一个月中的某几日 */
    /**
     * 每个月的第多少天
     * 每年的哪个月的第几天
     */
    @Schema(name ="循环指定一个月中的某几日")
    private String repeatBymonthday;

    /**
     * 循环为周 1 3 5 步长
     * 循环为月 1 3 4月
     */
    @Schema(name ="循环步长")
    private String repeatBysetpos;

    @Schema(name ="循环间隔")
    private int repeatInterval;

    @Schema(name ="循环截止时间")
    private Date repeatUntil;

    @Schema(name ="邀请人")
    private List<String> memberIds;
}