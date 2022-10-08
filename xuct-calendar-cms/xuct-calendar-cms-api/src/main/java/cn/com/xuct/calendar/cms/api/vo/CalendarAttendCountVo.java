/**
 * Copyright (C), 2015-2022, 楚恬商场
 * FileName: CalendarAttendCountVo
 * Author:   Derek Xu
 * Date:     2022/10/6 22:02
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/10/6
 * @since 1.0.0
 */
@Data
public class CalendarAttendCountVo implements Serializable {

    @Schema(name = "邀请总人数")
    private Integer attendSum;

    @Schema(name = "接受总人数")
    private Integer accepted;

    @Schema(name = "拒绝总人数")
    private Integer noAccepted;

    @Schema(name = "未操作总人数")
    private Integer noOperation;

}