package org.hzt.utils.iterables.primitives;

import org.jetbrains.annotations.NotNull;

/**
 * @param <T> The numberType
 * @param <B> The primitive binaryOperator
 * @param <P> The primitive predicate
 * @param <O> The primitive optional
 */
interface PrimitiveReducable<T, B, P, O> extends Iterable<T> {

    @NotNull O reduce(@NotNull B operator);

    O findFirst();

    O findFirst(P predicate);

    O findLast();

    O findLast(P predicate);

    boolean any(@NotNull P predicate);

    default boolean any() {
        return iterator().hasNext();
    }

    boolean all(@NotNull P predicate);

    boolean none(@NotNull P predicate);

    default boolean none() {
        return !iterator().hasNext();
    }
}
