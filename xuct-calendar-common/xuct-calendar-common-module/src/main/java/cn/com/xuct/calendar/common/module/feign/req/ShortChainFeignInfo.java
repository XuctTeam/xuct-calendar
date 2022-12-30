/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ShortChainFeignInfo
 * Author:   Derek Xu
 * Date:     2022/5/5 17:33
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.feign.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
 * @create 2022/5/5
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortChainFeignInfo implements Serializable {

    @NotNull
    private String url;

    @NotNull
    @Schema(description = "申请类型")
    private String type;

    @NotNull
    @Schema(description = "有效时间(ms)0为不过期")
    private Long expire = 0L;
}