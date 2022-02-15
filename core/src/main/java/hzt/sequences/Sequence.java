package hzt.sequences;

import hzt.PreConditions;
import hzt.collections.ListX;
import hzt.collections.MapX;
import hzt.function.QuadFunction;
import hzt.function.TriFunction;
import hzt.iterables.IterableX;
import hzt.iterables.IterableXHelper;
import hzt.iterators.ArrayIterator;
import hzt.iterators.FilteringIterator;
import hzt.iterators.FlatteningIterator;
import hzt.iterators.GeneratorIterator;
import hzt.iterators.MultiMappingIterator;
import hzt.iterators.SkipWhileIterator;
import hzt.iterators.TakeWhileIterator;
import hzt.ranges.DoubleRange;
import hzt.ranges.IntRange;
import hzt.ranges.LongRange;
import hzt.strings.StringX;
import hzt.tuples.IndexedValue;
import hzt.tuples.Pair;
import hzt.tuples.Triple;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Comparator;
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

    static <K, V> EntrySequence<K, V> of(MapX<K, V> map) {
        return map.entrySet()::iterator;
    }

    @SafeVarargs
    static <T> Sequence<T> of(T... values) {
        return () -> ArrayIterator.of(values);
    }

    static <T> Sequence<T> ofNullable(T value) {
        return value != null ? Sequence.of(value) : new EmptySequence<>();
    }

    static <T> Sequence<T> generate(T seedValue, UnaryOperator<T> nextFunction) {
        return seedValue == null ? new EmptySequence<>() : (() -> GeneratorIterator.of(() -> seedValue, nextFunction));
    }

    static <T> Sequence<T> generate(Supplier<T> nextFunction) {
        return () -> GeneratorIterator.of(nextFunction, t -> nextFunction.get());
    }

    static <T> Sequence<T> generate(Supplier<T> seedFunction, UnaryOperator<T> nextFunction) {
        return () -> GeneratorIterator.of(seedFunction, nextFunction);
    }

    default Sequence<T> plus(@NotNull T value) {
        return Sequence.of(this, Sequence.of(value)).flatMap(It::self);
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
        return () -> FlatteningIterator.of(iterator(), t -> transform.apply(t).iterator());
    }

    default <R> Sequence<R> flatMapStream(@NotNull Function<T, Stream<R>> transform) {
        return () -> FlatteningIterator.of(iterator(), t -> transform.apply(t).iterator());
    }

    default <R> Sequence<R> mapMulti(@NotNull BiConsumer<? super T, ? super Consumer<R>> mapper) {
        return () -> MultiMappingIterator.of(iterator(), mapper);
    }

    @Override
    default <R> Sequence<R> castIfInstanceOf(@NotNull Class<R> aClass) {
        return filter(aClass::isInstance).map(aClass::cast);
    }

    default Sequence<T> filter(@NotNull Predicate<T> predicate) {
        return () -> FilteringIterator.of(iterator(), predicate);
    }

    default Sequence<T> filterNot(@NotNull Predicate<T> predicate) {
        return () -> FilteringIterator.of(iterator(), predicate, false);
    }

    default <R> Sequence<T> filterBy(@NotNull Function<? super T, ? extends R> selector, @NotNull Predicate<R> predicate) {
        return filter(Objects::nonNull).filter(t -> predicate.test(selector.apply(t)));
    }

    @Override
    default Sequence<T> filterIndexed(@NotNull BiPredicate<Integer, T> predicate) {
        return withIndex()
                .filter(indexedValue -> predicate.test(indexedValue.index(), indexedValue.value()))
                .map(IndexedValue::value);
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

    @Override
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

    default Sequence<Pair<T, T>> zipWithNext() {
        return zipWithNext(Pair::of);
    }

    default <R> Sequence<R> zipWithNext(BiFunction<T, T, R> function) {
        return windowed(2, listX -> function.apply(listX.first(), listX.get(1)));
    }

    @Override
    default <R> Sequence<Pair<T, R>> zip(@NotNull Iterable<R> iterable) {
        return zip(iterable, Pair::of);
    }

    default <A, R> Sequence<R> zip(@NotNull Iterable<A> other, @NotNull BiFunction<? super T, ? super A, ? extends R> function) {
        return () -> SequenceHelper.mergingIterator(iterator(), other.iterator(), function);
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
        return () -> TakeWhileIterator.of(iterator(), predicate, false);
    }

    default Sequence<T> takeWhileInclusive(@NotNull Predicate<T> predicate) {
        return () -> TakeWhileIterator.of(iterator(), predicate, true);
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

    @Override
    default Sequence<T> skipWhile(@NotNull Predicate<T> predicate) {
        return () -> SkipWhileIterator.of(iterator(), predicate, false);
    }

    @Override
    default Sequence<T> skipWhileInclusive(@NotNull Predicate<T> predicate) {
        return () -> SkipWhileIterator.of(iterator(), predicate, true);
    }

    @Override
    default Sequence<T> sorted() {
        return Sequence.of(IterableX.super.sorted());
    }

    @Override
    default <R extends Comparable<R>> Sequence<T> sortedBy(@NotNull Function<? super T, ? extends R> selector) {
        return Sequence.of(IterableX.super.sortedBy(selector));
    }

    @Override
    default Sequence<T> sortedBy(Comparator<T> comparator) {
        return Sequence.of(IterableX.super.sortedBy(comparator));
    }

    @Override
    default Sequence<T> sortedDescending() {
        return Sequence.of(IterableX.super.sortedDescending());
    }

    @Override
    default <R extends Comparable<R>> Sequence<T> sortedByDescending(@NotNull Function<? super T, ? extends R> selector) {
        return Sequence.of(IterableX.super.sortedByDescending(selector));
    }

    @Override
    default Sequence<T> shuffled() {
        return sortedBy(s -> IterableXHelper.nextRandomDouble());
    }

    default <K, V> EntrySequence<K, V> asEntrySequence(Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return EntrySequence.of(map(value -> new AbstractMap.SimpleEntry<>(keyMapper.apply(value), valueMapper.apply(value))));
    }

    default <K, V> EntrySequence<K, V> asEntrySequence(Function<T, Pair<K, V>> toPairMapper) {
        return EntrySequence.ofPairs(map(toPairMapper));
    }

    @Override
    default <K> EntrySequence<K, T> associateBy(@NotNull Function<? super T, ? extends K> keyMapper) {
        return EntrySequence.ofPairs(() -> SequenceHelper.associateByIterator(iterator(), keyMapper));
    }

    default <V> EntrySequence<T, V> associateWith(@NotNull Function<? super T, ? extends V> valueMapper) {
        return EntrySequence.ofPairs(() -> SequenceHelper.associateWithIterator(iterator(), valueMapper));
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
        final R1 r1 = resultMapper1.apply(this);
        final R2 r2 = resultMapper2.apply(this);
        final R3 r3 = resultMapper3.apply(this);
        final R4 r4 = resultMapper4.apply(this);
        return merger.apply(r1, r2, r3, r4);
    }
}
