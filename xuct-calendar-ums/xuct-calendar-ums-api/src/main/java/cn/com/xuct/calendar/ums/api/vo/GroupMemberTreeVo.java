/**
 * Copyright (C), 2015-2023, XXX有限公司
 * FileName: GroupMemberTreeVo
 * Author:   Derek Xu
 * Date:     2023/1/5 9:45
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.api.vo;

import cn.com.xuct.calendar.ums.api.dto.GroupMemberInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2023/1/5
 * @since 1.0.0
 */
@Data
public class GroupMemberTreeVo implements Serializable {

    @Schema(description = "群组名称")
    private String groupName;

    @Schema(description = "群组ID")
    private String groupId;

    @Schema(description = "群组创建人ID")
    private String groupCreateMemberId;

    @Schema(description = "群组所有成员")
    private List<GroupMemberInfoDto> members;
}