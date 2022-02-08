package hzt.sequences;

import hzt.PreConditions;
import hzt.collections.ArrayX;
import hzt.collections.IndexedValue;
import hzt.collections.ListX;
import hzt.collections.MutableListX;
import hzt.function.QuadFunction;
import hzt.function.TriFunction;
import hzt.iterables.IterableX;
import hzt.iterators.ArrayIterator;
import hzt.ranges.DoubleRange;
import hzt.ranges.IntRange;
import hzt.ranges.LongRange;
import hzt.strings.StringX;
import hzt.tuples.Pair;
import hzt.tuples.Triple;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * A sequence is a simplified stream. It evaluates its operations in a lazy way.
 * <p>
 * It does not support parallel execution.
 * <p>
 * The implementation is heavily inspired on Kotlin's sequences api. This api provides offers simpler syntax than streams
 * and is easier to understand
 *
 * @param <T> the type of the items in the Sequence
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface Sequence<T> extends IterableX<T> {

    static <T> Sequence<T> empty() {
        return new EmptySequence<>();
    }

    static <T> Sequence<T> of(@NotNull Iterable<T> iterable) {
        return iterable::iterator;
    }

    static <T> Sequence<T> of(@NotNull Stream<T> stream) {
        return stream::iterator;
    }

    static <K, V> EntrySequence<K, V> of(Map<K, V> map) {
        return map.entrySet()::iterator;
    }

    @SafeVarargs
    static <T> Sequence<T> of(T... values) {
        return () -> ArrayIterator.of(values);
    }

    static <T> Sequence<T> ofNullable(T value) {
        return value == null ? new EmptySequence<>() : Sequence.of(value);
    }

    static <T> Sequence<T> generate(T seedValue, UnaryOperator<T> nextFunction) {
        return seedValue == null ? new EmptySequence<>() : new GeneratorSequence<>(() -> seedValue, nextFunction);
    }

    static <T> Sequence<T> generate(Supplier<T> nextFunction) {
        return new GeneratorSequence<>(nextFunction, t -> nextFunction.get());
    }

    static <T> Sequence<T> generate(Supplier<T> seedFunction, UnaryOperator<T> nextFunction) {
        return new GeneratorSequence<>(seedFunction, nextFunction);
    }

    default Sequence<T> plus(@NotNull T... values) {
        return Sequence.of(this, Sequence.of(values)).flatMap(It::self);
    }

    default Sequence<T> plus(@NotNull Iterable<T> values) {
        return Sequence.of(this, Sequence.of(values)).flatMap(It::self);
    }

    default <R> Sequence<R> map(@NotNull Function<? super T, ? extends R> mapper) {
        return new TransformingSequence<>(this, mapper);
    }

    @Override
    default <R> Sequence<StringX> mapToStringX(@NotNull Function<? super T, ? extends R> function) {
        return mapNotNull(t -> StringX.of((t != null ? function.apply(t) : "").toString()));
    }

    default <R> Sequence<R> mapNotNull(@NotNull Function<? super T, ? extends R> mapper) {
        return new FilteringSequence<>(
                new TransformingSequence<>(
                        new FilteringSequence<>(this,
                                Objects::nonNull), mapper), Objects::nonNull);
    }

    @Override
    default <R> Sequence<R> mapIndexed(@NotNull BiFunction<Integer, ? super T, ? extends R> mapper) {
        return new TransformingIndexedSequence<>(this, mapper);
    }

    default <R> Sequence<R> flatMap(@NotNull Function<T, Iterable<R>> transform) {
        return new FlatteningSequence<>(this, t -> transform.apply(t).iterator());
    }

    default <R> Sequence<R> flatMapStream(@NotNull Function<T, Stream<R>> transform) {
        return new FlatteningSequence<>(this, t -> transform.apply(t).iterator());
    }

    default <R> Sequence<R> mapMulti(@NotNull BiConsumer<? super T, ? super Consumer<R>> mapper) {
        MutableListX<R> list = MutableListX.empty();
        for (T t : this) {
            mapper.accept(t, (Consumer<R>) list::add);
        }
        return list.asSequence();
    }

    @Override
    default <R> Sequence<R> castIfInstanceOf(@NotNull Class<R> aClass) {
        throw new UnsupportedOperationException();
    }

    default Sequence<T> filter(@NotNull Predicate<T> predicate) {
        return new FilteringSequence<>(this, predicate);
    }

    default <R> Sequence<T> filterBy(@NotNull Function<? super T, ? extends R> selector, @NotNull Predicate<R> predicate) {
        return filter(Objects::nonNull).filter(t -> predicate.test(selector.apply(t)));
    }

    @Override
    default Sequence<T> filterIndexed(@NotNull BiPredicate<Integer, T> predicate) {
        return new TransformingSequence<>(
                new FilteringSequence<>(withIndex(),
                        val -> predicate.test(val.index(), val.value())), IndexedValue::value);
    }

    default Sequence<T> filterNot(@NotNull Predicate<T> predicate) {
        return new FilteringSequence<>(this, predicate, false);
    }

    default Sequence<IndexedValue<T>> withIndex() {
        return this::indexedIterator;
    }

    @NotNull
    @Override
    default Sequence<T> onEach(@NotNull Consumer<? super T> consumer) {
        return onEach(It::self, consumer);
    }

    @NotNull
    @Override
    default <R> Sequence<T> onEach(@NotNull Function<? super T, ? extends R> selector, @NotNull Consumer<? super R> consumer) {
        return map(item -> {
            consumer.accept(selector.apply(item));
            return item;
        });
    }

    @NotNull
    default Sequence<T> distinct() {
        return distinctBy(It::self);
    }

    @NotNull
    default <R> Sequence<T> distinctBy(@NotNull Function<T, ? extends R> selector) {
        return new DistinctSequence<>(this, selector);
    }

    default Sequence<ListX<T>> chunked(int size) {
        return windowed(size, size, true);
    }

    default <R> Sequence<R> chunked(int size, @NotNull Function<? super ListX<T>, ? extends R> transform) {
        return windowed(size, size, true).map(transform);
    }

    default Sequence<ListX<T>> windowed(int size) {
        return windowed(size, 1);
    }

    default <R> Sequence<R> windowed(int size, @NotNull Function<? super ListX<T>, ? extends R> transform) {
        return windowed(size, 1).map(transform);
    }

    default Sequence<ListX<T>> windowed(int size, int step) {
        return windowed(size, step, false);
    }

    default <R> Sequence<R> windowed(int size, int step, @NotNull Function<? super ListX<T>, ? extends R> transform) {
        return windowed(size, step, false).map(transform);
    }

    default Sequence<ListX<T>> windowed(int size, boolean partialWindows) {
        return windowed(size, 1, partialWindows);
    }

    default Sequence<ListX<T>> windowed(int size, int step, boolean partialWindows) {
        return windowed(size, step, partialWindows, It::self);
    }

    default <R> Sequence<R> windowed(int size, int step, boolean partialWindows,
                                     @NotNull Function<? super ListX<T>, R> transform) {
        return new WindowedSequence<>(this, size, step, partialWindows).map(transform);
    }

    default <R> Sequence<R> zipWithNext(BiFunction<T, T, R> function) {
        return windowed(2, listX -> function.apply(listX.first(), listX.get(1)));
    }

    default <A, R> Sequence<R> zipWith(@NotNull Iterable<A> iterable, @NotNull BiFunction<? super T, ? super A, ? extends R> function) {
        return Sequence.of(zipToListWith(iterable, function));
    }

    @Override
    default Sequence<T> take(long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return new EmptySequence<>();
        } else if (this instanceof SkipTakeSequence) {
            SkipTakeSequence<T> skipTakeSequence = (SkipTakeSequence<T>) this;
            return skipTakeSequence.take(n);
        } else {
            return new TakeSequence<>(this, n);
        }
    }

    default Sequence<T> takeWhile(@NotNull Predicate<T> predicate) {
        return new TakeWhileSequence<>(this, predicate, false);
    }

    default Sequence<T> takeWhileInclusive(@NotNull Predicate<T> predicate) {
        return new TakeWhileSequence<>(this, predicate, true);
    }

    @Override
    default Sequence<T> skipWhile(@NotNull Predicate<T> predicate) {
        return new SkipWhileSequence<>(this, predicate, false);
    }

    @Override
    default Sequence<T> skipWhileInclusive(@NotNull Predicate<T> predicate) {
        return new SkipWhileSequence<>(this, predicate, true);
    }

    default Sequence<T> skip(long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return this;
        } else if (this instanceof SkipTakeSequence) {
            SkipTakeSequence<T> skipTakeSequence = (SkipTakeSequence<T>) this;
            return skipTakeSequence.skip(n);
        } else {
            return new SkipSequence<>(this, n);
        }
    }

    default <R extends Comparable<R>> Sequence<T> sortedBy(@NotNull Function<? super T, ? extends R> selector) {
        return toSortedListX(selector).asSequence();
    }

    default Sequence<T> sorted() {
        return IterableX.super.toSortedListX().asSequence();
    }

    default <K, V> EntrySequence<K, V> asEntrySequence(Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return EntrySequence.of(map(value -> Map.entry(keyMapper.apply(value), valueMapper.apply(value))));
    }

    @Override
    default IntRange asIntRange(@NotNull ToIntFunction<T> toIntMapper) {
        return IntRange.of(map(toIntMapper::applyAsInt));
    }

    @Override
    default LongRange asLongRange(@NotNull ToLongFunction<T> toLongMapper) {
        return LongRange.of(map(toLongMapper::applyAsLong));
    }

    @Override
    default DoubleRange asDoubleRange(@NotNull ToDoubleFunction<T> toDoubleMapper) {
        return DoubleRange.of(map(toDoubleMapper::applyAsDouble));
    }

    default T[] toArray(IntFunction<T[]> generator) {
        return toArrayOf(It::self, generator);
    }

    default ArrayX<T> toArrayX(IntFunction<T[]> generator) {
        return toArrayXOf(It::self, generator);
    }

    default <R> R transform(@NotNull Function<? super Sequence<T>, ? extends R> resultMapper) {
        return resultMapper.apply(this);
    }

    default <R1, R2, R> R toTwo(@NotNull Function<? super Sequence<T>, ? extends R1> resultMapper1,
                                @NotNull Function<? super Sequence<T>, ? extends R2> resultMapper2,
                                @NotNull BiFunction<R1, R2, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this));
    }

    default <R1, R2> Pair<R1, R2> toTwo(@NotNull Function<? super Sequence<T>, ? extends R1> resultMapper1,
                                        @NotNull Function<? super Sequence<T>, ? extends R2> resultMapper2) {
        return toTwo(resultMapper1, resultMapper2, Pair::of);
    }

    default <R1, R2, R3, R> R toThree(@NotNull Function<? super Sequence<T>, ? extends R1> resultMapper1,
                                      @NotNull Function<? super Sequence<T>, ? extends R2> resultMapper2,
                                      @NotNull Function<? super Sequence<T>, ? extends R3> resultMapper3,
                                      @NotNull TriFunction<R1, R2, R3, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this), resultMapper3.apply(this));
    }

    default <R1, R2, R3> Triple<R1, R2, R3> toThree(@NotNull Function<? super Sequence<T>, ? extends R1> resultMapper1,
                                                    @NotNull Function<? super Sequence<T>, ? extends R2> resultMapper2,
                                                    @NotNull Function<? super Sequence<T>, ? extends R3> resultMapper3) {
        return toThree(resultMapper1, resultMapper2, resultMapper3, Triple::of);
    }

    default <R1, R2, R3, R4, R> R toFour(@NotNull Function<? super Sequence<T>, ? extends R1> resultMapper1,
                                         @NotNull Function<? super Sequence<T>, ? extends R2> resultMapper2,
                                         @NotNull Function<? super Sequence<T>, ? extends R3> resultMapper3,
                                         @NotNull Function<? super Sequence<T>, ? extends R4> resultMapper4,
                                         @NotNull QuadFunction<R1, R2, R3, R4, R> merger) {
        final var r1 = resultMapper1.apply(this);
        final var r2 = resultMapper2.apply(this);
        final var r3 = resultMapper3.apply(this);
        final var r4 = resultMapper4.apply(this);
        return merger.apply(r1, r2, r3, r4);
    }
}
