package org.hzt.utils.iterables.primitives;

import org.jetbrains.annotations.NotNull;

import java.util.function.LongFunction;

@FunctionalInterface
public interface LongStringable extends PrimitiveIterable.OfLong {

    default String joinToString() {
        return joinToString(", ");
    }

    default String joinToString(CharSequence delimiter) {
        final var sb = new StringBuilder();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.nextLong()).append(iterator.hasNext() ? delimiter : "");
        }
        return sb.toString().trim();
    }

    default <R> String joinToStringBy(@NotNull LongFunction<? extends R> selector) {
        return joinToStringBy(selector, ", ");
    }

    default <R> String joinToStringBy(@NotNull LongFunction<? extends R> selector, CharSequence delimiter) {
        final var sb = new StringBuilder();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var r = selector.apply(iterator.nextLong());
            sb.append(r).append(iterator.hasNext() ? delimiter : "");
        }
        return sb.toString().trim();
    }
}
