/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: GroupApplyParam
 * Author:   Derek Xu
 * Date:     2022/2/28 14:45
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/28
 * @since 1.0.0
 */
@Data
public class GroupApplyParam implements Serializable {

   @Schema(title = "群组ID")
   private String id;

   @Schema(title = "操作类型" , description = "1.拒绝入群 2.主动离开")
   private Integer action;
}