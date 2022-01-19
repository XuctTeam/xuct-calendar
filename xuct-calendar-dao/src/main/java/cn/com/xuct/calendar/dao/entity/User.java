/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: User
 * Author:   Derek Xu
 * Date:     2021/11/22 14:54
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("sys_user")
public class User extends SuperEntity<User> {

    @TableField("user_name")
    private String userName;

    private String password;

    @TableField("nick_name")
    private String nickName;

    @TableField("dept_id")
    private Long deptId;

    @TableField("del_flag")
    private int delFlag;
}