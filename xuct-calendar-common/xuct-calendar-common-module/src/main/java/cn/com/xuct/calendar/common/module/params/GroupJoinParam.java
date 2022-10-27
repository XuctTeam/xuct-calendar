/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: GroupJoinParam
 * Author:   Derek Xu
 * Date:     2022/2/17 10:56
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/17
 * @since 1.0.0
 */
@Data
public class GroupJoinParam implements Serializable {

    @Schema(title = "群组ID")
    @NotNull
    private Long id;

    @Schema(title = "加入密码")
    private String password;
}