/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: Super
 * Author:   Derek Xu
 * Date:     2021/11/10 12:41
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.db.dao.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/10
 * @since 1.0.0
 */
@Data
public class SuperEntity<T> {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId//主键id
    private Long id;

    //insert时自动填充
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    private Date createTime = new Date();

    //update时自动填充
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_time")
    private Date updateTime = new Date();
}