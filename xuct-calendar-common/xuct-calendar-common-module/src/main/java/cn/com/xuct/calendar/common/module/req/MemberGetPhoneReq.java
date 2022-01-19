/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MemberGetPhoneReq
 * Author:   Derek Xu
 * Date:     2021/12/15 15:30
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.req;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/15
 * @since 1.0.0
 */
@Data
public class MemberGetPhoneReq {

    private String encryptedData;

    private String ivStr;
}