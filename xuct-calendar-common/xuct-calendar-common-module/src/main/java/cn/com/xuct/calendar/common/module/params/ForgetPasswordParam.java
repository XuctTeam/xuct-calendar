/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ForgetPasswordParam
 * Author:   Derek Xu
 * Date:     2022/3/26 23:52
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/26
 * @since 1.0.0
 */
@Data
public class ForgetPasswordParam {

    @Schema(title = "邮箱")
    private String email;

    @Schema(title = "电话")
    private String phone;

    @Schema(title = "验证码")
    private String code;

    @Schema(title = "验证类型")
    @NotNull
    private Integer type;
}