/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: CalUpdateInfoDto
 * Author:   Derek Xu
 * Date:     2021/12/2 16:21
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.feign;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/2
 * @since 1.0.0
 */
@Data
public class CalendarInitFeignInfoReq {

    @ApiModelProperty("会员ID")
    private Long memberId;

    @ApiModelProperty("昵称")
    private String memberNickName;
}