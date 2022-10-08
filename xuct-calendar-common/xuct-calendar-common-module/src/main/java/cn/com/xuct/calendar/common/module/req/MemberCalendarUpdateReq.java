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
public class MemberCalendarUpdateReq implements Serializable {

    @NotNull
    @Schema(name = "日历ID")
    private Long id;

    @NotNull
    @Schema(name ="颜色Code")
    private String color;

    @Schema(name ="创建者昵称")
    private String createMemberName;

    @NotNull
    @Schema(name ="日历名称")
    private String name;

    @NotNull
    @Schema(name ="日历描述")
    private String description;

    @NotNull
    @Schema(name ="是否显示")
    private Integer display;

    @Schema(name ="提醒时间")
    @NotNull
    private String alarmTime;

    @Schema(name ="提醒类型")
    @NotNull
    private String alarmType;

    @Schema(name ="是否共享")
    @NotNull
    private Integer isShare;
}