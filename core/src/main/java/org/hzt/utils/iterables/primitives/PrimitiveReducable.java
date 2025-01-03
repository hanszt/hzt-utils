package org.hzt.utils.iterables.primitives;


/**
 * @param <T> The boxed version of the primitive type
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

    boolean all(P predicate);

    boolean none(P predicate);

    default boolean any() {
        return iterator().hasNext();
    }

    default boolean none() {
        return !iterator().hasNext();
    }
}
