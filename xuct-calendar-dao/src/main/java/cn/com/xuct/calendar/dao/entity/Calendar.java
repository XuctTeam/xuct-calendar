/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: CalCarendar
 * Author:   Derek Xu
 * Date:     2021/11/23 13:12
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/23
 * @since 1.0.0
 */
@Data
@TableName("cms_calendar")
public class Calendar extends SuperEntity<Calendar> {

    /* 用户ID */
    @TableField("member_id")
    private Long memberId;

    /* 提醒方式  0 站内信 1 邮箱 2 公众号 3短信 */
    @TableField("alarm_type")
    private int alarmType;

    /* 提醒时间0 15 30 60 */
    @TableField("alarm_time")
    private int alarmTime;

    /* 是否可分享 */
    @TableField("is_share")
    private int isShare;
}