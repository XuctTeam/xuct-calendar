/**
 * Copyright (C), 2021-2021, 263
 * FileName: GuidUtil
 * Author:   Derek xu
 * Date:     2021/8/2 15:48
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.core.utils;

import java.util.UUID;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Administrator
 * @create 2021/8/2
 * @since 1.0.0
 */
public class GuidUtils {

    public static Long guid() {
        UUID uuid = UUID.randomUUID();
        Long guid = uuid.getMostSignificantBits() ^ uuid.getLeastSignificantBits();
        return guid < 0 ? 0 - guid : guid;
    }
}