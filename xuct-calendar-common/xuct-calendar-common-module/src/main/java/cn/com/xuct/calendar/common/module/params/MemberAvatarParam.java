/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberAvatarParam
 * Author:   Derek Xu
 * Date:     2022/3/2 11:24
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/2
 * @since 1.0.0
 */
@Data
public class MemberAvatarParam implements Serializable {

    @Schema(title = "头像地址")
    private String avatar;
}