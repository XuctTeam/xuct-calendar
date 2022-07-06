/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: GroupCountDto
 * Author:   Derek Xu
 * Date:     2022/2/14 22:17
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈群组统计列表〉
 *
 * @author Derek Xu
 * @create 2022/2/14
 * @since 1.0.0
 */
@Data
public class GroupInfoDto implements Serializable {

    @ApiModelProperty(notes = "ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(notes = "群组名称")
    private String name;

    @ApiModelProperty(notes = "群组图片")
    private String images;

    private String password;

    private Integer count;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long createMemberId;

    private String createMemberName;

    private String power;

    /* 群组设置人数 */
    private Integer num;

    private int hasPasswordJoin = 0;
}