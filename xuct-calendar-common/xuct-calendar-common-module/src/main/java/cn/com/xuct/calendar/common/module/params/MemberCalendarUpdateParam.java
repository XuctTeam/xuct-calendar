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
package cn.com.xuct.calendar.common.module.params;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class MemberCalendarUpdateParam implements Serializable {

    @Schema(title = "日历ID")
    private Long id;

    @NotNull
    @Schema(title ="颜色Code")
    private String color;

    @Schema(title ="创建者昵称")
    private String createMemberName;

    @NotNull
    @Schema(title ="日历名称")
    private String name;

    @NotNull
    @Schema(title ="日历描述")
    private String description;

    @NotNull
    @Schema(title ="是否显示")
    private Integer display;

    @Schema(title ="提醒时间")
    @NotNull
    private String alarmTime;

    @Schema(title ="提醒类型")
    @NotNull
    private String alarmType;

    @Schema(title ="是否共享")
    @NotNull
    private Integer isShare;
}