/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MemberPhoneReq
 * Author:   Derek Xu
 * Date:     2021/12/13 16:58
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/13
 * @since 1.0.0
 */
@Data
public class MemberPhoneParam implements Serializable {

    @Schema(title = "手机号")
    @NotNull
    private String phone;

    @Schema(title = "验证码")
    private String code;

    @Schema(title = "类型", description = "1.绑定手机 2.解绑手机")
    private Integer type;
}