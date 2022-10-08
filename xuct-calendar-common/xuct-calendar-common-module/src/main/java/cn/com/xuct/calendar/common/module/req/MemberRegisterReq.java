/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MemberRegisterRep
 * Author:   Derek Xu
 * Date:     2021/12/6 14:55
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.req;

import cn.com.xuct.calendar.common.module.enums.RegisterEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/6
 * @since 1.0.0
 */
@Data
public class MemberRegisterReq implements Serializable {

    @NotNull
    @Schema(name = "注册类型")
    private RegisterEnum type;

    @Schema(name = "用户名")
    private String username;

    @Schema(name = "密码")
    private String password;

    @Schema(name = "手机号")
    private String phone;

    @Schema(name = "短信验证码")
    private String smsCode;
}