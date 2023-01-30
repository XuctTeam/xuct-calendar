/**
 * Copyright (C), 2015-2023, XXX有限公司
 * FileName: CalendarSharedVo
 * Author:   Derek Xu
 * Date:     2023/1/30 15:15
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.api.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2023/1/30
 * @since 1.0.0
 */
@Data
public class CalendarSharedVo implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "短链地址")
    private String shortUrl;

    @Schema(description = "小程序二维码")
    private String qr;

}