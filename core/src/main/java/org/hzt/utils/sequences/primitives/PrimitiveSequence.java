package org.hzt.utils.sequences.primitives;

import org.hzt.utils.iterables.Indexable;
import org.hzt.utils.sequences.Sequence;

/**
 * @param <T> The primitive type
 * @param <C> The PrimitiveConsumer
 * @param <F> The PrimitiveUnaryOperator
 * @param <P> The PrimitivePredicate
 * @param <B> The PrimitiveBinaryOperator
 */
public interface PrimitiveSequence<T, C, F, P, B> extends Indexable<T> {

    Sequence<T> boxed();

    PrimitiveSequence<T, C, F, P, B> distinct();

    PrimitiveSequence<T, C, F, P, B> plus(Iterable<T> values);

    PrimitiveSequence<T, C, F, P, B> minus(Iterable<T> values);

    PrimitiveSequence<T, C, F, P, B> filter(P predicate);

    PrimitiveSequence<T, C, F, P, B> filterNot(P predicate);

    PrimitiveSequence<T, C, F, P, B> map(F mapper);

    PrimitiveSequence<T, C, F, P, B> take(long n);

    PrimitiveSequence<T, C, F, P, B> takeWhile(P predicate);

    PrimitiveSequence<T, C, F, P, B> takeWhileInclusive(P predicate);

    PrimitiveSequence<T, C, F, P, B> skip(long n);

    PrimitiveSequence<T, C, F, P, B> skipWhile(P predicate);

    PrimitiveSequence<T, C, F, P, B> skipWhileInclusive(P predicate);

    PrimitiveSequence<T, C, F, P, B> onEach(C consumer);

    PrimitiveSequence<T, C, F, P, B> zip(B merger, Iterable<T> other);

    PrimitiveSequence<T, C, F, P, B> zipWithNext(B merger);

}
