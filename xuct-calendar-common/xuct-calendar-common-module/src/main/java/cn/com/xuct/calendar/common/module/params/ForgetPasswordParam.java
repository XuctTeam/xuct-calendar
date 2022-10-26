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

import javax.validation.constraints.NotNull;

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

    @Schema(name = "邮箱")
    private String email;

    @Schema(name = "电话")
    private String phone;

    @Schema(name = "验证码")
    private String code;

    @Schema(name = "验证类型")
    @NotNull
    private Integer type;
}