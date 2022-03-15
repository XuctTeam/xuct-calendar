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
import cn.com.xuct.calendar.common.db.dao.base.SuperEntity;
import cn.com.xuct.calendar.ums.api.handler.JsonObjectStdSerializer;
import cn.com.xuct.calendar.ums.api.handler.JsonObjectTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
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
@Accessors(chain = true)
@TableName(value = "sys_member_message" , autoResultMap = true)
public class MemberMessage extends SuperEntity<MemberMessage> {

    @TableField("member_id")
    private Long memberId;

    private MemberMessageTypeEnum type;

    /* 类型 */
    private Integer operation;

    private Integer status;

    @TableField(typeHandler = JsonObjectTypeHandler.class)
    @JsonSerialize(using = JsonObjectStdSerializer.class)
    private JSONObject content;

}

