/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: PinYinUtils
 * Author:   Derek Xu
 * Date:     2022/3/1 11:41
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.core.utils;

import lombok.NonNull;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import javax.annotation.Nullable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/1
 * @since 1.0.0
 */
public class PinYinUtils {

    @NonNull
    public static String first(@Nullable String str) {
        if (str == null || str.equals("")) {
            return "#";
        }
        char ch = str.charAt(0);
        if (ch >= 'a' && ch <= 'z') {
            return (char) (ch - 'a' + 'A') + "";
        }
        if (ch >= 'A' && ch <= 'Z') {
            return ch + "";
        }
        try {
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            // 设置大小写格式
            defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            // 设置声调格式：
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            if (Character.toString(ch).matches("[\\u4E00-\\u9FA5]+")) {
                String[] array = PinyinHelper.toHanyuPinyinStringArray(ch, defaultFormat);
                if (array != null) {
                    return array[0].charAt(0) + "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "#";
    }
}