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
package cn.com.xuct.calendar.common.module.feign.req;

import io.swagger.annotations.ApiModelProperty;
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
public class WxUserInfoFeignInfo {

    @ApiModelProperty("小程序openId")
    private String openId;

    @ApiModelProperty("小程序SessionKey")
    private String sessionKey;

    @ApiModelProperty("小程序encryptedData")
    private String encryptedData;

    @ApiModelProperty("小程序iv")
    private String iv;

    @ApiModelProperty("是否是登录")
    private boolean login = false;
}