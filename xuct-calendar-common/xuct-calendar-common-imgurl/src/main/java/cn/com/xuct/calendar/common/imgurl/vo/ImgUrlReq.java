/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: SmmsUploadRes
 * Author:   Derek Xu
 * Date:     2022/2/13 16:13
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.imgurl.vo;

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
public class ImgUrlReq implements Serializable {

    private String file;

    private String uid;

    private String token;
}