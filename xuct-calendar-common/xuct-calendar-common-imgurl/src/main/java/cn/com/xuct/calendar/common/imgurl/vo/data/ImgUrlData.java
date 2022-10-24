/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ImgUrlData
 * Author:   Derek Xu
 * Date:     2022/10/24 18:25
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.imgurl.vo.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/10/24
 * @since 1.0.0
 */
@Data
public class ImgUrlData implements Serializable {

    @JsonProperty("relative_path")
    private String relativePath;

    private String url;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @JsonProperty("image_width")
    private Integer imageWidth;

    @JsonProperty("image_height")
    private Integer imageHeight;

    @JsonProperty("client_name")
    private String clientName;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("imgid")
    private String imgId;

    private String delete;

}