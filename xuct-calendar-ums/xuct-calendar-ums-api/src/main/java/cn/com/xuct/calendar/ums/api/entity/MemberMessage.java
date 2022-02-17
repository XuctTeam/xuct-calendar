/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberMessage
 * Author:   Derek Xu
 * Date:     2022/2/17 11:19
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.api.entity;

import cn.com.xuct.calendar.common.module.enums.MemberMessageTypeEnum;
import cn.com.xuct.calendar.dao.base.SuperEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import org.json.JSONObject;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/17
 * @since 1.0.0
 */
@Data
@TableName(value = "sys_member_message", autoResultMap = true)
public class MemberMessage extends SuperEntity<MemberMessage> {

    @TableField("member_id")
    private Long memberId;

    private MemberMessageTypeEnum type;

    /* 分类 */
    private Integer operation;


    private Integer status;

    /* 具体数据 */
    @TableField(value = "content", typeHandler = JacksonTypeHandler.class)
    private JSONObject content;
}