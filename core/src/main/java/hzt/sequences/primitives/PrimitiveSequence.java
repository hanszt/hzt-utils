package hzt.sequences.primitives;

import hzt.iterables.Groupable;
import hzt.iterables.Stringable;
import hzt.sequences.Sequence;
import hzt.statistics.NumberStatistics;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * @param <T> The number type
 * @param <C> The NumberConsumer
 * @param <F> The NumberUnaryOperator
 * @param <P> The NumberPredicate
 * @param <B> The NumberBinaryOperator
 */
public interface PrimitiveSequence<T extends Number, C, F, P, B> extends Stringable<T>, Groupable<T> {

    Sequence<T> boxed();

    <R> Sequence<R> mapToObj(@NotNull Function<T, R> function);

    PrimitiveSequence<T, C, F, P, B> filter(@NotNull P predicate);

    PrimitiveSequence<T, C, F, P, B> filterNot(@NotNull P predicate);

    PrimitiveSequence<T, C, F, P, B> map(@NotNull F mapper);

    PrimitiveSequence<T, C, F, P, B> take(long n);

    PrimitiveSequence<T, C, F, P, B> takeWhile(@NotNull P predicate);

    PrimitiveSequence<T, C, F, P, B> takeWhileInclusive(@NotNull P predicate);

    PrimitiveSequence<T, C, F, P, B> skip(long n);

    PrimitiveSequence<T, C, F, P, B> skipWhile(@NotNull P predicate);

    PrimitiveSequence<T, C, F, P, B> skipWhileInclusive(@NotNull P predicate);

    PrimitiveSequence<T, C, F, P, B> sorted();

    PrimitiveSequence<T, C, F, P, B> sortedDescending();

    PrimitiveSequence<T, C, F, P, B> onEach(@NotNull C consumer);

    PrimitiveSequence<T, C, F, P, B> zip(@NotNull B merger, @NotNull Iterable<T> other);

    @NotNull NumberStatistics stats();

    @NotNull NumberStatistics stats(@NotNull P predicate);

}
