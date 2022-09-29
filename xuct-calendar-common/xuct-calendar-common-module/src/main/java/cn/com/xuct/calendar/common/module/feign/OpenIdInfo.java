/**
 * Copyright (C), 2015-2022, 楚恬商场
 * FileName: OpenIdInfo
 * Author:   Derek Xu
 * Date:     2022/9/14 19:46
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.feign;

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
 * @create 2022/9/14
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenIdInfo implements Serializable {

    private String openId;

    private String sessionKey;
}