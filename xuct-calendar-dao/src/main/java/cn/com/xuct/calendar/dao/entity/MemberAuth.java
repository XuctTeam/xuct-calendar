/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MemberAuth
 * Author:   Derek Xu
 * Date:     2021/12/14 9:01
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.dao.entity;

import cn.com.xuct.calendar.common.module.enums.IdentityTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/14
 * @since 1.0.0
 */
@Data
@TableName("sys_member_auth")
public class MemberAuth extends SuperEntity<MemberAuth> {

    @TableField("member_id")
    private Long memberId;

    @TableField("nick_name")
    private String nickName;

    private String avatar;
    /**
     * session Key
     */
    @JsonIgnore
    @TableField("session_key")
    private String sessionKey;

    @TableField("user_name")
    private String username;

    private String password;

    @TableField("identity_type")
    private IdentityTypeEnum identityType;

}