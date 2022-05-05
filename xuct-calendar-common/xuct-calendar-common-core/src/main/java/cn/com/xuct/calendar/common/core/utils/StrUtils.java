package cn.com.xuct.calendar.common.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by xiaohua on 2019/1/15.
 * <p>
 * $Id$
 */
@Slf4j
public class StrUtils {


    private static final char[] UUID_CHAR_TABLE = new char[]{
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };


    public synchronized static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getShortUuid() {
        StringBuilder sb = new StringBuilder();
        String uuid = getUuid();
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            sb.append(UUID_CHAR_TABLE[x % UUID_CHAR_TABLE.length]);
        }
        return sb.toString();
    }

    public static String getMiniUuid() {
        StringBuilder sb = new StringBuilder();
        String uuid = getUuid() + "0000";
        for (int i = 0; i < 6; i++) {
            String str = uuid.substring(i * 6, i * 6 + 6);
            int x = Integer.parseInt(str, 16);
            sb.append(UUID_CHAR_TABLE[x % UUID_CHAR_TABLE.length]);
        }
        return sb.toString();
    }


    public static <T> String concatBySeparator(final Collection<T> vals) {
        return concatBySeparator(vals, ", ");
    }

    public static <T> String concatBySeparator(final Collection<T> vals, final String separator) {
        return concatBySeparator(vals, separator, "", "");
    }

    public static <T> String concatBySeparator(final Collection<T> vals, final String separator, final String prefix,
                                               final String suffix) {
        if (null == vals || vals.isEmpty()) {
            return "";
        }

        int flag = 0;
        final StringBuffer buf = new StringBuffer();

        final String prefixStr = (null == prefix) ? "" : prefix;
        final String suffixStr = (null == suffix) ? "" : suffix;
        final String separatorStr = (null == separator) ? "" : separator;

        try {
            for (final T val : vals) {
                if (flag > 0) {
                    buf.append(separatorStr);
                }

                buf.append(prefixStr);
                buf.append(val);
                buf.append(suffixStr);

                flag++;
            }
        } catch (final Exception e) {
            log.error("", e);
        }

        return buf.toString();
    }

    public static void main(String[] args) {
        System.out.println(getUuid());
        System.out.println(getShortUuid());
        System.out.println(getMiniUuid());
    }
}
