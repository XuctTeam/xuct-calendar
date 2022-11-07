/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberRegisterDto
 * Author:   Derek Xu
 * Date:     2022/11/7 16:38
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.api.dto;

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
 * @create 2022/11/7
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegisterDto implements Serializable {

    private Integer code;

    private String message;
}