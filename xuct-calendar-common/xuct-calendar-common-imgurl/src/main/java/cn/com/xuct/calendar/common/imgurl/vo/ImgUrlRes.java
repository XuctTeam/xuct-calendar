/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: SmmsRes
 * Author:   Derek Xu
 * Date:     2022/2/13 9:00
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.imgurl.vo;

import cn.com.xuct.calendar.common.imgurl.vo.data.ImgUrlData;
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
public class ImgUrlRes implements Serializable {


    private Integer code;

    private ImgUrlData data;

    private String msg;
}