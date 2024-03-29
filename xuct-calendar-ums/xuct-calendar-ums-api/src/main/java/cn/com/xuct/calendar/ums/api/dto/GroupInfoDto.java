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
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(title = "ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(title = "群组名称")
    private String name;

    private Date createTime;

    @Schema(title = "群组图片")
    private String images;

    @Schema(title = "编号")
    private String no;

    @Schema(title = "密码")
    private String password;

    @Schema(title = "总人数")
    private Integer count;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long createMemberId;

    private String createMemberName;

    @Schema(title = "是否公开")
    private String power;

    @Schema(title = "最大人数")
    private Integer num;

    @Schema(title = "是否需要免密加入")
    private int hasPasswordJoin = 0;
}