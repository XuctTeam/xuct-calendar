/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: MemberPhoneRegisterData
 * Author:   Derek Xu
 * Date:     2022/5/4 11:44
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
public class MemberPhoneRegisterData implements Serializable {

    @Schema(name = "手机号")
    private String phone;

    @Schema(name ="密码")
    private String password;

    @Schema(name ="验证码")
    private String smsCode;
}