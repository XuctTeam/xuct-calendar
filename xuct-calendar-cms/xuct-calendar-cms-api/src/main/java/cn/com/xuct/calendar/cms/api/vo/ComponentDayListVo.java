/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ComponentDayVo
 * Author:   Derek Xu
 * Date:     2022/1/24 17:52
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.api.vo;

import lombok.Data;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/24
 * @since 1.0.0
 */
@Data
public class ComponentDayListVo {

    private String day;

    private List<ComponentDayVo> components;
}