/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ComponentVo
 * Author:   Derek Xu
 * Date:     2022/1/10 20:12
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.api.vo;

import cn.com.xuct.calendar.cms.api.entity.Component;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/10
 * @since 1.0.0
 */
@Data
public class ComponentDayVo extends Component {

    private String color;

    private String calendarName;
}
