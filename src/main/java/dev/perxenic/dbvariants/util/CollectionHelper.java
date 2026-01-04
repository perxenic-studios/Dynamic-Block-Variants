package dev.perxenic.dbvariants.util;

import org.jetbrains.annotations.Nullable;

public class CollectionHelper {
    public static <T> @Nullable T getFirstArray(T[] array) {
        if (array.length < 1) return null;
        return array[0];
    }

    public static <T> @Nullable T getLastArray(T[] array) {
        if (array.length < 1) return null;
        return array[array.length - 1];
    }
}
