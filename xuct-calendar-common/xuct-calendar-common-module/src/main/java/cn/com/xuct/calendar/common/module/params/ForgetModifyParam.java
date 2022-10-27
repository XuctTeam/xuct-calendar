/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ForgetModifyParam
 * Author:   Derek Xu
 * Date:     2022/3/27 3:17
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/27
 * @since 1.0.0
 */
@Data
public class ForgetModifyParam {

    @Schema(title = "会员ID")
    @NotNull
    private Long memberId;

    @Schema(title = "密码")
    @NotNull
    private String password;

    @Schema(title = "验证码")
    @NotNull
    private String code;
}