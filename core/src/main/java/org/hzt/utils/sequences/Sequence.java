package org.hzt.utils.sequences;

import org.hzt.utils.PreConditions;
import org.hzt.utils.function.QuadFunction;
import org.hzt.utils.function.TriFunction;
import org.hzt.utils.iterables.IterableX;
import org.hzt.utils.iterators.ArrayIterator;
import org.hzt.utils.iterators.FlatteningIterator;
import org.hzt.utils.iterators.GeneratorIterator;
import org.hzt.utils.iterators.MultiMappingIterator;
import org.hzt.utils.iterators.SkipWhileIterator;
import org.hzt.utils.iterators.TakeWhileIterator;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.hzt.utils.strings.StringX;
import org.hzt.utils.tuples.IndexedValue;
import org.hzt.utils.tuples.Pair;
import org.hzt.utils.tuples.Triple;
import org.hzt.utils.It;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
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
public interface Sequence<T> extends IterableX<T>, WindowedSequence<T> {

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

    static <T> Sequence<T> ofNullable(@Nullable T value) {
        return value != null ? Sequence.of(value) : new EmptySequence<>();
    }

    static <T> Sequence<T> generate(@Nullable T seedValue, @NotNull UnaryOperator<T> nextFunction) {
        return seedValue == null ? new EmptySequence<>() : (() -> GeneratorIterator.of(() -> seedValue, nextFunction));
    }

    static <T> Sequence<T> generate(@NotNull Supplier<T> nextFunction) {
        return () -> GeneratorIterator.of(nextFunction, t -> nextFunction.get());
    }

    static <T> Sequence<T> generate(@NotNull Supplier<T> seedFunction, @NotNull UnaryOperator<T> nextFunction) {
        return () -> GeneratorIterator.of(seedFunction, nextFunction);
    }

    default Sequence<T> plus(@NotNull T value) {
        return Sequence.of(this, Sequence.of(value)).flatMap(It::self);
    }

    default Sequence<T> plus(@NotNull Iterable<T> values) {
        return Sequence.of(this, Sequence.of(values)).flatMap(It::self);
    }

    default <R> Sequence<R> map(@NotNull Function<? super T, ? extends R> mapper) {
        return SequenceHelper.transformingSequence(this, mapper);
    }

    @Override
    default <R> Sequence<StringX> mapToStringX(@NotNull Function<? super T, ? extends R> function) {
        return mapNotNull(t -> StringX.of((t != null ? function.apply(t) : "").toString()));
    }

    default <R> Sequence<R> mapNotNull(@NotNull Function<? super T, ? extends R> mapper) {
        return SequenceHelper.filteringSequence(
                SequenceHelper.transformingSequence(
                        SequenceHelper.filteringSequence(this,
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
        return SequenceHelper.filteringSequence(this, predicate);
    }

    default Sequence<T> filterNot(@NotNull Predicate<T> predicate) {
        return SequenceHelper.filteringSequence(this, predicate, false);
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

    default Sequence<T> step(int step) {
        return filterIndexed((index, v) -> index % step == 0);
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
    default @NotNull Sequence<T> distinct() {
        return distinctBy(It::self);
    }

    @Override
    default <R> @NotNull Sequence<T> distinctBy(@NotNull Function<T, ? extends R> selector) {
        return new DistinctSequence<>(this, selector);
    }

    default <R> Sequence<R> zipWithNext(@NotNull BiFunction<? super T, ? super T, ? extends R> function) {
        return windowed(2, list -> function.apply(list.first(), list.last()));
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
    default Sequence<T> sorted(Comparator<T> comparator) {
        return Sequence.of(IterableX.super.sorted(comparator));
    }

    @Override
    default <R extends Comparable<? super R>> Sequence<T> sortedBy(@NotNull Function<? super T, ? extends R> selector) {
        return Sequence.of(IterableX.super.sortedBy(selector));
    }

    @Override
    default Sequence<T> sortedDescending() {
        return Sequence.of(IterableX.super.sortedDescending());
    }

    @Override
    default Sequence<T> shuffled() {
        return toListX().shuffled().asSequence();
    }

    @Override
    default <R extends Comparable<? super R>> Sequence<T> sortedByDescending(@NotNull Function<? super T, ? extends R> selector) {
        return Sequence.of(IterableX.super.sortedByDescending(selector));
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
    default IntSequence mapToInt(@NotNull ToIntFunction<? super T> toIntMapper) {
        return () -> PrimitiveIterators.intIteratorOf(iterator(), toIntMapper);
    }

    @Override
    default LongSequence mapToLong(@NotNull ToLongFunction<? super T> toLongMapper) {
        return () -> PrimitiveIterators.longIteratorOf(iterator(), toLongMapper);
    }

    @Override
    default DoubleSequence mapToDouble(@NotNull ToDoubleFunction<? super T> toDoubleMapper) {
        return () -> PrimitiveIterators.doubleIteratorOf(iterator(), toDoubleMapper);
    }

    default <R> R transform(@NotNull Function<? super Sequence<T>, ? extends R> resultMapper) {
        return resultMapper.apply(this);
    }

    default Sequence<T> onSequence(Consumer<Sequence<T>> sequenceConsumer) {
        sequenceConsumer.accept(this);
        return this;
    }

    default <R1, R2, R> R toTwo(@NotNull Function<? super Sequence<T>, ? extends R1> resultMapper1,
                                @NotNull Function<? super Sequence<T>, ? extends R2> resultMapper2,
                                @NotNull BiFunction<? super R1, ? super R2, ? extends R> merger) {
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
