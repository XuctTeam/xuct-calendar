package cn.com.xuct.calendar.common.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

public final class EnumUtil {
    private static final Logger log = LoggerFactory.getLogger(EnumUtil.class);

    public static <T extends Enum<T>> T evalueOf(final T def, final String val) {
        if (null == val || val.trim().isEmpty()) {
            return def;
        }
        final T ret = evalueOf(def.getDeclaringClass(), val);
        return (null == ret) ? def : ret;
    }

    public static <T extends Enum<T>> T evalueOf(final T def, final int index) {
        final T ret = evalueOf(def.getDeclaringClass(), index);
        return (null == ret) ? def : ret;
    }

    public static <T extends Enum<T>, E extends Enum<E>> T evalueOf(final T def, final E val) {
        if (null == val) {
            return def;
        }
        final T ret = evalueOf(def.getDeclaringClass(), val.name());
        return (null == ret) ? def : ret;
    }

    public static <T extends Enum<T>> T evalueOf(final Class<T> enumType, final Object val) {
        if (null == val) {
            return null;
        }

        if (val instanceof Integer) {
            return EnumUtil.evalueOf(enumType, ((Integer) val).intValue());
        }

        if (val instanceof Long) {
            return EnumUtil.evalueOf(enumType, ((Long) val).intValue());
        }

        String str = "";
        if (val instanceof Enum) {
            str = ((Enum<?>) val).name();
        } else if (val instanceof String) {
            str = (String) val;
        } else {
            str = val.toString();
        }

        try {
            final T ret = Enum.valueOf(enumType, str.trim().toUpperCase().replace('-', '_'));
            if (ret != null) {
                return ret;
            }
        } catch (final Exception e) {
            //
        }
        try {
            return evalueOf(enumType, Integer.parseInt(str.trim()));
        } catch (final Exception e) {
            //
        }
        return null;
    }

    public static <T extends Enum<T>> T evalueOf(final Class<T> enumType, final int index) {
        final int idx = Math.max(index, 0);

        try {
            return enumType.getEnumConstants()[idx];
        } catch (final Exception e) {
            log.error("", e);
        }

        try {
            return enumType.getEnumConstants()[0];
        } catch (final Exception e) {
            log.error("", e);
        }

        return null;
    }


    public static <E extends Enum<E>> Collection<Integer> toIntArray(final EnumSet<E> vals) {
        final ArrayList<Integer> vec = new ArrayList<>();

        if (null == vals || vals.isEmpty()) {
            return vec;
        }

        for (final Enum<E> val : vals) {
            vec.add(Integer.valueOf(val.ordinal()));
        }

        return vec;
    }

    @SafeVarargs
    public static <E extends Enum<E>> EnumSet<E> toEnumSet(final E... vals) {
        if (null == vals || vals.length == 0) {
            return null;
        }
        final EnumSet<E> eset = EnumSet.noneOf(vals[0].getDeclaringClass());
        for (final E val : vals) {
            eset.add(val);
        }
        return eset;
    }
}
