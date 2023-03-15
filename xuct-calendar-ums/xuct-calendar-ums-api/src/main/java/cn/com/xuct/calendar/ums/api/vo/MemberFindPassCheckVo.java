/**
 * Copyright (C), 2015-2023, XXX有限公司
 * FileName: MemberFindPassCheckVo
 * Author:   Derek Xu
 * Date:     2023/3/15 9:33
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.api.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2023/3/15
 * @since 1.0.0
 */
@Data
@Builder
public class MemberFindPassCheckVo implements Serializable {

    private String memberId;

    private String code;
}