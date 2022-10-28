/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: MemberUserNameRegisterData
 * Author:   Derek Xu
 * Date:     2022/5/4 11:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/5/4
 * @since 1.0.0
 */
@Data
public class MemberUserNameRegisterData implements Serializable {

    @NotNull
    @Schema(title = "用户名")
    public String username;

    @NotNull
    @Schema(title = "密码")
    private String password;

    @NotNull
    @Schema(title = "请求key")
    private String randomStr;

    @NotNull
    @Schema(title = "验证码")
    private String captcha;
}