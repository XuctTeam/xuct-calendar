/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: WechatCodeDto
 * Author:   Administrator
 * Date:     2021/11/27 21:14
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

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/27
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WechatCodeDto {

    private String code;

    private String encryptedData;

    private String iv;
}