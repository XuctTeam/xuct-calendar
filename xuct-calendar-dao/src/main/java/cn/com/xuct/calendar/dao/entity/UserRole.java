/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: UserRole
 * Author:   Derek Xu
 * Date:     2021/11/22 14:58
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/22
 * @since 1.0.0
 */
@Data
public class UserRole extends SuperEntity<UserRole> {

    @TableField("user_id")
    private Long userId;

    @TableField("role_id")
    private Long roleId;
}