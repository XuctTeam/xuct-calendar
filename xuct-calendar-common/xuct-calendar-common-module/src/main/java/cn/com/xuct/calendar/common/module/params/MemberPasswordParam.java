/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MemberPasswordParam
 * Author:   Administrator
 * Date:     2021/12/19 17:31
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/19
 * @since 1.0.0
 */
@Data
public class MemberPasswordParam implements Serializable {

    @NotNull
    private String password;
}