/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ComponentAddAlarmData
 * Author:   Administrator
 * Date:     2022/1/3 18:18
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params.data;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/3
 * @since 1.0.0
 */
@Data
public class ComponentAddAlarmData implements Serializable {

    private String alarmType;

    private List<Integer> alarmTime;
}