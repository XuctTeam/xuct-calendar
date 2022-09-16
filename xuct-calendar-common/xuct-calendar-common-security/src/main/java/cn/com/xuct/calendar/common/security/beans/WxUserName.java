/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: WxUserName
 * Author:   Derek Xu
 * Date:     2022/9/16 13:28
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.security.beans;

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
 * @create 2022/9/16
 * @since 1.0.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WxUserName implements Serializable {

    private String code;

    private String iv;

    private String encryptedData;
}