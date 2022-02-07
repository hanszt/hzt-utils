package hzt.sequences;

import hzt.PreConditions;
import hzt.collections.IndexedValue;
import hzt.collections.ListX;
import hzt.collections.MutableListX;
import hzt.collections.MutableSetX;
import hzt.collections.SetX;
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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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

    @Override
    default <R> Sequence<R> map(@NotNull Function<? super T, ? extends R> mapper) {
        return new TransformingSequence<>(this, mapper);
    }

    @Override
    default <R> Sequence<StringX> mapToStringX(@NotNull Function<? super T, ? extends R> function) {
        return mapNotNull(t -> StringX.of((t != null ? function.apply(t) : "").toString()));
    }

    @Override
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

    @Override
    default Sequence<T> filter(@NotNull Predicate<T> predicate) {
        return new FilteringSequence<>(this, predicate);
    }

    @Override
    default Sequence<T> filterIndexed(@NotNull BiPredicate<Integer, T> predicate) {
        return new TransformingSequence<>(
                new FilteringSequence<>(withIndex(),
                        val -> predicate.test(val.index(), val.value())), IndexedValue::value);
    }

    @Override
    default Sequence<T> filterNot(@NotNull Predicate<T> predicate) {
        return new FilteringSequence<>(this, predicate, false);
    }

    @Override
    default <R> Sequence<R> flatMap(@NotNull Function<T, Iterable<R>> transform) {
        return new FlatteningSequence<>(this, t -> transform.apply(t).iterator());
    }

    default <R> Sequence<R> flatMapStream(@NotNull Function<T, Stream<R>> transform) {
        return new FlatteningSequence<>(this, t -> transform.apply(t).iterator());
    }

    @Override
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
    @Override
    default Sequence<T> distinct() {
        return distinctBy(It::self);
    }

    @NotNull
    @Override
    default <R> Sequence<T> distinctBy(@NotNull Function<T, ? extends R> selector) {
        return new DistinctSequence<>(this, selector);
    }

    default Sequence<ListX<T>> windowed(int size) {
        return windowed(size, 1);
    }

    default <R> Sequence<R> windowed(int size, Function<ListX<T>, R> transform) {
        return windowed(size, 1).map(transform);
    }

    default Sequence<ListX<T>> windowed(int size, int step) {
        return windowed(size, step, false);
    }

    default <R> Sequence<R> windowed(int size, int step, Function<ListX<T>, R> transform) {
        return windowed(size, step, false).map(transform);
    }

    default Sequence<ListX<T>> windowed(int size, boolean partialWindows) {
        return windowed(size, 1, partialWindows);
    }

    default Sequence<ListX<T>> windowed(int size, int step, boolean partialWindows) {
        return new WindowedSequence<>(this, size, step, partialWindows);
    }

    default <R> Sequence<R> windowed(int size, int step, boolean partialWindows, Function<ListX<T>, R> transform) {
        return new WindowedSequence<>(this, size, step, partialWindows).map(transform);
    }

    @Override
    default <R> Sequence<R> zipWithNext(BiFunction<T, T, R> function) {
        return windowed(2, listX -> function.apply(listX.first(), listX.get(1)));
    }

    @Override
    default <A, R> Sequence<R> zipWith(@NotNull Iterable<A> iterable, @NotNull BiFunction<? super T, ? super A, ? extends R> function) {
        return Sequence.of(zipToMutableListWith(iterable, function));
    }

    @Override
    default Sequence<T> take(long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return new EmptySequence<>();
        } else if (this instanceof SkipTakeSequence<T> skipTakeSequence) {
            return skipTakeSequence.take(n);
        } else {
            return new TakeSequence<>(this, n);
        }
    }

    @Override
    default Sequence<T> takeWhile(@NotNull Predicate<T> predicate) {
        return new TakeWhileSequence<>(this, predicate, false);
    }

    @Override
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

    @Override
    default Sequence<T> skip(long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return this;
        } else if (this instanceof SkipTakeSequence<T> skipTakeSequence) {
            return skipTakeSequence.skip(n);
        } else {
            return new SkipSequence<>(this, n);
        }
    }

    @Override
    default <R extends Comparable<R>> Sequence<T> sortedBy(@NotNull Function<? super T, ? extends R> selector) {
        return Sequence.of(toMutableListSortedBy(selector));
    }

    @Override
    default Sequence<T> sorted() {
        return Sequence.of(IterableX.super.sorted());
    }

    default <C extends Collection<T>> C toCollection(Supplier<C> collectionFactory) {
        return filterToCollection(collectionFactory, It::noFilter);
    }

    default MutableListX<T> toMutableList() {
        return toMutableListOf(It::self);
    }

    default ListX<T> toListX() {
        return toMutableList();
    }

    default List<T> toList() {
        return List.copyOf(toMutableList());
    }

    default MutableSetX<T> toMutableSet() {
        return toCollectionNotNullOf(MutableSetX::empty, It::self);
    }

    default SetX<T> toSetX() {
        return toMutableSet();
    }

    default Set<T> toSet() {
        return Set.copyOf(toMutableSet());
    }

    default long count() {
        return count(It::noFilter);
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
