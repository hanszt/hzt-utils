package org.hzt.utils.iterables.primitives;


import java.util.PrimitiveIterator;
import java.util.function.IntFunction;

@FunctionalInterface
public interface IntStringable extends PrimitiveIterable.OfInt {

    default String joinToString() {
        return joinToString(", ");
    }

    default String joinToString(CharSequence delimiter) {
        final StringBuilder sb = new StringBuilder();
        final PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.nextInt()).append(iterator.hasNext() ? delimiter : "");
        }
        return sb.toString().trim();
    }

    default <R> String joinToStringBy(IntFunction<? extends R> selector) {
        return joinToStringBy(selector, ", ");
    }

    default <R> String joinToStringBy(IntFunction<? extends R> selector, CharSequence delimiter) {
        final StringBuilder sb = new StringBuilder();
        final PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            final R r = selector.apply(iterator.nextInt());
            sb.append(r).append(iterator.hasNext() ? delimiter : "");
        }
        return sb.toString().trim();
    }
}
