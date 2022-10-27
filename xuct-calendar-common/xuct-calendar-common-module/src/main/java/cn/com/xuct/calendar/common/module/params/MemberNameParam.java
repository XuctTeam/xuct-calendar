/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MemberNameParam
 * Author:   Derek Xu
 * Date:     2021/12/20 8:59
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
 * @create 2021/12/20
 * @since 1.0.0
 */
@Data
public class MemberNameParam implements Serializable {

    @Schema(title = "姓名")
    private String name;
}