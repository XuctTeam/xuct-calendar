/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ComponentAlarmEnum
 * Author:   Derek Xu
 * Date:     2022/1/4 9:28
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
 * @create 2022/1/4
 * @since 1.0.0
 */
public enum ComponentAlarmEnum {

    UNKNOWN("0"),

    INTERNAL_MESSAGE("1"),

    MAIL("2"),

    OFFICIAL_ACCOUNT("3"),

    SMS_CODE("4");


    private String code;


    ComponentAlarmEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ComponentAlarmEnum getByCode(String code) {
        for (ComponentAlarmEnum s : ComponentAlarmEnum.values()) {
            if (code.equals(s.getCode())) {
                return s;
            }
        }
        return null;
    }
}