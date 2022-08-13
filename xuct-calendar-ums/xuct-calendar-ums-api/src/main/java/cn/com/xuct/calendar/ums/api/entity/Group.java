/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberGroup
 * Author:   Derek Xu
 * Date:     2022/2/7 15:45
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.api.entity;

import cn.com.xuct.calendar.common.module.enums.CommonStatusEnum;
import cn.com.xuct.calendar.common.module.enums.CommonPowerEnum;
import cn.com.xuct.calendar.common.db.dao.base.SuperEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
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
@TableName("sys_group")
public class Group extends SuperEntity<Group> {

    private String name;

    private String no;

    /* 编号 */
    @TableField(value = "images", updateStrategy = FieldStrategy.IGNORED)
    private String images;

    @TableField("member_id")
    private Long memberId;

    @TableField("status")
    private CommonStatusEnum status;

    private CommonPowerEnum power;

    private String password;

    private Integer num;

    @TableField(exist = false)
    private Integer count;
}