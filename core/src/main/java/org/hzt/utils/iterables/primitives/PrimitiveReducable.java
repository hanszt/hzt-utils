package org.hzt.utils.iterables.primitives;


/**
 * @param <T> The numberType
 * @param <B> The primitive binaryOperator
 * @param <P> The primitive predicate
 * @param <O> The primitive optional
 */
interface PrimitiveReducable<T, B, P, O> extends Iterable<T> {

    O reduce(B operator);

    O findFirst();

    O findFirst(P predicate);

    O findLast();

    O findLast(P predicate);

    boolean any(P predicate);

    default boolean any() {
        return iterator().hasNext();
    }

    boolean all(P predicate);

    boolean none(P predicate);

    default boolean none() {
        return !iterator().hasNext();
    }
}
