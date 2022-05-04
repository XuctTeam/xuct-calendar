/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: CalendarCountFeignInfo
 * Author:   Derek Xu
 * Date:     2022/4/29 17:35
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.feign.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/4/29
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarCountFeignInfo implements Serializable {

    private List<Long> memberIds;
}