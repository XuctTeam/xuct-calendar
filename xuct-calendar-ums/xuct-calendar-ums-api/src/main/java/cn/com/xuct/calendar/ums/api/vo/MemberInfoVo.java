/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MemberInfoVo
 * Author:   Derek Xu
 * Date:     2021/12/14 13:52
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.api.vo;

import cn.com.xuct.calendar.ums.api.entity.Member;
import cn.com.xuct.calendar.ums.api.entity.MemberAuth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/14
 * @since 1.0.0
 */
@Data
public class MemberInfoVo {

    @Schema(description = "用户信息")
    private Member member;

    @Schema(description = "用户认证方式")
    private List<MemberAuth> auths;
}