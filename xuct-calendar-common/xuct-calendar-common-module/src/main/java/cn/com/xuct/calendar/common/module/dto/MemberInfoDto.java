/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MemberInfoDto
 * Author:   Derek Xu
 * Date:     2021/11/15 12:40
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/15
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoDto implements Serializable {

    private Long userId;

    private String username;

    private Integer status;

    private String password;

}