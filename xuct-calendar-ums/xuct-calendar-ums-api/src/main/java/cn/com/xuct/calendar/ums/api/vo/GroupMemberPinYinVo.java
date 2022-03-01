/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: GroupMemberPinYinVo
 * Author:   Derek Xu
 * Date:     2022/3/1 11:44
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.api.vo;

import cn.com.xuct.calendar.ums.api.dto.GroupMemberInfoDto;
import lombok.Data;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/1
 * @since 1.0.0
 */
@Data
public class GroupMemberPinYinVo {

    private String charCode;

    private List<GroupMemberInfoDto> members;
}