package org.hzt.utils.sequences.primitives;

import org.hzt.utils.iterables.Stringable;
import org.hzt.utils.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

/**
 * @param <T> The number type
 * @param <C> The NumberConsumer
 * @param <F> The NumberUnaryOperator
 * @param <P> The NumberPredicate
 * @param <B> The NumberBinaryOperator
 */
public interface PrimitiveSequence<T extends Number, C, F, P, B> extends Stringable<T> {

    Sequence<T> boxed();

    PrimitiveSequence<T, C, F, P, B> plus(@NotNull Iterable<T> values);

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
