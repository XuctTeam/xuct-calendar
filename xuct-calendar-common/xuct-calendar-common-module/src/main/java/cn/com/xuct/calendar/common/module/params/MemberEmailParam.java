/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: MemberEmailParam
 * Author:   Derek Xu
 * Date:     2022/5/4 16:48
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
 * @create 2022/5/4
 * @since 1.0.0
 */
@Data
public class MemberEmailParam implements Serializable {

    private String email;

    private String code;
}