/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: MemberRegisterParam
 * Author:   Derek Xu
 * Date:     2022/2/19 10:02
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params;

import cn.com.xuct.calendar.common.module.enums.RegisterEnum;
import cn.com.xuct.calendar.common.module.params.data.MemberEmailRegisterData;
import cn.com.xuct.calendar.common.module.params.data.MemberPhoneRegisterData;
import cn.com.xuct.calendar.common.module.params.data.MemberUserNameRegisterData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/19
 * @since 1.0.0
 */
@Data
public class MemberRegisterParam implements Serializable {

    @NotNull
    @Schema(title = "注册类型")
    private RegisterEnum formType;

    @Schema(title = "用户名注册参数")
    private MemberUserNameRegisterData username;

    @Schema(title = "手机注册参数")
    private MemberPhoneRegisterData phone;

    @Schema(title = "邮箱注册参数")
    private MemberEmailRegisterData email;

}