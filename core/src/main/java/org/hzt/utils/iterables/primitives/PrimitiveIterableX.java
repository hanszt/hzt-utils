package org.hzt.utils.iterables.primitives;

import org.hzt.utils.iterables.Indexable;

/**
 * @param <T> The primitive type
 * @param <C> The PrimitiveConsumer
 * @param <F> The PrimitiveUnaryOperator
 * @param <P> The PrimitivePredicate
 * @param <B> The PrimitiveBinaryOperator
 */
public interface PrimitiveIterableX<T, C, F, P, B> extends Indexable<T> {

    Iterable<T> boxed();

    PrimitiveIterableX<T, C, F, P, B> distinct();
    PrimitiveIterableX<T, C, F, P, B> distinctBy(F selector);

    PrimitiveIterableX<T, C, F, P, B> plus(Iterable<T> values);

    PrimitiveIterableX<T, C, F, P, B> minus(Iterable<T> values);

    PrimitiveIterableX<T, C, F, P, B> filter(P predicate);

    PrimitiveIterableX<T, C, F, P, B> filterNot(P predicate);

    PrimitiveIterableX<T, C, F, P, B> map(F mapper);

    PrimitiveIterableX<T, C, F, P, B> take(long n);

    PrimitiveIterableX<T, C, F, P, B> takeWhile(P predicate);

    PrimitiveIterableX<T, C, F, P, B> takeWhileInclusive(P predicate);

    PrimitiveIterableX<T, C, F, P, B> skip(long n);

    PrimitiveIterableX<T, C, F, P, B> skipWhile(P predicate);

    PrimitiveIterableX<T, C, F, P, B> skipWhileInclusive(P predicate);

    PrimitiveIterableX<T, C, F, P, B> onEach(C consumer);

    PrimitiveIterableX<T, C, F, P, B> zip(B merger, Iterable<T> other);

    PrimitiveIterableX<T, C, F, P, B> zipWithNext(B merger);

}
