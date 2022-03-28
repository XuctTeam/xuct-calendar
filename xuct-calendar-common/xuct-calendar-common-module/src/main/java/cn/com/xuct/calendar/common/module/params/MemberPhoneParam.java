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

import io.swagger.annotations.ApiModelProperty;
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

    @NotNull
    private String phone;

    private String code;

    @ApiModelProperty(name = "类型", notes = "1.绑定手机 2.解绑手机")
    private Integer type;
}