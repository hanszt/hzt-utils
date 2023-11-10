package org.hzt.utils.iterables.primitives;


import java.util.PrimitiveIterator;
import java.util.function.LongFunction;

@FunctionalInterface
public interface LongStringable extends PrimitiveIterable.OfLong {

    default String joinToString() {
        return joinToString(", ");
    }

    default String joinToString(CharSequence delimiter) {
        final StringBuilder sb = new StringBuilder();
        final PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.nextLong()).append(iterator.hasNext() ? delimiter : "");
        }
        return sb.toString().trim();
    }

    default <R> String joinToStringBy(LongFunction<? extends R> selector) {
        return joinToStringBy(selector, ", ");
    }

    default <R> String joinToStringBy(LongFunction<? extends R> selector, CharSequence delimiter) {
        final StringBuilder sb = new StringBuilder();
        final PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            final R r = selector.apply(iterator.nextLong());
            sb.append(r).append(iterator.hasNext() ? delimiter : "");
        }
        return sb.toString().trim();
    }
}
