package org.hzt.utils.iterables.primitives;


import java.util.function.IntFunction;

@FunctionalInterface
public interface IntStringable extends PrimitiveIterable.OfInt {

    default String joinToString() {
        return joinToString(", ");
    }

    default String joinToString(final CharSequence delimiter) {
        final var sb = new StringBuilder();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.nextInt()).append(iterator.hasNext() ? delimiter : "");
        }
        return sb.toString().trim();
    }

    default <R> String joinToStringBy(final IntFunction<? extends R> selector) {
        return joinToStringBy(selector, ", ");
    }

    default <R> String joinToStringBy(final IntFunction<? extends R> selector, final CharSequence delimiter) {
        final var sb = new StringBuilder();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var r = selector.apply(iterator.nextInt());
            sb.append(r).append(iterator.hasNext() ? delimiter : "");
        }
        return sb.toString().trim();
    }
}
