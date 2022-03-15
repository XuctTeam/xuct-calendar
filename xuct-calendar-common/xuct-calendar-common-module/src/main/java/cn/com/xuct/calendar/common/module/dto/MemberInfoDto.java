/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MemberInfoDto
 * Author:   Derek Xu
 * Date:     2021/11/15 12:40
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
 * @create 2021/11/15
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "会员类")
public class MemberInfoDto implements Serializable {

    @ApiModelProperty(value = "会员ID")
    private Long userId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "账号")
    private String username;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "时区")
    private String timeZone;

}