/**
 * Copyright (C), 2015-2020, xuct.net
 * FileName: Column
 * Author:   xutao
 * Date:     2020/2/3 16:37
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.core.vo;

import cn.com.xuct.calendar.common.core.enums.ColumnEnum;
import com.google.common.collect.Sets;
import lombok.Data;

import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author user
 * @create 2020/2/3
 * @since 1.0.0
 */
@Data
public class Column {

    private String column;

    private Set<Object> value = Sets.newHashSet();

    private ColumnEnum columnEnum;

    private Column() {
    }


    public static Column of(String column, Object value) {
        Column c = new Column();
        c.setColumn(column);
        c.getValue().add(value);
        c.setColumnEnum(ColumnEnum.eq);
        return c;
    }

    /**
     * 功能描述: <br>
     * 〈〉
     *
     * @param column
     * @param columnEnum
     * @return:com.net263.vcs.api.server.common.vo.Column
     * @since: 1.0.0
     * @Author:
     * @Date: 2020/4/2 13:31
     */
    public static Column of(String column, ColumnEnum columnEnum) {
        Column c = new Column();
        c.setColumn(column);
        c.setColumnEnum(columnEnum);
        return c;
    }

    /**
     * 功能描述: <br>
     * 〈〉
     *
     * @param column
     * @param value
     * @param columnEnum
     * @return:com.net263.vcs.api.server.common.vo.Column
     * @since: 1.0.0
     * @Author:
     * @Date: 2020/2/3 16:42
     */
    public static Column of(String column, Object value, ColumnEnum columnEnum) {
        Column c = new Column();
        c.setColumn(column);
        c.getValue().add(value);
        c.setColumnEnum(columnEnum);
        return c;
    }

    /**
     * 功能描述: <br>
     * 〈添加值〉
     *
     * @param value
     * @return:com.net263.vcs.api.server.common.vo.Column
     * @since: 1.0.0
     * @Author:
     * @Date: 2020/4/2 14:00
     */
    public Column addValue(Object value) {
        this.getValue().add(value);
        return this;
    }
}