/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: Dict
 * Author:   Derek Xu
 * Date:     2021/12/6 13:57
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/6
 * @since 1.0.0
 */
@Data
@TableName("sys_dict")
public class Dict extends SuperEntity<Dict> {

    private String type;

    private String name;

    private String code;

    private String value;
}