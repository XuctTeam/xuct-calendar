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
package cn.com.xuct.calendar.common.smms.vo;

import cn.com.xuct.calendar.common.smms.vo.data.SmmsUploadData;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

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
public class SmmsUploadRes extends SmmsRes {

    private SmmsUploadData data;
}