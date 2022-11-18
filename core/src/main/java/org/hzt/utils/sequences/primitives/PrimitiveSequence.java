package org.hzt.utils.sequences.primitives;

import org.hzt.utils.iterables.Indexable;
import org.hzt.utils.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

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

    PrimitiveSequence<T, C, F, P, B> plus(@NotNull Iterable<T> values);

    PrimitiveSequence<T, C, F, P, B> minus(@NotNull Iterable<T> values);

    PrimitiveSequence<T, C, F, P, B> filter(@NotNull P predicate);

    PrimitiveSequence<T, C, F, P, B> filterNot(@NotNull P predicate);

    PrimitiveSequence<T, C, F, P, B> map(@NotNull F mapper);

    PrimitiveSequence<T, C, F, P, B> take(long n);

    PrimitiveSequence<T, C, F, P, B> takeWhile(@NotNull P predicate);

    PrimitiveSequence<T, C, F, P, B> takeWhileInclusive(@NotNull P predicate);

    PrimitiveSequence<T, C, F, P, B> skip(long n);

    PrimitiveSequence<T, C, F, P, B> skipWhile(@NotNull P predicate);

    PrimitiveSequence<T, C, F, P, B> skipWhileInclusive(@NotNull P predicate);

    PrimitiveSequence<T, C, F, P, B> onEach(@NotNull C consumer);

    PrimitiveSequence<T, C, F, P, B> zip(@NotNull B merger, @NotNull Iterable<T> other);

    PrimitiveSequence<T, C, F, P, B> zipWithNext(@NotNull B merger);

}
