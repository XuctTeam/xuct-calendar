/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: SmmsRes
 * Author:   Derek Xu
 * Date:     2022/2/12 20:31
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.res;

import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/12
 * @since 1.0.0
 */
@Data
public class SmmsRes implements Serializable {

    private Boolean success;

    private String code;

    private String message;
}