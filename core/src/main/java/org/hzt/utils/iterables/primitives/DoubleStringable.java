package org.hzt.utils.iterables.primitives;

import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.DoubleFunction;

@FunctionalInterface
public interface DoubleStringable extends PrimitiveIterable.OfDouble {

    default String joinToString() {
        return joinToString(", ");
    }

    default String joinToString(CharSequence delimiter) {
        final StringBuilder sb = new StringBuilder();
        final PrimitiveIterator.OfDouble iterator = iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.nextDouble()).append(iterator.hasNext() ? delimiter : "");
        }
        return sb.toString().trim();
    }
    
    default <R> String joinToStringBy(@NotNull DoubleFunction<? extends R> selector) {
        return joinToStringBy(selector, ", ");
    }

    default <R> String joinToStringBy(@NotNull DoubleFunction<? extends R> selector, CharSequence delimiter) {
        final StringBuilder sb = new StringBuilder();
        final PrimitiveIterator.OfDouble iterator = iterator();
        while (iterator.hasNext()) {
            final R r = selector.apply(iterator.nextDouble());
            sb.append(r).append(iterator.hasNext() ? delimiter : "");
        }
        return sb.toString().trim();
    }
}
