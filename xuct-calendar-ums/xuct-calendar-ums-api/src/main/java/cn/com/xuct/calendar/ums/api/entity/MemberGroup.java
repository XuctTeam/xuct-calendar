/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberGroup
 * Author:   Derek Xu
 * Date:     2022/2/7 16:15
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.api.entity;

import cn.com.xuct.calendar.common.module.enums.GroupMemberStatusEnum;
import cn.com.xuct.calendar.common.db.dao.base.SuperEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/7
 * @since 1.0.0
 */
@Data
@TableName("sys_member_group")
public class MemberGroup extends SuperEntity<MemberGroup> {

    @TableField("group_id")
    private Long groupId;

    @TableField("member_id")
    private Long memberId;

    /*0 正常 1申请中 */
    private GroupMemberStatusEnum status;

}