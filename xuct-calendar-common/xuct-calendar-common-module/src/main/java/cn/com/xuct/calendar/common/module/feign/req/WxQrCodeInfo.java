/**
 * Copyright (C), 2015-2023, XXX有限公司
 * FileName: WxQrCodeInfo
 * Author:   Derek Xu
 * Date:     2023/2/3 17:39
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.feign.req;

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
 * @create 2023/2/3
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WxQrCodeInfo implements Serializable {

    private String scene;

    private String page;

    private String envVersion;

    private Integer width;
}