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
import lombok.Data;

import javax.validation.constraints.NotNull;
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

    private Long id;

    private String imageUrl;

    @NotNull
    private String name;

    @NotNull
    private String power;

    private String password;
}