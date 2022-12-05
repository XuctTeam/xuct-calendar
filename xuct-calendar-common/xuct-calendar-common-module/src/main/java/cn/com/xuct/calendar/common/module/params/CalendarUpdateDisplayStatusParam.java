/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: CalendarUpdateDisplayStatusParam
 * Author:   Derek Xu
 * Date:     2022/12/5 9:10
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/12/5
 * @since 1.0.0
 */
@Data
public class CalendarUpdateDisplayStatusParam implements Serializable {

    @Schema(title = "日历ID")
    private String calendarId;

    @Schema(title = "显示状态 0不显示 1显示")
    private Integer display;
}