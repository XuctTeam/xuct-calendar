/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: EmailDto
 * Author:   Derek Xu
 * Date:     2022/3/28 10:47
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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/28
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailFeignInfo implements Serializable {

    @Schema(title = "收件者")
    private List<String> tos;

    @Schema(title = "抄送")
    private List<String> ccs;

    @Schema(title = "主题")
    private String subject;

    @Schema(title = "参数")
    private Map<String, Object> params;

    @Schema(title = "模板")
    private String template;

}