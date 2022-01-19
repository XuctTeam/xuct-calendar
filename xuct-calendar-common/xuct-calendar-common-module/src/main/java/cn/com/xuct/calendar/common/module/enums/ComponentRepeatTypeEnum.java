/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: ComponentRepeatTypeEnum
 * Author:   Derek Xu
 * Date:     2021/12/21 13:28
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.enums;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/21
 * @since 1.0.0
 */
public enum ComponentRepeatTypeEnum implements CommonEnum {

    UNKNOWN(0, "UNKNOWN"),

    DAILY(1, "DAILY"),

    WEEKLY(2, "WEEKLY"),

    MONTHLY(3, "MONTHLY"),

    YEARLY(4, "YEARLY");

    private Integer code;

    private String value;

    ComponentRepeatTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getValue() {
        return this.value;
    }


    public static ComponentRepeatTypeEnum getValueByValue(String value) {
        return Enum.valueOf(ComponentRepeatTypeEnum.class, value);
    }
}