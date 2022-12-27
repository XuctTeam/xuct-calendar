/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ComponentAttendVo
 * Author:   Derek Xu
 * Date:     2022/3/15 16:17
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/15
 * @since 1.0.0
 */
@Data
public class ComponentAttendVo {

    @Schema(description = "参与者姓名")
    private String name;

    @Schema(description = "参与者头像")
    private String avatar;

    @Schema(description = "参与者ID")
    private String memberId;

    @Schema(description = "接受状态")
    private Integer status;

}