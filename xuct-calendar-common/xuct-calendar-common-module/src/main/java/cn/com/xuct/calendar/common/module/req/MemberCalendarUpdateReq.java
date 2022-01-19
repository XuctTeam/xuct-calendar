/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MemberCalendarUpdateRep
 * Author:   Derek Xu
 * Date:     2021/12/9 9:11
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/9
 * @since 1.0.0
 */
@Data
public class MemberCalendarUpdateReq implements Serializable {

    @NotNull
    @ApiModelProperty("日历ID")
    private Long id;

    @NotNull
    @ApiModelProperty("颜色Code")
    private String color;

    @ApiModelProperty("创建者昵称")
    private String createMemberName;

    @NotNull
    @ApiModelProperty("日历名称")
    private String name;

    @NotNull
    @ApiModelProperty("日历描述")
    private String description;

    @NotNull
    @ApiModelProperty("是否显示")
    private Integer display;

    @ApiModelProperty("提醒时间")
    @NotNull
    private String alarmTime;

    @ApiModelProperty("提醒类型")
    @NotNull
    private String alarmType;

    @ApiModelProperty("是否共享")
    @NotNull
    private Integer isShare;
}