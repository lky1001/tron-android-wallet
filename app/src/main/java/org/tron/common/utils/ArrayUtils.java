package org.tron.common.utils;

import java.lang.reflect.Array;

public class ArrayUtils {

    public static boolean isEmpty(final byte[] array) {
        return getLength(array) == 0;
    }

    public static boolean isEmpty(final char[] array) {
        return getLength(array) == 0;
    }

    public static int getLength(final Object array) {
        if (array == null) {
            return 0;
        }
        return Array.getLength(array);
    }

}
