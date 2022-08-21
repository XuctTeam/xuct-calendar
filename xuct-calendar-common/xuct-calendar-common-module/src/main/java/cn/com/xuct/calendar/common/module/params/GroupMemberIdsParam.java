/**
 * Copyright (C), 2015-2022, 楚恬商场
 * FileName: GroupMemberIdsParam
 * Author:   Derek Xu
 * Date:     2022/8/21 20:55
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/8/21
 * @since 1.0.0
 */
@Data
public class GroupMemberIdsParam implements Serializable {

    private List<Long> ids;
}