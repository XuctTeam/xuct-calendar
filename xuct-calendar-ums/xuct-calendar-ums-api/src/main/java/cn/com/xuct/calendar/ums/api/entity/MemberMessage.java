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
import io.swagger.annotations.ApiModelProperty;
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
@TableName(value = "sys_member_message", autoResultMap = true)
public class MemberMessage extends SuperEntity<MemberMessage> {


    private String title;

    @TableField("member_id")
    private Long memberId;

    @ApiModelProperty("分类")
    private MemberMessageTypeEnum type;

    /* 类型 */
    @ApiModelProperty(value = "类型", notes = "type=EVENT:0.新建邀请 1.更新邀请 2.事件删除 3.事件提醒 type=GROUP 0申请入组 1申请同意 2申请不同意 3.撤回申请 4.主动离开 5.踢出群组 6.群组删除 type=SYSTEM:0")
    private Integer operation;

    @ApiModelProperty(value = "状态", notes = "0未读 1.已读")
    private Integer status;

    @TableField(typeHandler = JsonObjectTypeHandler.class)
    @JsonSerialize(using = JsonObjectStdSerializer.class)
    private JSONObject content;

}

