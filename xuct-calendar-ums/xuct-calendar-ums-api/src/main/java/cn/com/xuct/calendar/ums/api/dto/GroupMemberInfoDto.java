/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: GroupMemberInfoDto
 * Author:   Derek Xu
 * Date:     2022/3/1 10:40
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

import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/1
 * @since 1.0.0
 */
@Data
public class GroupMemberInfoDto {

    @Schema(description = "ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "组员名称")
    private String name;

    @Schema(description = "组员ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long memberId;

    @Schema(description = "组员头像")
    private String avatar;

    @Schema(description = "群组ID")
    private String groupId;

    @Schema(description = "群组名称")
    private String groupName;

    @Schema(description = "群组创建人ID")
    private String groupCreateMemberId;

    @Schema(description = "群组创建人名称")
    private String groupCreateMemberName;

    @Schema(description = "群组创建时间")
    private Date createTime;

}