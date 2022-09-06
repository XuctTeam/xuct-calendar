/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: UserRoleInfo
 * Author:   Derek Xu
 * Date:     2022/9/6 16:36
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.feign;

import lombok.Data;

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
public class UserRoleInfo implements Serializable {

    private String id;

    private String name;
}