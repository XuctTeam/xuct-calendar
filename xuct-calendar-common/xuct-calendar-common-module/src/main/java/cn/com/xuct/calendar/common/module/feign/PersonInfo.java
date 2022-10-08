/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberInfo
 * Author:   Derek Xu
 * Date:     2022/9/6 16:35
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.feign;

import io.swagger.v3.oas.annotations.media.Schema;
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
 * @create 2022/9/6
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "会员类")
public class PersonInfo implements Serializable {

    @Schema(name  = "会员ID")
    private Long userId;

    @Schema(name = "名称")
    private String name;

    @Schema(name = "头像")
    private String avatar;

    @Schema(name = "账号")
    private String username;

    @Schema(name = "状态")
    private Integer status;

    @Schema(name = "密码")
    private String password;

    @Schema(name = "时区")
    private String timeZone;


}