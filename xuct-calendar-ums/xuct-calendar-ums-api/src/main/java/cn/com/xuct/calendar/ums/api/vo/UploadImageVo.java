/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: UploadImageVo
 * Author:   Derek Xu
 * Date:     2022/10/25 13:57
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/10/25
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadImageVo implements Serializable {

    @Schema(title = "图片地址")
    private String url;

    @Schema(title = "缩略图")
    private String thumbnailUrl;

    @Schema(title = "图片宽")
    private Integer width;

    @Schema(title = "图片高")
    private Integer height;
}