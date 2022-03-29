/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: UserInfoDto
 * Author:   Derek Xu
 * Date:     2021/11/15 12:51
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.feign;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/15
 * @since 1.0.0
 */
@Data
public class UserInfoFeignInfo implements Serializable {

    private Long userId;

    private String username;

    private String password;

    private Integer status;

    private String timeZone;

    private List<String> roles;
}