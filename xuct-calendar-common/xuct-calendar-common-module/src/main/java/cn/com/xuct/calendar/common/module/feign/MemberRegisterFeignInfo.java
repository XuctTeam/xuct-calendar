/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberRegisterDto
 * Author:   Derek Xu
 * Date:     2022/3/26 19:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.feign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/26
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRegisterFeignInfo {

    public String username;

    private String password;
}