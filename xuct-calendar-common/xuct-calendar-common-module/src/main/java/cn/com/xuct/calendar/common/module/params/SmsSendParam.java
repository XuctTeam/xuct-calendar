/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: SmsSendParam
 * Author:   Derek Xu
 * Date:     2022/10/8 8:53
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/10/8
 * @since 1.0.0
 */
@Data
public class SmsSendParam implements Serializable {

    @Schema(description = "电话号码" ,example = "17101991287")
    private String phone;

    @Schema(description = "0.登录 1.绑定手机 2.解绑手机 3.注册", example = "1")
    private Integer type;
}