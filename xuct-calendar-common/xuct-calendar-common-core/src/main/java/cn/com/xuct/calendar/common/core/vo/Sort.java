/**
 * Copyright (C), 2015-2020, xuct.net
 * FileName: Sort
 * Author:   xutao
 * Date:     2020/2/3 16:21
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.core.vo;

import cn.com.xuct.calendar.common.core.enums.SortEnum;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author user
 * @create 2020/2/3
 * @since 1.0.0
 */
@Data
public class Sort {

    private String column;

    private SortEnum sortEnum;


    public static Sort of(String column, SortEnum sortEnum) {
        Sort sort = new Sort();
        sort.setColumn(column);
        sort.setSortEnum(sortEnum);
        return sort;
    }
}