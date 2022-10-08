/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ComponentListVo
 * Author:   Derek Xu
 * Date:     2022/1/7 11:46
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.api.vo;

import cn.com.xuct.calendar.cms.api.entity.Component;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/7
 * @since 1.0.0
 */
@Data
public class ComponentListVo {

    @Schema(name = "日程发生天")
    private String day;

    @Schema(name ="日历ID")
    private String calendarId;

    @Schema(name ="日程列表")
    private List<Component> components = Lists.newArrayList();
}