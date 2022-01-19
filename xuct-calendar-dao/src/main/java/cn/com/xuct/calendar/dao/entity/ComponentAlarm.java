/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ComponentAlarm
 * Author:   Derek Xu
 * Date:     2022/1/4 9:20
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.dao.entity;

import cn.com.xuct.calendar.common.module.enums.CommonStatusEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/4
 * @since 1.0.0
 */
@Data
@TableName("cms_component_alarm")
public class ComponentAlarm extends SuperEntity<ComponentAlarm> {

    @TableField("component_id")
    private Long componentId;

    @TableField("calendar_id")
    private Long calendarId;

    @TableField("create_member_id")
    private Long createMemberId;

    private CommonStatusEnum status;

    @TableField("alarm_time")
    private Date alarmTime;
    /**
     * 间隔时间
     */
    @TableField("trigger_sec")
    private Integer triggerSec;
}