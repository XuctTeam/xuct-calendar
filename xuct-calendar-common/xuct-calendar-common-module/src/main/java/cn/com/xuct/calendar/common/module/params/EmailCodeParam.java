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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

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

    private String code;

    @NotNull
    private String email;

    @ApiModelProperty(name = "类型", notes = "1.绑定邮箱 2.解绑邮箱")
    private Integer type;

}