/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: SmsCodeDto
 * Author:   Derek Xu
 * Date:     2022/3/28 10:23
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
import java.util.List;

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
public class SmsCodeFeignInfo {

    @NotEmpty
    @Schema(name = "收短信人")
    private List<String> phones;

    @NotNull
    @Schema(name = "短信模板")
    private String template;

    @NotNull
    @Schema(name = "验证码")
    private String code;
}