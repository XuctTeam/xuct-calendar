/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberMessagePageVo
 * Author:   Derek Xu
 * Date:     2022/2/17 13:46
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.api.vo;

import cn.com.xuct.calendar.ums.api.entity.MemberMessage;
import lombok.Data;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/17
 * @since 1.0.0
 */
@Data
public class MemberMessagePageVo {

    private boolean finished = false;

    private List<MemberMessage> messages;
}