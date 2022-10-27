/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: MemberMergeParam
 * Author:   Derek Xu
 * Date:     2022/4/29 17:21
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
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
 * @create 2022/4/29
 * @since 1.0.0
 */
@Data
public class MemberMergeParam implements Serializable {

    @Schema(title = "电话号码")
    private String phone;
}