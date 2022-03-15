/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: Role
 * Author:   Derek Xu
 * Date:     2021/11/22 14:57
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.api.entity;

import cn.com.xuct.calendar.common.db.dao.base.SuperEntity;
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
@TableName("sys_role")
public class Role extends SuperEntity<Role> {

    private String name;
}