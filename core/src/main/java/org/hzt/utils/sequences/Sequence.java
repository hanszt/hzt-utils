package org.hzt.utils.sequences;

import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.function.IndexedFunction;
import org.hzt.utils.function.IndexedPredicate;
import org.hzt.utils.function.QuadFunction;
import org.hzt.utils.function.TriFunction;
import org.hzt.utils.iterables.EntryIterable;
import org.hzt.utils.iterables.IterableX;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.Iterators;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.hzt.utils.tuples.IndexedValue;
import org.hzt.utils.tuples.Pair;
import org.hzt.utils.tuples.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.ORDERED;

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
@SuppressWarnings("squid:S1448")
public interface Sequence<T> extends IterableX<T>, WindowedSequence<T> {

    static <T> Sequence<T> empty() {
        return new EmptySequence<>();
    }

    @SafeVarargs
    static <T> Sequence<T> of(T... values) {
        return () -> Iterators.arrayIterator(values);
    }

    static <T> Sequence<T> of(@NotNull Iterable<T> iterable) {
        return iterable::iterator;
    }

    @SafeVarargs
    static <T> Sequence<T> ofReverse(T... values) {
        return () -> Iterators.reverseArrayIterator(values);
    }

    static <T> Sequence<T> ofReverse(List<T> list) {
        return () -> Iterators.reverseIterator(list);
    }

    static <T> Sequence<T> ofReverse(ListX<T> list) {
        return () -> Iterators.reverseIterator(list);
    }

    static <K, V> EntrySequence<K, V> of(EntryIterable<K, V> entryIterable) {
        return entryIterable::iterator;
    }

    static <K, V> EntrySequence<K, V> ofMap(Map<K, V> map) {
        return map.entrySet()::iterator;
    }

    static <T> Sequence<T> ofNullable(@Nullable T value) {
        return value != null ? Sequence.of(value) : new EmptySequence<>();
    }

    static <T> Sequence<T> iterate(@Nullable T seedValue, @NotNull UnaryOperator<T> nextFunction) {
        return seedValue == null ? new EmptySequence<>() : (() -> Iterators.generatorIterator(() -> seedValue, nextFunction));
    }

    static <T> Sequence<T> generate(@NotNull Supplier<? extends T> nextFunction) {
        return generate(nextFunction, t -> nextFunction.get());
    }

    static <T> Sequence<T> generate(@NotNull Supplier<? extends T> seedFunction, @NotNull UnaryOperator<T> nextFunction) {
        return () -> Iterators.generatorIterator(seedFunction, nextFunction);
    }

    default Sequence<T> plus(@NotNull T value) {
        return Sequence.of(this, Sequence.of(value)).flatMap(It::self);
    }

    default Sequence<T> plus(@NotNull Iterable<? extends T> values) {
        return Sequence.of(this, Sequence.of(values)).flatMap(It::self);
    }

    default Sequence<T> minus(@NotNull T value) {
        return () -> Iterators.removingIterator(this, value);
    }

    default Sequence<T> minus(@NotNull Iterable<T> values) {
        final var others = values instanceof Set<?> ? (Set<T>) values : Sequence.of(values).toMutableSet();
        return () -> others.isEmpty() ? iterator() : filterNot(others::contains).iterator();
    }

    default Sequence<T> intersperse(T value) {
        return intersperse(t -> value);
    }

    default Sequence<T> intersperse(UnaryOperator<T> operator) {
        return () -> Iterators.interspersingIterator(iterator(), operator);
    }

    default Sequence<T> intersperse(Supplier<T> operator) {
        return intersperse(operator, t -> operator.get());
    }

    default Sequence<T> intersperse(T initValue, UnaryOperator<T> operator) {
        return intersperse(() -> initValue, operator);
    }

    default Sequence<T> intersperse(Supplier<T> initSupplier, UnaryOperator<T> operator) {
        return () -> Iterators.interspersingIterator(iterator(), initSupplier, operator);
    }

    default <R> Sequence<R> map(@NotNull Function<? super T, ? extends R> mapper) {
        return () -> Iterators.transformingIterator(iterator(), mapper);
    }

    default <R> Sequence<R> mapNotNull(@NotNull Function<? super T, ? extends R> mapper) {
        return () -> Iterators.filteringIterator(
                Iterators.transformingIterator(
                        Iterators.filteringIterator(iterator(),
                                Objects::nonNull, true),
                        mapper),
                Objects::nonNull, true);
    }

    @Override
    default <R> Sequence<R> mapIndexed(@NotNull IndexedFunction<? super T, ? extends R> mapper) {
        return () -> Iterators.transformingIndexedIterator(iterator(), mapper);
    }

    default <R> Sequence<R> flatMap(@NotNull Function<? super T, ? extends Iterable<? extends R>> transform) {
        return () -> Iterators.flatMappingIterator(iterator(), t -> transform.apply(t).iterator());
    }

    @Override
    default IntSequence flatMapToInt(@NotNull Function<? super T, ? extends PrimitiveIterable.OfInt> mapper) {
        return () -> PrimitiveIterators.toIntFlatMappingIterator(iterator(), t -> mapper.apply(t).iterator());
    }

    @Override
    default LongSequence flatMapToLong(@NotNull Function<? super T, ? extends PrimitiveIterable.OfLong> mapper) {
        return () -> PrimitiveIterators.toLongFlatMappingIterator(iterator(), t -> mapper.apply(t).iterator());
    }

    @Override
    default DoubleSequence flatMapToDouble(@NotNull Function<? super T, ? extends PrimitiveIterable.OfDouble> mapper) {
        return () -> PrimitiveIterators.toDoubleFlatMappingIterator(iterator(), t -> mapper.apply(t).iterator());
    }

    default <R> Sequence<R> mapMulti(@NotNull BiConsumer<? super T, ? super Consumer<R>> mapper) {
        return () -> Iterators.multiMappingIterator(iterator(), mapper);
    }

    @Override
    default IntSequence mapMultiToInt(@NotNull BiConsumer<? super T, IntConsumer> mapper) {
        return (IntSequence) IterableX.super.mapMultiToInt(mapper);
    }

    @Override
    default LongSequence mapMultiToLong(@NotNull BiConsumer<? super T, LongConsumer> mapper) {
        return (LongSequence) IterableX.super.mapMultiToLong(mapper);
    }

    @Override
    default DoubleSequence mapMultiToDouble(@NotNull BiConsumer<? super T, DoubleConsumer> mapper) {
        return (DoubleSequence) IterableX.super.mapMultiToDouble(mapper);
    }

    @Override
    default <R> Sequence<R> castIfInstanceOf(@NotNull Class<R> aClass) {
        return filter(aClass::isInstance).map(aClass::cast);
    }

    default Sequence<T> filter(@NotNull Predicate<? super T> predicate) {
        return () ->Iterators.filteringIterator(iterator(), predicate, true);
    }

    default Sequence<T> filterNot(@NotNull Predicate<? super T> predicate) {
        return () ->Iterators.filteringIterator(iterator(), predicate, false);
    }

    default <R> Sequence<T> filterBy(@NotNull Function<? super T, ? extends R> selector,
                                     @NotNull Predicate<? super R> predicate) {
        return filter(Objects::nonNull).filter(t -> predicate.test(selector.apply(t)));
    }

    @Override
    default Sequence<T> filterIndexed(@NotNull IndexedPredicate<? super T> predicate) {
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
    default <R> @NotNull Sequence<T> distinctBy(@NotNull Function<? super T, ? extends R> selector) {
        return () -> Iterators.distinctIterator(iterator(), selector);
    }

    default Sequence<T> constrainOnce() {
        final var consumed = new AtomicBoolean();
        return () -> Iterators.constrainOnceIterator(iterator(), consumed);
    }

    default <R> Sequence<R> zipWithNext(@NotNull BiFunction<? super T, ? super T, ? extends R> function) {
        return windowed(2, list -> function.apply(list.first(), list.last()));
    }

    default <A, R> Sequence<R> zip(@NotNull Iterable<A> other, @NotNull BiFunction<? super T, ? super A, ? extends R> function) {
        return () -> Iterators.mergingIterator(iterator(), other.iterator(), function);
    }

    @Override
    default Sequence<T> take(long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return new EmptySequence<>();
        } else if (this instanceof SkipTakeSequence) {
            var skipTakeSequence = (SkipTakeSequence<T>) this;
            return skipTakeSequence.take(n);
        } else {
            return new TakeSequence<>(this, n);
        }
    }

    default Sequence<T> takeWhile(@NotNull Predicate<? super T> predicate) {
        return () -> Iterators.takeWhileIterator(iterator(), predicate, false);
    }

    default Sequence<T> takeWhileInclusive(@NotNull Predicate<? super T> predicate) {
        return () -> Iterators.takeWhileIterator(iterator(), predicate, true);
    }

    default Sequence<T> skip(long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return this;
        } else if (this instanceof SkipTakeSequence) {
            var skipTakeSequence = (SkipTakeSequence<T>) this;
            return skipTakeSequence.skip(n);
        } else {
            return new SkipSequence<>(this, n);
        }
    }

    @Override
    default Sequence<T> skipWhile(@NotNull Predicate<? super T> predicate) {
        return () -> Iterators.skipWhileIterator(iterator(), predicate, false);
    }

    @Override
    default Sequence<T> skipWhileInclusive(@NotNull Predicate<? super T> predicate) {
        return () -> Iterators.skipWhileIterator(iterator(), predicate, true);
    }

    @Override
    default Sequence<T> sorted() {
        return () -> IterableX.super.sorted().iterator();
    }

    @Override
    default Sequence<T> sorted(Comparator<? super T> comparator) {
        return () -> IterableX.super.sorted(comparator).iterator();
    }

    @Override
    default <R extends Comparable<? super R>> Sequence<T> sortedBy(@NotNull Function<? super T, ? extends R> selector) {
        return () -> IterableX.super.sortedBy(selector).iterator();
    }

    @Override
    default Sequence<T> sortedDescending() {
        return () -> IterableX.super.sortedDescending().iterator();
    }

    default Sequence<T> shuffled() {
        return () -> toListX().shuffled().iterator();
    }

    @Override
    default <R extends Comparable<? super R>> Sequence<T> sortedByDescending(@NotNull Function<? super T, ? extends R> selector) {
        return () -> IterableX.super.sortedByDescending(selector).iterator();
    }

    default <K, V> EntrySequence<K, V> asEntrySequence(@NotNull Function<? super T, ? extends K> keyMapper,
                                                       @NotNull Function<? super T, ? extends V> valueMapper) {
        return EntrySequence.of(map(value -> Map.entry(keyMapper.apply(value), valueMapper.apply(value))));
    }

    default <K, V> EntrySequence<K, V> asEntrySequence(Function<? super T, Pair<K, V>> toPairMapper) {
        return EntrySequence.ofPairs(map(toPairMapper));
    }

    @Override
    default <K> EntrySequence<K, T> associateBy(@NotNull Function<? super T, ? extends K> keyMapper) {
        return EntrySequence.ofPairs(map(e -> Pair.of(keyMapper.apply(e), e)));
    }

    default <V> EntrySequence<T, V> associateWith(@NotNull Function<? super T, ? extends V> valueMapper) {
        return EntrySequence.ofPairs(map(e -> Pair.of(e, valueMapper.apply(e))));
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

    @Override
    default Stream<T> stream() {
        return StreamSupport.stream(() -> Spliterators.spliteratorUnknownSize(iterator(), ORDERED), ORDERED, false);
    }

    default Sequence<T> onSequence(Consumer<? super Sequence<T>> sequenceConsumer) {
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
        final var r1 = resultMapper1.apply(this);
        final var r2 = resultMapper2.apply(this);
        final var r3 = resultMapper3.apply(this);
        final var r4 = resultMapper4.apply(this);
        return merger.apply(r1, r2, r3, r4);
    }
}
