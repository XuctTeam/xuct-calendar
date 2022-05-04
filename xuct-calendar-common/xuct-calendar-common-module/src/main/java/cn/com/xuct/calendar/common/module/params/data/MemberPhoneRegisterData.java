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

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("验证码")
    private String smsCode;
}