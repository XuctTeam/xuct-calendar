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

import lombok.Data;

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

    private String name;

    private String memberId;

    private String avatar;

    private String groupId;

    private String groupName;

    private String groupCreateMemberId;
}