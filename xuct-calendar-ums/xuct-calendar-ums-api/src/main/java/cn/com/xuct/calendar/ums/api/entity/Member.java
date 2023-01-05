/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: User
 * Author:   Derek Xu
 * Date:     2021/11/10 12:44
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.api.entity;

import cn.com.xuct.calendar.common.db.dao.base.SuperEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/10
 * @since 1.0.0
 */
@Data
@TableName("sys_member")
public class Member extends SuperEntity<Member> {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "状态 0正常 1冻结 2删除")
    private int status;

    @Schema(description = "时区")
    @TableField("time_zone")
    private String timeZone = "east_8";

    @Schema(description = "组织")
    private String organization;
}