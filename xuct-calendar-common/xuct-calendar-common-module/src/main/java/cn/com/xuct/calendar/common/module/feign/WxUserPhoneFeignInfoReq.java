/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: WxUserPhoneDto
 * Author:   Derek Xu
 * Date:     2022/3/29 17:01
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

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/29
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WxUserPhoneFeignInfoReq implements Serializable {

    @NotNull
    private String sessionKey;

    @NotNull
    private String encryptedData;

    @NotNull
    private String ivStr;
}