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
    @Schema(name = "用户名")
    public String username;

    @NotNull
    @Schema(name = "密码")
    private String password;

    @NotNull
    @Schema(name = "请求key")
    private String key;

    @NotNull
    @Schema(name = "验证码")
    private String captcha;
}