package org.hzt.utils.sequences;

import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.function.IndexedBiFunction;
import org.hzt.utils.function.IndexedFunction;
import org.hzt.utils.function.IndexedPredicate;
import org.hzt.utils.function.QuadFunction;
import org.hzt.utils.function.TriFunction;
import org.hzt.utils.gatherers.Gatherer;
import org.hzt.utils.iterables.EntryIterable;
import org.hzt.utils.iterables.IterableExtension;
import org.hzt.utils.iterables.IterableX;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.Iterators;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.hzt.utils.streams.StreamX;
import org.hzt.utils.tuples.IndexedValue;
import org.hzt.utils.tuples.Pair;
import org.hzt.utils.tuples.Triple;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
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

    static <T> Sequence.Builder<T> builder() {
        return new SequenceBuilder<>();
    }

    @SafeVarargs
    static <T> Sequence<T> of(final T... values) {
        return () -> Iterators.arrayIterator(values);
    }

    static <T> Sequence<T> of(final Iterable<T> iterable) {
        return iterable::iterator;
    }

    @SafeVarargs
    static <T> Sequence<T> reverseOf(final T... values) {
        return () -> Iterators.reverseArrayIterator(values);
    }

    static <T> Sequence<T> reverseOf(final List<T> list) {
        return () -> Iterators.reverseIterator(list);
    }

    static <T> Sequence<T> reverseOf(final ListX<T> list) {
        return () -> Iterators.reverseIterator(list);
    }

    static <K, V> EntrySequence<K, V> of(final EntryIterable<K, V> entryIterable) {
        return entryIterable::iterator;
    }

    static <K, V> EntrySequence<K, V> ofMap(final Map<K, V> map) {
        return map.entrySet()::iterator;
    }

    static <T> Sequence<T> ofNullable(final T value) {
        return value != null ? Sequence.of(value) : new EmptySequence<>();
    }

    static <T> Sequence<T> iterate(final T seedValue, final UnaryOperator<T> nextFunction) {
        return seedValue == null ? new EmptySequence<>() : (() -> Iterators.generatorIterator(() -> seedValue, nextFunction));
    }

    static <T> Sequence<T> generate(final Supplier<? extends T> nextFunction) {
        return generate(nextFunction, t -> nextFunction.get());
    }

    static <T> Sequence<T> generate(final Supplier<? extends T> seedFunction, final UnaryOperator<T> nextFunction) {
        return () -> Iterators.generatorIterator(seedFunction, nextFunction);
    }

    default Sequence<T> plus(final T value) {
        return Sequence.of(this, Sequence.of(value)).mapMulti(Iterable::forEach);
    }

    default Sequence<T> plus(final Iterable<? extends T> values) {
        return Sequence.of(this, Sequence.of(values)).mapMulti(Iterable::forEach);
    }

    default Sequence<T> minus(final T value) {
        return () -> Iterators.removingIterator(this, value);
    }

    default Sequence<T> minus(final Iterable<T> values) {
        final var others = values instanceof Set<?> ? (Set<T>) values : Sequence.of(values).toMutableSet();
        return () -> others.isEmpty() ? iterator() : filterNot(others::contains).iterator();
    }

    default Sequence<T> intersperse(final T value) {
        return intersperse(t -> value);
    }

    default Sequence<T> intersperse(final UnaryOperator<T> operator) {
        return () -> Iterators.interspersingIterator(iterator(), operator);
    }

    default Sequence<T> intersperse(final Supplier<T> operator) {
        return intersperse(operator, t -> operator.get());
    }

    default Sequence<T> intersperse(final T initValue, final UnaryOperator<T> operator) {
        return intersperse(() -> initValue, operator);
    }

    default Sequence<T> intersperse(final Supplier<T> initSupplier, final UnaryOperator<T> operator) {
        return () -> Iterators.interspersingIterator(iterator(), initSupplier, operator);
    }

    default <R> Sequence<R> map(final Function<? super T, ? extends R> mapper) {
        return () -> Iterators.transformingIterator(iterator(), mapper);
    }

    default <R> Sequence<R> mapNotNull(final Function<? super T, ? extends R> mapper) {
        return () -> Iterators.filteringIterator(
                Iterators.transformingIterator(
                        Iterators.filteringIterator(iterator(),
                                Objects::nonNull, true),
                        mapper),
                Objects::nonNull, true);
    }

    @Override
    default <A, R> Sequence<R> gather(final Gatherer<? super T, A, R> gatherer) {
        return () -> Iterators.gatheringIterator(iterator(), gatherer);
    }

    default <R> Sequence<R> then(final IterableExtension<T, R> extension) {
        return () -> extension.extend(this).iterator();
    }

    default <R> Sequence<R> mapIfPresent(final Function<? super T, Optional<R>> mapper) {
        return () -> Iterators.multiMappingIterator(iterator(), (t, consumer) -> mapper.apply(t).ifPresent(consumer));
    }

    @Override
    default <R> Sequence<R> mapIndexed(final IndexedFunction<? super T, ? extends R> mapper) {
        return () -> Iterators.transformingIndexedIterator(iterator(), mapper);
    }

    default <R> Sequence<R> flatMap(final Function<? super T, ? extends Iterable<? extends R>> transform) {
        return () -> Iterators.flatMappingIterator(iterator(), t -> transform.apply(t).iterator());
    }

    @Override
    default IntSequence flatMapToInt(final Function<? super T, ? extends PrimitiveIterable.OfInt> mapper) {
        return () -> PrimitiveIterators.toIntFlatMappingIterator(iterator(), t -> mapper.apply(t).iterator());
    }

    @Override
    default LongSequence flatMapToLong(final Function<? super T, ? extends PrimitiveIterable.OfLong> mapper) {
        return () -> PrimitiveIterators.toLongFlatMappingIterator(iterator(), t -> mapper.apply(t).iterator());
    }

    @Override
    default DoubleSequence flatMapToDouble(final Function<? super T, ? extends PrimitiveIterable.OfDouble> mapper) {
        return () -> PrimitiveIterators.toDoubleFlatMappingIterator(iterator(), t -> mapper.apply(t).iterator());
    }

    default <R> Sequence<R> mapMulti(final BiConsumer<? super T, ? super Consumer<R>> mapper) {
        return () -> Iterators.multiMappingIterator(iterator(), mapper);
    }

    @Override
    default IntSequence mapMultiToInt(final BiConsumer<? super T, IntConsumer> mapper) {
        return (IntSequence) IterableX.super.mapMultiToInt(mapper);
    }

    @Override
    default LongSequence mapMultiToLong(final BiConsumer<? super T, LongConsumer> mapper) {
        return (LongSequence) IterableX.super.mapMultiToLong(mapper);
    }

    @Override
    default DoubleSequence mapMultiToDouble(final BiConsumer<? super T, DoubleConsumer> mapper) {
        return (DoubleSequence) IterableX.super.mapMultiToDouble(mapper);
    }

    @Override
    default <R> Sequence<R> castIfInstanceOf(final Class<R> aClass) {
        return filter(aClass::isInstance).map(aClass::cast);
    }

    default Sequence<T> filter(final Predicate<? super T> predicate) {
        return () -> Iterators.filteringIterator(iterator(), predicate, true);
    }

    default Sequence<T> filterNot(final Predicate<? super T> predicate) {
        return () -> Iterators.filteringIterator(iterator(), predicate, false);
    }

    default <R> Sequence<T> filterBy(final Function<? super T, ? extends R> selector,
                                     final Predicate<? super R> predicate) {
        return filter(Objects::nonNull).filter(t -> predicate.test(selector.apply(t)));
    }

    @Override
    default Sequence<T> filterIndexed(final IndexedPredicate<? super T> predicate) {
        return withIndex()
                .filter(indexedValue -> predicate.test(indexedValue.index(), indexedValue.value()))
                .map(IndexedValue::value);
    }

    default Sequence<T> step(final int step) {
        return filterIndexed((index, v) -> index % step == 0);
    }

    default Sequence<IndexedValue<T>> withIndex() {
        return this::indexedIterator;
    }

    @Override
    default Sequence<T> onEach(final Consumer<? super T> consumer) {
        return onEach(It::self, consumer);
    }

    @Override
    default <R> Sequence<T> onEach(final Function<? super T, ? extends R> selector, final Consumer<? super R> consumer) {
        return map(item -> {
            consumer.accept(selector.apply(item));
            return item;
        });
    }

    @Override
    default <R> Sequence<R> scan(final R initial, final BiFunction<? super R, ? super T, ? extends R> operation) {
        return () -> Iterators.scanningIterator(iterator(), initial, (i, acc, t) -> operation.apply(acc, t));
    }

    @Override
    default <R> Sequence<R> scanIndexed(final R initial, final IndexedBiFunction<? super R, ? super T, ? extends R> operation) {
        return () -> Iterators.scanningIterator(iterator(), initial, operation);
    }

    @Override
    default Sequence<T> distinct() {
        return distinctBy(It::self);
    }

    @Override
    default <R> Sequence<T> distinctBy(final Function<? super T, ? extends R> selector) {
        return () -> Iterators.distinctIterator(iterator(), selector);
    }

    default Sequence<T> constrainOnce() {
        final var consumed = new AtomicBoolean();
        return () -> Iterators.constrainOnceIterator(iterator(), consumed);
    }

    default <R> Sequence<R> zipWithNext(final BiFunction<? super T, ? super T, ? extends R> function) {
        return windowed(2, list -> function.apply(list.first(), list.last()));
    }

    default <A, R> Sequence<R> zip(final Iterable<A> other, final BiFunction<? super T, ? super A, ? extends R> function) {
        return () -> Iterators.mergingIterator(iterator(), other.iterator(), function);
    }

    @Override
    default Sequence<T> take(final long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return new EmptySequence<>();
        } else if (this instanceof final SkipTakeSequence<T> skipTakeSequence) {
            return skipTakeSequence.take(n);
        } else {
            return new TakeSequence<>(this, n);
        }
    }

    default Sequence<T> takeWhile(final Predicate<? super T> predicate) {
        return () -> Iterators.takeWhileIterator(iterator(), predicate, false);
    }

    default Sequence<T> takeWhileInclusive(final Predicate<? super T> predicate) {
        return () -> Iterators.takeWhileIterator(iterator(), predicate, true);
    }

    default Sequence<T> skip(final long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return this;
        } else if (this instanceof final SkipTakeSequence<T> skipTakeSequence) {
            return skipTakeSequence.skip(n);
        } else {
            return new SkipSequence<>(this, n);
        }
    }

    @Override
    default Sequence<T> skipWhile(final Predicate<? super T> predicate) {
        return () -> Iterators.skipWhileIterator(iterator(), predicate, false);
    }

    @Override
    default Sequence<T> skipWhileInclusive(final Predicate<? super T> predicate) {
        return () -> Iterators.skipWhileIterator(iterator(), predicate, true);
    }

    @Override
    default Sequence<T> sorted() {
        return () -> IterableX.super.sorted().iterator();
    }

    @Override
    default Sequence<T> sorted(final Comparator<? super T> comparator) {
        return () -> IterableX.super.sorted(comparator).iterator();
    }

    @Override
    default <R extends Comparable<? super R>> Sequence<T> sortedBy(final Function<? super T, ? extends R> selector) {
        return () -> IterableX.super.sortedBy(selector).iterator();
    }

    @Override
    default Sequence<T> sortedDescending() {
        return () -> IterableX.super.sortedDescending().iterator();
    }

    default Sequence<T> shuffled(final Random random) {
        return () -> toListX().shuffled(random).iterator();
    }

    @Override
    default <R extends Comparable<? super R>> Sequence<T> sortedByDescending(final Function<? super T, ? extends R> selector) {
        return () -> IterableX.super.sortedByDescending(selector).iterator();
    }

    default <K, V> EntrySequence<K, V> asEntrySequence(final Function<? super T, ? extends K> keyMapper,
                                                       final Function<? super T, ? extends V> valueMapper) {
        return EntrySequence.of(map(value -> Map.entry(keyMapper.apply(value), valueMapper.apply(value))));
    }

    default <K, V> EntrySequence<K, V> asEntrySequence(final Function<? super T, Pair<K, V>> toPairMapper) {
        return EntrySequence.ofPairs(map(toPairMapper));
    }

    @Override
    default <K> EntrySequence<K, T> associateBy(final Function<? super T, ? extends K> keyMapper) {
        return EntrySequence.ofPairs(map(e -> Pair.of(keyMapper.apply(e), e)));
    }

    default <V> EntrySequence<T, V> associateWith(final Function<? super T, ? extends V> valueMapper) {
        return EntrySequence.ofPairs(map(e -> Pair.of(e, valueMapper.apply(e))));
    }

    @Override
    default IntSequence mapToInt(final ToIntFunction<? super T> toIntMapper) {
        return () -> PrimitiveIterators.intIteratorOf(iterator(), toIntMapper);
    }

    @Override
    default LongSequence mapToLong(final ToLongFunction<? super T> toLongMapper) {
        return () -> PrimitiveIterators.longIteratorOf(iterator(), toLongMapper);
    }

    @Override
    default DoubleSequence mapToDouble(final ToDoubleFunction<? super T> toDoubleMapper) {
        return () -> PrimitiveIterators.doubleIteratorOf(iterator(), toDoubleMapper);
    }

    default <R> R transform(final Function<? super Sequence<T>, ? extends R> resultMapper) {
        return resultMapper.apply(this);
    }

    @Override
    default StreamX<T> stream() {
        return StreamX.of(StreamSupport.stream(() -> Spliterators.spliteratorUnknownSize(iterator(), ORDERED), ORDERED, false));
    }

    default Sequence<T> onSequence(final Consumer<? super Sequence<T>> sequenceConsumer) {
        sequenceConsumer.accept(this);
        return this;
    }

    default <R1, R2, R> R toTwo(final Function<? super Sequence<T>, ? extends R1> resultMapper1,
                                final Function<? super Sequence<T>, ? extends R2> resultMapper2,
                                final BiFunction<? super R1, ? super R2, ? extends R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this));
    }

    default <R1, R2> Pair<R1, R2> toTwo(final Function<? super Sequence<T>, ? extends R1> resultMapper1,
                                        final Function<? super Sequence<T>, ? extends R2> resultMapper2) {
        return toTwo(resultMapper1, resultMapper2, Pair::of);
    }

    default <R1, R2, R3, R> R toThree(final Function<? super Sequence<T>, ? extends R1> resultMapper1,
                                      final Function<? super Sequence<T>, ? extends R2> resultMapper2,
                                      final Function<? super Sequence<T>, ? extends R3> resultMapper3,
                                      final TriFunction<R1, R2, R3, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this), resultMapper3.apply(this));
    }

    default <R1, R2, R3> Triple<R1, R2, R3> toThree(final Function<? super Sequence<T>, ? extends R1> resultMapper1,
                                                    final Function<? super Sequence<T>, ? extends R2> resultMapper2,
                                                    final Function<? super Sequence<T>, ? extends R3> resultMapper3) {
        return toThree(resultMapper1, resultMapper2, resultMapper3, Triple::of);
    }

    default <R1, R2, R3, R4, R> R toFour(final Function<? super Sequence<T>, ? extends R1> resultMapper1,
                                         final Function<? super Sequence<T>, ? extends R2> resultMapper2,
                                         final Function<? super Sequence<T>, ? extends R3> resultMapper3,
                                         final Function<? super Sequence<T>, ? extends R4> resultMapper4,
                                         final QuadFunction<R1, R2, R3, R4, R> merger) {
        final var r1 = resultMapper1.apply(this);
        final var r2 = resultMapper2.apply(this);
        final var r3 = resultMapper3.apply(this);
        final var r4 = resultMapper4.apply(this);
        return merger.apply(r1, r2, r3, r4);
    }

    interface Builder<T> extends Consumer<T> {

        /**
         * Adds an element to the sequence being built.
         *
         * @throws IllegalStateException if the builder has already transitioned to the built state
         */
        @Override
        void accept(T t);

        /**
         * Adds an element to the sequence being built.
         *
         * @implSpec
         * The default implementation behaves as if:
         * <pre>{@code
         *     accept(t)
         *     return this;
         * }</pre>
         *
         * @param t the element to add
         * @return {@code this} builder
         * @throws IllegalStateException if the builder has already transitioned to
         * the built state
         */
        default Sequence.Builder<T> add(final T t) {
            accept(t);
            return this;
        }

        /**
         * Builds the sequence, transitioning this builder to the built state.
         * An {@code IllegalStateException} is thrown if there are further attempts
         * to operate on the builder after it has entered the built state.
         *
         * @return the built sequence
         * @throws IllegalStateException if the builder has already transitioned to the built state
         */
        Sequence<T> build();

    }
}
