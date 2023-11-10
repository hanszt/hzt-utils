package org.hzt.utils.sequences;

import org.hzt.utils.PreConditions;

import java.util.Map;

public final class SequenceHelper {

    private SequenceHelper() {
    }

    private static String getErrorMessage(final int size, final int step) {
        if (size != step) {
            return "Both size " + size + " and step " + step + " must be greater than zero.";
        }
        return "size " + size + " must be greater than zero.";
    }

    public static void checkInitWindowSizeAndStep(final int size, final int step) {
        PreConditions.require(size > 0 && step > 0, () -> getErrorMessage(size, step));
    }

    static <K, V> V keyAsValueTypeOrThrow(final Map.Entry<K, V> entry) {
        final K k = entry.getKey();
        final V v = entry.getValue();
        if (k.getClass() == v.getClass()) {
            //noinspection unchecked
            return (V) k;
        }
        throw new IllegalStateException("Key and value not of same type. Merge not allowed");
    }
}
