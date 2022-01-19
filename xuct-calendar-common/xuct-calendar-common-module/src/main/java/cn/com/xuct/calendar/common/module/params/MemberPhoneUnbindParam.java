/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MmemberPhoneUnbindParam
 * Author:   Administrator
 * Date:     2021/12/18 15:56
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
 * @create 2021/12/18
 * @since 1.0.0
 */
@Data
public class MemberPhoneUnbindParam {

    @NotNull
    private String code;

}