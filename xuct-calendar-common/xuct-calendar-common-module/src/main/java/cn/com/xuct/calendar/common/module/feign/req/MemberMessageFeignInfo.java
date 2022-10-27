/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberMessageFeignInfoReq
 * Author:   Derek Xu
 * Date:     2022/4/1 10:41
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.feign.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/4/1
 * @since 1.0.0
 */
@Data
@Builder
@Schema(description = "站内信消息体")
@NoArgsConstructor
@AllArgsConstructor
public class MemberMessageFeignInfo implements Serializable {

    @Schema(title = "标题")
    private String title;

    @NotNull
    @Schema(title = "类型", description = "SYSTEM,GROUP,EVENT")
    private String type;

    @NotEmpty
    @Schema(title = "会员")
    private List<Long> memberIds;

    @NotNull
    @Schema(title ="操作")
    private Integer operation;

    @Schema(title = "消息体")
    private Map<String, Object> content;
}