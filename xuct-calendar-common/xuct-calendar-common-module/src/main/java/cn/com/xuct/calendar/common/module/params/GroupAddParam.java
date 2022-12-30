/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: GroupAddParam
 * Author:   Derek Xu
 * Date:     2022/2/13 10:26
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params;

import cn.com.xuct.calendar.common.module.enums.CommonPowerEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/13
 * @since 1.0.0
 */
@Data
public class GroupAddParam implements Serializable {

    @Schema(title = "群组ID")
    private Long id;

    @Schema(title = "群组图片")
    private String imageUrl;

    @Schema(title = "群组名称")
    @NotNull
    private String name;

    @Schema(title = "是否可搜索")
    @NotNull
    private String power;

    @Schema(title = "密码")
    private String password;

    @Schema(title = "群组人数")
    private Integer num;
}