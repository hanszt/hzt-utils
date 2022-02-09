package hzt.iterables;

import hzt.collections.ArrayX;
import hzt.collections.IndexedIterable;
import hzt.collections.IndexedValue;
import hzt.collections.ListX;
import hzt.collections.MapX;
import hzt.collections.MutableListX;
import hzt.collections.MutableMapX;
import hzt.collections.MutableSetX;
import hzt.collections.NavigableMapX;
import hzt.collections.NavigableSetX;
import hzt.collections.SetX;
import hzt.numbers.BigDecimalX;
import hzt.ranges.DoubleRange;
import hzt.ranges.IntRange;
import hzt.ranges.LongRange;
import hzt.sequences.Sequence;
import hzt.statistics.BigDecimalStatistics;
import hzt.statistics.DoubleStatistics;
import hzt.statistics.IntStatistics;
import hzt.statistics.LongStatistics;
import hzt.strings.StringX;
import hzt.tuples.Pair;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This is an interface to transform smaller iterables including collections or arrays to some other state.
 * <p>
 * Its use is very comparable to the streams api but with shorter syntax.
 * <p>
 * It is inspired by the functional methods provided for collections in Kotlin
 * <p>
 * The functions applied to a Transformer are eagerly evaluated as apposed to in a stream.
 * For smaller collections < 100_000 elements, the performance is similar to streams
 * <p>
 * For larger collections, or when a lot of transformation are applied, streams are preferred.
 * <p>
 * For a Transformer, I suggest not to apply more than 3 subsequent transformations.
 *
 * @param <T> The Type of the Iterable in the Transform object
 * @author Hans Zuidervaart
 */
@SuppressWarnings({"unused"})
public interface IterableX<T> extends Iterable<T>, IndexedIterable<T> {

    IterableX<T> plus(@NotNull T... values);

    IterableX<T> plus(@NotNull Iterable<T> values);

    <R> IterableX<R> map(@NotNull Function<? super T, ? extends R> mapper);

    <R> IterableX<R> mapNotNull(@NotNull Function<? super T, ? extends R> mapper);

    <R> IterableX<R> mapIndexed(@NotNull BiFunction<Integer, ? super T, ? extends R> mapper);

    <R> IterableX<R> flatMap(@NotNull Function<T, Iterable<R>> mapper);

    <R> IterableX<R> mapMulti(@NotNull BiConsumer<? super T, ? super Consumer<R>> mapper);

    <R> IterableX<StringX> mapToStringX(@NotNull Function<? super T, ? extends R> function);

    IterableX<T> filter(@NotNull Predicate<T> predicate);

    <R> IterableX<T> filterBy(@NotNull Function<? super T, ? extends R> selector, @NotNull Predicate<R> predicate);

    IterableX<T> filterNot(@NotNull Predicate<T> predicate);

    default <R> ListX<R> toListXOf(@NotNull Function<? super T, ? extends R> transform) {
        return toMutableListNotNullOf(transform);
    }

    default <R> List<R> toListOf(@NotNull Function<? super T, ? extends R> transform) {
        return List.copyOf(toMutableListNotNullOf(transform));
    }

    default <R> SetX<R> toSetXOf(@NotNull Function<? super T, ? extends R> transform) {
        return toMutableSetNotNullOf(transform);
    }

    default <R> Set<R> toSetOf(@NotNull Function<? super T, ? extends R> transform) {
        return Collections.unmodifiableSet(toMutableSetNotNullOf(transform));
    }

    private <R> MutableSetX<R> toMutableSetNotNullOf(@NotNull Function<? super T, ? extends R> transform) {
        return toCollectionNotNullOf(MutableSetX::empty, transform);
    }

    default <R> MutableListX<R> toMutableListOf(@NotNull Function<? super T, ? extends R> transform) {
        return mapTo(MutableListX::empty, transform);
    }

    private <R> MutableListX<R> toMutableListNotNullOf(@NotNull Function<? super T, ? extends R> transform) {
        return toCollectionNotNullOf(MutableListX::empty, transform);
    }

    default <R> MutableSetX<R> toMutableSetOf(@NotNull Function<? super T, ? extends R> transform) {
        return mapTo(MutableSetX::empty, transform);
    }

    default <R, C extends Collection<R>> C mapTo(@NotNull Supplier<C> collectionFactory,
                                                 @NotNull Function<? super T, ? extends R> mapper) {
        return mapFilteringToCollection(collectionFactory, Objects::nonNull, mapper, It::noFilter);
    }

    default <C extends Collection<T>> C toCollection(Supplier<C> collectionFactory) {
        return mapFilteringToCollection(collectionFactory, It::noFilter, It::self, It::noFilter);
    }

    private <R, C extends Collection<R>> C mapFilteringToCollection(
            @NotNull Supplier<C> collectionFactory,
            @NotNull Predicate<? super T> predicate,
            @NotNull Function<? super T, ? extends R> mapper,
            @NotNull Predicate<R> resultFilter) {
        C collection = collectionFactory.get();
        for (T t : this) {
            if (t != null && predicate.test(t)) {
                final R r = mapper.apply(t);
                if (resultFilter.test(r)) {
                    collection.add(r);
                }
            }
        }
        return collection;
    }

    private  <R, C extends Collection<R>> C toCollectionNotNullOf(@NotNull Supplier<C> collectionFactory,
                                                                 @NotNull Function<? super T, ? extends R> mapper) {
        return mapFilteringToCollection(collectionFactory, Objects::nonNull, mapper, Objects::nonNull);
    }

    IterableX<T> filterIndexed(@NotNull BiPredicate<Integer, T> predicate);

    <R> IterableX<R> castIfInstanceOf(@NotNull Class<R> aClass);

    private <R extends Comparable<R>> MutableListX<T> toMutableListSortedBy(@NotNull Function<? super T, ? extends R> selector) {
        final MutableListX<T> list = toMutableListOf(It::self);
        list.sort(Comparator.comparing(selector));
        return list;
    }

    default <R extends Comparable<R>> NavigableSetX<T> toSortedSet(@NotNull Function<? super T, ? extends R> selector) {
        NavigableSetX<T> navigableSetX = NavigableSetX.comparingBy(selector);
        navigableSetX.addAll(filterBy(selector, Objects::nonNull).toList());
        return navigableSetX;
    }

    default <R extends Comparable<R>> NavigableSetX<R> toSortedSetOf(@NotNull Function<? super T, ? extends R> selector) {
        return NavigableSetX.of(toMutableListNotNullOf(selector), It::self);
    }

    default <R> R[] toArrayOf(@NotNull Function<? super T, ? extends R> mapper, @NotNull IntFunction<R[]> generator) {
        return toMutableListOf(mapper).toArray(generator.apply(0));
    }

    default <R> ArrayX<R> toArrayXOf(@NotNull Function<? super T, ? extends R> mapper, @NotNull IntFunction<R[]> generator) {
        return ArrayX.of(toArrayOf(mapper, generator));
    }

    default int[] toIntArrayOf(@NotNull ToIntFunction<? super T> mapper) {
        int counter = 0;
        int[] array = new int[(int) count(Objects::nonNull)];
        for (T value : this) {
            if (value != null) {
                final int anInt = mapper.applyAsInt(value);
                array[counter] = anInt;
                counter++;
            }
        }
        return array;
    }

    default long[] toLongArray(@NotNull ToLongFunction<? super T> mapper) {
        int counter = 0;
        long[] array = new long[(int) count(Objects::nonNull)];
        for (T value : this) {
            if (value != null) {
                final long t = mapper.applyAsLong(value);
                array[counter] = t;
                counter++;
            }
        }
        return array;
    }

    default double[] toDoubleArray(@NotNull ToDoubleFunction<? super T> mapper) {
        int counter = 0;
        double[] array = new double[(int) count(Objects::nonNull)];
        for (T value : this) {
            if (value != null) {
                final double t = mapper.applyAsDouble(value);
                array[counter] = t;
                counter++;
            }
        }
        return array;
    }

    default IntStatistics statsOfInts(@NotNull ToIntFunction<? super T> mapper) {
        return collect(IntStatistics::new,
                Objects::nonNull,
                mapper::applyAsInt,
                IntStatistics::accept);
    }

    default LongStatistics statsOfLongs(@NotNull ToLongFunction<? super T> mapper) {
        return collect(LongStatistics::new,
                Objects::nonNull,
                mapper::applyAsLong,
                LongStatistics::accept);
    }

    default DoubleStatistics statsOfDoubles(@NotNull ToDoubleFunction<? super T> mapper) {
        return collect(DoubleStatistics::new,
                Objects::nonNull,
                mapper::applyAsDouble,
                DoubleStatistics::accept);
    }

    default BigDecimalStatistics statsOfBigDecimals(@NotNull Function<T, ? extends BigDecimal> mapper) {
        return collect(BigDecimalStatistics::new,
                Objects::nonNull,
                mapper,
                Objects::nonNull,
                BigDecimalStatistics::accept,
                It::self);
    }

    default <K, V> MutableMapX<K, V> toMutableMap(@NotNull Function<? super T, ? extends K> keyMapper,
                                                  @NotNull Function<? super T, ? extends V> valueMapper) {
        MutableMapX<K, V> map = MutableMapX.empty();
        for (T t : this) {
            if (t != null) {
                final K key = keyMapper.apply(t);
                if (key != null) {
                    map.put(key, valueMapper.apply(t));
                }
            }
        }
        return map;
    }

    default <K, V> MapX<K, V> toMapX(@NotNull Function<T, K> keyMapper, @NotNull Function<T, V> valueMapper) {
        return toMutableMap(keyMapper, valueMapper);
    }

    default <K, V> Map<K, V> toMap(@NotNull Function<T, K> keyMapper, @NotNull Function<T, V> valueMapper) {
        return toMutableMap(keyMapper, valueMapper);
    }

    private MutableListX<T> getListOrElseCompute() {
        final Iterable<T> iterable = this;
        return iterable instanceof List ? MutableListX.of((List<T>) iterable) : MutableListX.of(this);
    }

    private MutableSetX<T> getSetOrElseCompute() {
        final Iterable<T> iterable = this;
        return iterable instanceof Set ? MutableSetX.of((Set<T>) iterable) : MutableSetX.of(this);
    }

    private MutableListX<T> getListOrElseThrow() {
        final Iterable<T> iterable = this;
        if (iterable instanceof List) {
            return MutableListX.of((List<T>) iterable);
        }
        throw new IllegalArgumentException(iterable.getClass().getSimpleName() + " is not an instance of List");
    }

    private MutableSetX<T> getSetOrElseThrow() {
        final Iterable<T> iterable = this;
        if (iterable instanceof Set) {
            return MutableSetX.of(((Set<T>) iterable));
        }
        throw new IllegalArgumentException(iterable.getClass().getSimpleName() + " is not an instance of Set");
    }

    default <K> MapX<K, T> associateBy(@NotNull Function<? super T, ? extends K> keyMapper) {
        return toMutableMap(keyMapper, It::self);
    }

    default <K extends Comparable<K>> NavigableMapX<K, T> toSortedMapAssociatedBy(
            @NotNull Function<? super T, ? extends K> keyMapper) {
        return NavigableMapX.ofMap(toMutableMap(keyMapper, It::self), It::self);
    }

    default <V> MapX<T, V> associateWith(@NotNull Function<? super T, ? extends V> valueMapper) {
        return toMutableMap(It::self, valueMapper);
    }

    default <K extends Comparable<K>, V> NavigableMapX<K, V> toSortedMapAssociatedWith(
            @NotNull Function<? super T, ? extends V> valueMapper) {
        Function<T, K> keyMapper = IterableXHelper::asComparableOrThrow;
        final MutableMapX<K, V> entries = toMutableMap(keyMapper, valueMapper);
        return NavigableMapX.of(entries, It::self);
    }

    default <R extends Comparable<R>> ListX<T> toSortedListX(@NotNull Function<? super T, ? extends R> selector) {
        return toMutableListSortedBy(selector);
    }

    default <R extends Comparable<R>> ListX<T> toSortedListX() {
        return toSortedListX((Function<T, R>) IterableXHelper::asComparableOrThrow);
    }

    default <R extends Comparable<R>> ListX<T> shuffled() {
        return toMutableListSortedBy(s -> IterableXHelper.nextRandomDouble());
    }

    IterableX<T> sorted();

    <R extends Comparable<R>> IterableX<T> sortedBy(@NotNull Function<? super T, ? extends R> selector);

    default <R extends Comparable<R>> ListX<T> sortedDescending() {
        return sortedByDescending((Function<T, R>) IterableXHelper::asComparableOrThrow);
    }
    default <R extends Comparable<R>> ListX<T> sortedByDescending(@NotNull Function<? super T, ? extends R> selector) {
        final MutableListX<T> list = toMutableList();
        list.sort(Comparator.comparing(selector).reversed());
        return list;
    }

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Sequence<T> asSequence() {
        return Sequence.of(this);
    }

    default IntRange asIntRange(@NotNull ToIntFunction<T> keyMapper) {
        return IntRange.of(asSequence().map(keyMapper::applyAsInt));
    }

    default LongRange asLongRange(@NotNull ToLongFunction<T> keyMapper) {
        return LongRange.of(asSequence().map(keyMapper::applyAsLong));
    }

    default DoubleRange asDoubleRange(@NotNull ToDoubleFunction<T> keyMapper) {
        return DoubleRange.of(asSequence().map(keyMapper::applyAsDouble));
    }

    IterableX<IndexedValue<T>> withIndex();

    IterableX<T> distinct();

    <R> IterableX<T> distinctBy(@NotNull Function<T, ? extends R> selector);

    default long count(@NotNull Predicate<T> predicate) {
        long counter = 0;
        for (T t : this) {
            if (predicate.test(t)) {
                counter++;
            }
        }
        return counter;
    }

    default long sumOfInts(@NotNull ToIntFunction<? super T> selector) {
        long sum = 0;
        for (T t : this) {
            if (t != null) {
                final int value = selector.applyAsInt(t);
                sum += value;
            }
        }
        return sum;
    }

    default long sumOfLongs(@NotNull ToLongFunction<? super T> selector) {
        long sum = 0;
        for (T t : this) {
            if (t != null) {
                final long value = selector.applyAsLong(t);
                sum += value;
            }
        }
        return sum;
    }

    default double sumOfDoubles(@NotNull ToDoubleFunction<? super T> selector) {
        double sum = 0;
        for (T t : this) {
            if (t != null) {
                final double value = selector.applyAsDouble(t);
                sum += value;
            }
        }
        return sum;
    }

    default BigDecimalX bigDecimalSum(@NotNull Function<? super T, ? extends BigDecimal> selector) {
        BigDecimal sum = BigDecimal.ZERO;
        for (T t : this) {
            if (t != null) {
                final BigDecimal value = selector.apply(t);
                if (value != null) {
                    sum = sum.add(value);
                }
            }
        }
        return BigDecimalX.of(sum);
    }

    default double averageOfInts(@NotNull ToIntFunction<? super T> selector) {
        double sum = 0;
        int counter = 0;
        for (T t : this) {
            if (t != null) {
                final int value = selector.applyAsInt(t);
                sum += value;
                counter++;
            }
        }
        return (counter != 0) ? (sum / counter) : 0;
    }

    default double averageOfLongs(@NotNull ToLongFunction<? super T> selector) {
        double sum = 0;
        int counter = 0;
        for (T t : this) {
            if (t != null) {
                final long value = selector.applyAsLong(t);
                sum += value;
                counter++;
            }
        }
        return (counter != 0) ? (sum / counter) : 0;
    }

    default double averageOfDoubles(@NotNull ToDoubleFunction<? super T> selector) {
        double sum = 0;
        int counter = 0;
        for (T t : this) {
            if (t != null) {
                final double value = selector.applyAsDouble(t);
                sum += value;
                counter++;
            }
        }
        return (counter != 0) ? (sum / counter) : 0;
    }

    default BigDecimalX toBigDecimalAverage(@NotNull Function<? super T, ? extends BigDecimal> selector) {
        return toBigDecimalAverage(selector, 2, RoundingMode.HALF_UP);
    }

    default BigDecimalX toBigDecimalAverage(@NotNull Function<? super T, ? extends BigDecimal> selector, int scale,
                                            @NotNull RoundingMode roundingMode) {
        BigDecimal sum = BigDecimal.ZERO;
        int counter = 0;
        for (T t : this) {
            if (t != null) {
                final BigDecimal value = selector.apply(t);
                if (value != null) {
                    sum = sum.add(value);
                }
                counter++;
            }
        }
        return BigDecimalX.of(sum.divide(BigDecimal.valueOf(counter), scale, roundingMode));
    }

    @NotNull
    default <R extends Comparable<R>> Optional<T> minBy(@NotNull Function<? super T, ? extends R> selector) {
        return IterableXHelper.compareBy(iterator(),
                selector, (first, second) -> first != null && second != null && first.compareTo(second) > 0);
    }

    @NotNull
    default <R extends Comparable<R>> Optional<T> maxBy(@NotNull Function<? super T, ? extends R> selector) {
        return IterableXHelper.compareBy(iterator(),
                selector, (first, second) -> first != null && second != null && first.compareTo(second) < 0);
    }

    @NotNull
    default <R extends Comparable<R>> R minOf(@NotNull Function<? super T, ? extends R> selector) {
        return IterableXHelper
                .comparisonOf(iterator(), selector, (first, second) -> first != null && second != null && first.compareTo(second) > 0);
    }

    @NotNull
    default <R extends Comparable<R>> R maxOf(@NotNull Function<? super T, ? extends R> selector) {
        return IterableXHelper
                .comparisonOf(iterator(), selector, (first, second) -> first != null && second != null && first.compareTo(second) < 0);
    }

    default <R> void forEach(@NotNull Function<? super T, ? extends R> selector,
                             @NotNull Consumer<? super R> consumer) {
        onEach(selector, consumer);
    }

    @NotNull
    IterableX<T> onEach(@NotNull Consumer<? super T> consumer);

    @NotNull <R> IterableX<T> onEach(@NotNull Function<? super T, ? extends R> selector, @NotNull Consumer<? super R> consumer);

    default <R> R fold(@NotNull R initial,
                       @NotNull BiFunction<? super R, ? super T, ? extends R> operation) {
        R accumulator = initial;
        for (T t : this) {
            if (t != null) {
                accumulator = operation.apply(accumulator, t);
            }
        }
        return accumulator;
    }

    default T reduce(@NotNull T initial, @NotNull BinaryOperator<T> operation) {
        return IterableReductions.reduce(this, initial, operation);
    }

    default <R> R reduce(@NotNull R initial,
                         @NotNull Function<? super T, ? extends R> mapper,
                         @NotNull BinaryOperator<R> operation) {
        R accumulator = initial;
        for (T t : this) {
            if (t != null) {
                accumulator = operation.apply(accumulator, mapper.apply(t));
            }
        }
        return accumulator;
    }

    default Optional<T> reduce(@NotNull BinaryOperator<T> operation) {
        return IterableReductions.reduce(iterator(), operation);
    }

    default <A, R> R collect(@NotNull Collector<T, A, R> collector) {
        A result = collector.supplier().get();
        final BiConsumer<A, T> accumulator = collector.accumulator();
        asSequence().filter(Objects::nonNull).forEach(t -> accumulator.accept(result, t));
        return collector.finisher().apply(result);
    }

    default <A, R> R collect(@NotNull Supplier<A> supplier,
                             @NotNull BiConsumer<A, ? super T> accumulator,
                             @NotNull Function<A, R> finisher) {
        A result = supplier.get();
        return collect(supplier, It::noFilter, It::self, It::noFilter, accumulator, finisher);
    }

    default <A, R> A collect(@NotNull Supplier<A> supplier,
                             @NotNull Predicate<T> filter,
                             @NotNull Function<T, R> mapper,
                             @NotNull BiConsumer<A, ? super R> accumulator) {
        A result = supplier.get();
        return collect(supplier, filter, mapper, It::noFilter, accumulator, It::self);
    }

    default <A, U, R> R collect(@NotNull Supplier<A> supplier,
                                @NotNull Predicate<T> filter,
                                @NotNull Function<T, U> mapper,
                                @NotNull Predicate<U> resultFilter,
                                @NotNull BiConsumer<A, ? super U> accumulator,
                                @NotNull Function<A, R> finisher) {
        A result = supplier.get();
        for (T t : this) {
            if (filter.test(t)) {
                U u = mapper.apply(t);
                if (resultFilter.test(u)) {
                    accumulator.accept(result, u);
                }
            }
        }
        return finisher.apply(result);
    }

    default MapX<T, MutableListX<T>> group() {
        return groupBy(It::self);
    }

    default <K> MapX<K, MutableListX<T>> groupBy(@NotNull Function<? super T, ? extends K> classifier) {
        return groupMapping(classifier, It::self);
    }

    default <K, R> MapX<K, MutableListX<R>> groupMapping(@NotNull Function<? super T, ? extends K> classifier,
                                                         @NotNull Function<? super T, ? extends R> valueMapper) {
        return IterableReductions.groupMapping(this, classifier, valueMapper);
    }

    default Pair<ListX<T>, ListX<T>> partition(@NotNull Predicate<T> predicate) {
        return partitionMapping(predicate, It::self);
    }

    default <R> Pair<ListX<R>, ListX<R>> partitionMapping(@NotNull Predicate<T> predicate,
                                                          @NotNull Function<? super T, ? extends R> resultMapper) {
        return IterableReductions.partitionMapping(this, predicate, resultMapper);
    }

    default <S, C extends Collection<S>, R> SetX<R> intersectionOf(@NotNull Function<? super T, ? extends C> toCollectionMapper,
                                                                   @NotNull Function<? super S, ? extends R> selector) {
        return IterableReductions.intersectionOf(this, toCollectionMapper, selector);
    }

    default <R, C extends Collection<R>> SetX<R> intersectionOf(@NotNull Function<? super T, ? extends C> toCollectionMapper) {
        return intersectionOf(toCollectionMapper, It::self);
    }

    default T first() {
        return firstOf(It::self);
    }

    @NotNull
    default T first(@NotNull Predicate<T> predicate) {
        for (T next : this) {
            if (next != null && predicate.test(next)) {
                return next;
            }
        }
        throw IterableXHelper.noValuePresentException();
    }

    @NotNull
    default T firstNot(@NotNull Predicate<T> predicate) {
        return first(predicate.negate());
    }

    default <R> R firstOf(@NotNull Function<? super T, ? extends R> mapper) {
        for (T next : this) {
            if (next != null) {
                final R result = mapper.apply(next);
                if (result != null) {
                    return result;
                }
            }
        }
        throw IterableXHelper.noValuePresentException();
    }

    default Optional<T> findFirst() {
        return findFirstOf(It::self);
    }

    default T findFirstOrElseGet(@NotNull Supplier<T> supplier) {
        return findFirstOf(It::self).orElseGet(supplier);
    }

    default T findFirstOrElse(@NotNull T defaultValue) {
        return findFirstOf(It::self).orElse(defaultValue);
    }

    default Optional<T> findFirst(@NotNull Predicate<T> predicate) {
        for (T next : this) {
            if (next != null && predicate.test(next)) {
                return Optional.of(next);
            }
        }
        return Optional.empty();
    }

    default <R> Optional<R> findFirstOf(@NotNull Function<? super T, ? extends R> mapper) {
        for (T next : this) {
            if (next != null) {
                return Optional.of(next).map(mapper);
            }
        }
        return Optional.empty();
    }

    default T last() {
        return lastOf(It::self);
    }

    default <R> R lastOf(@NotNull Function<? super T, ? extends R> mapper) {
        final Iterator<T> iterator = iterator();
        if (!iterator.hasNext()) {
            throw IterableXHelper.noValuePresentException();
        } else if (this instanceof List) {
            return IterableXHelper.findLastIfInstanceOfList(Objects::nonNull, (List<T>) this).map(mapper)
                    .orElseThrow(IllegalStateException::new);
        } else {
            return IterableXHelper.findLastIfUnknownIterable(Objects::nonNull, iterator).map(mapper)
                    .orElseThrow(IllegalStateException::new);
        }
    }

    default Optional<T> findLast() {
        return findLastOf(It::self);
    }

    default T findLastOrElse(@NotNull T defaultVal) {
        return findFirstOrElseGet(() -> defaultVal);
    }

    default T findLastOrElseGet(@NotNull Supplier<T> supplier) {
        return findLast().orElseGet(supplier);
    }

    default Optional<T> findLast(@NotNull Predicate<T> predicate) {
        return IterableReductions.findLast(this, predicate);
    }

    default <R> Optional<R> findLastOf(@NotNull Function<T, R> mapper) {
        return IterableReductions.findLastOf(this, mapper);
    }

    default boolean any(@NotNull Predicate<T> predicate) {
        return IterableReductions.any(this, predicate);
    }

    default boolean all(@NotNull Predicate<T> predicate) {
        return IterableReductions.all(this, predicate);
    }

    default boolean none(@NotNull Predicate<T> predicate) {
        return IterableReductions.none(this, predicate);
    }

    <A, R> IterableX<R> zipWith(@NotNull Iterable<A> iterable, @NotNull BiFunction<? super T, ? super A, ? extends R> function);

    <R> IterableX<R> zipWithNext(BiFunction<T, T, R> function);

    default <A, R> List<R> zipToListWith(@NotNull Iterable<A> otherIterable,
                                          @NotNull BiFunction<? super T, ? super A, ? extends R> function) {
        final Iterator<A> otherIterator = otherIterable.iterator();
        final Iterator<T> iterator = iterator();
        final int resultListSize = Math.min(IterableXHelper.collectionSizeOrElse(this, 10),
                IterableXHelper.collectionSizeOrElse(otherIterable, 10));

        final MutableListX<R> list = MutableListX.withInitCapacity(resultListSize);
        while (iterator.hasNext() && otherIterator.hasNext()) {
            final T next = iterator.next();
            final A otherNext = otherIterator.next();
            list.add(function.apply(next, otherNext));
        }
        return list;
    }

    IterableX<ListX<T>> chunked(int size);

    <R>IterableX<R> chunked(int size, @NotNull Function<? super ListX<T>, ? extends R> transform);

    IterableX<ListX<T>> windowed(int size);

    <R> IterableX<R> windowed(int size, @NotNull Function<? super ListX<T>, ? extends R> transform);

    IterableX<ListX<T>> windowed(int size, int step);

    <R> IterableX<R> windowed(int size, int step, @NotNull Function<? super ListX<T>, ? extends R> transform);

    IterableX<ListX<T>> windowed(int size, boolean partialWindows);

    IterableX<ListX<T>> windowed(int size, int step, boolean partialWindows);

    <R> IterableX<R> windowed(int size, int step, boolean partialWindows,
                              @NotNull Function<? super ListX<T>, R> transform);

    IterableX<T> skip(long count);

    IterableX<T> skipWhileInclusive(@NotNull Predicate<T> predicate);

    IterableX<T> skipWhile(@NotNull Predicate<T> predicate);

    IterableX<T> take(long n);

    IterableX<T> takeWhile(@NotNull Predicate<T> predicate);

    IterableX<T> takeWhileInclusive(@NotNull Predicate<T> predicate);

    default SetX<T> union(@NotNull Iterable<T> other) {
        MutableSetX<T> union = MutableSetX.empty();
        forEach(union::add);
        other.forEach(union::add);
        return union;
    }

    default <R> SetX<R> union(@NotNull Iterable<T> other, @NotNull Function<? super T, ? extends R> mapper) {
        MutableSetX<R> union = toMutableSetOf(mapper);
        final SetX<R> setX = ListX.of(other).toSetXOf(mapper);
        setX.forEach(union::add);
        return union;
    }

    default String joinToString() {
        return joinToStringBy(Object::toString);
    }

    default String joinToString(@NotNull CharSequence delimiter) {
        return joinToStringBy(Object::toString, delimiter);
    }

    default <R> String joinToStringBy(@NotNull Function<? super T, ? extends R> selector) {
        return joinToStringBy(selector, ", ");
    }

    default <R> String joinToStringBy(@NotNull Function<? super T, ? extends R> selector, CharSequence delimiter) {
        final StringBuilder sb = new StringBuilder();
        final Iterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            final R r = selector.apply(iterator.next());
            sb.append(r).append(iterator.hasNext() ? delimiter : "");
        }
        return sb.toString().trim();
    }

    default StringX joinToStringX() {
        return joinToStringXBy(Object::toString);
    }

    default StringX joinToStringX(CharSequence delimiter) {
        return joinToStringXBy(Object::toString, delimiter);
    }

    default <R> StringX joinToStringXBy(@NotNull Function<? super T, ? extends R> selector) {
        return joinToStringXBy(selector, ", ");
    }

    default <R> StringX joinToStringXBy(@NotNull Function<? super T, ? extends R> selector, CharSequence delimiter) {
        return StringX.of(joinToStringBy(selector, delimiter));
    }

    @Override
    default @NotNull Iterator<IndexedValue<T>> indexedIterator() {
        return IterableXHelper.indexedIterator(iterator());
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
}
