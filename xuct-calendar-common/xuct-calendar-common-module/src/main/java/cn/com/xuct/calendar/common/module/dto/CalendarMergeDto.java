/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MemberCalendarMergeReq
 * Author:   Derek Xu
 * Date:     2021/12/13 17:26
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/13
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CalendarMergeDto implements Serializable {

    @ApiModelProperty("当前用户ID")
    private Long memberId;

    @ApiModelProperty("需合并的用户ID")
    private Long fromMemberId;
}