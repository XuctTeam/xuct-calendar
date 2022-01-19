/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: RabbitmqMessageBody
 * Author:   Administrator
 * Date:     2022/1/3 20:08
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/3
 * @since 1.0.0
 */
@Setter
@Getter
public class RabbitmqMessageBody implements Serializable {


    private String type;


    private String payload;

}