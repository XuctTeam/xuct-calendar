/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: EmailCodeParam
 * Author:   Derek Xu
 * Date:     2022/3/28 10:10
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/28
 * @since 1.0.0
 */
@Data
public class EmailCodeParam {

    @Schema(title = "验证码")
    private String code;

    @Schema(title = "邮箱")
    @NotNull
    private String email;

    @Schema(title = "类型", description = "1.邮箱注册  2.忘记密码  3.绑定邮箱 4.解绑邮箱")
    private Integer type;

}