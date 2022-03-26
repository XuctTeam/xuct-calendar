/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ForgetPasswordParam
 * Author:   Derek Xu
 * Date:     2022/3/26 23:52
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/26
 * @since 1.0.0
 */
@Data
public class ForgetPasswordParam {

    private String email;

    private String phone;

    private String code;

    @NotNull
    private Integer type;
}