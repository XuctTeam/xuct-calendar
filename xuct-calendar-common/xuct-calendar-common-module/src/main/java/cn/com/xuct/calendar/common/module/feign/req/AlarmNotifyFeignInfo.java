/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberAuthFeignInfoReq
 * Author:   Derek Xu
 * Date:     2022/3/30 10:04
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

import java.io.Serializable;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/30
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlarmNotifyFeignInfo implements Serializable {

    @ApiModelProperty("事件ID")
    private Long componentId;

    @ApiModelProperty("事件标题")
    private String summary;

    @ApiModelProperty("事件开始时间")
    private String startDate;

    @ApiModelProperty("事件创建者")
    private Long createMemberId;

    @ApiModelProperty("参加人")
    private List<Long> ids;

    @ApiModelProperty("认证方式 1.站内信 2.邮件 3.订阅消息")
    private Integer type;
}