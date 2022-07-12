/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MessageDeleteBatchParam
 * Author:   Derek Xu
 * Date:     2022/6/18 17:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2022/6/18
 * @since 1.0.0
 */
@Data
public class MessageDeleteBatchParam implements Serializable {

    private List<String> ids;
}