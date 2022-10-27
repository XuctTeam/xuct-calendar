/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: GroupDeleteParam
 * Author:   Derek Xu
 * Date:     2022/3/4 21:12
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
 * @create 2022/3/4
 * @since 1.0.0
 */
@Data
public class GroupDeleteParam implements Serializable {

    @Schema(title = "群组ID")
    private Long id;
}