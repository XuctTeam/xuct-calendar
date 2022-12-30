/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ComponentAttendParam
 * Author:   Derek Xu
 * Date:     2022/3/15 11:45
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/15
 * @since 1.0.0
 */
@Data
public class ComponentAttendParam implements Serializable {

    @NotNull
    private Long componentId;

    @NotNull
    private Integer status;
}