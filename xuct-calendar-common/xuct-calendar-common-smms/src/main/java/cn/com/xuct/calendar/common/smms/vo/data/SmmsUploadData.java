/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: SmmsUploadData
 * Author:   Derek Xu
 * Date:     2022/2/13 16:13
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.smms.vo.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/13
 * @since 1.0.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmmsUploadData implements Serializable {

    private String url;

    @JsonProperty("file_id")
    private Integer fileId;

    private String filename;

    private Integer width;

    private Integer height;

    @JsonProperty("storename")
    private String storeName;

    private Long size;

    private String path;

    private String hash;



    private String delete;

    private String page;
}