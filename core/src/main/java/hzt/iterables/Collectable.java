package hzt.iterables;

import hzt.collections.ListView;
import hzt.collections.MapView;
import hzt.collections.MutableList;
import hzt.collections.MutableMap;
import hzt.collections.MutableSet;
import hzt.collections.SetView;
import hzt.function.QuadFunction;
import hzt.function.TriFunction;
import hzt.tuples.Pair;
import hzt.tuples.Triple;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface Collectable<T> extends Iterable<T> {

    default T[] toTypedArray(IntFunction<T[]> generator) {
        return toArrayOf(It::self, generator);
    }

    default <R> R[] toArrayOf(@NotNull Function<? super T, ? extends R> mapper, @NotNull IntFunction<R[]> generator) {
        MutableList<R> list = MutableList.empty();
        for (T t : this) {
            if (t != null) {
                list.add(mapper.apply(t));
            }
        }
        return list.toArray(generator);
    }

    default int[] toIntArray(@NotNull ToIntFunction<? super T> mapper) {
        return StreamSupport.stream(spliterator(), false).mapToInt(mapper).toArray();
    }

    default long[] toLongArray(@NotNull ToLongFunction<? super T> mapper) {
        return StreamSupport.stream(spliterator(), false).mapToLong(mapper).toArray();
    }

    default double[] toDoubleArray(@NotNull ToDoubleFunction<? super T> mapper) {
        return StreamSupport.stream(spliterator(), false).mapToDouble(mapper).toArray();
    }

    default <K, V> MutableMap<K, V> toMutableMap(@NotNull Function<? super T, ? extends K> keyMapper,
                                                 @NotNull Function<? super T, ? extends V> valueMapper) {
        MutableMap<K, V> map = MutableMap.empty();
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

    default <K, V> MapView<K, V> toMapView(@NotNull Function<T, K> keyMapper, @NotNull Function<T, V> valueMapper) {
        return toMutableMap(keyMapper, valueMapper);
    }

    default <K, V> Map<K, V> toMap(@NotNull Function<T, K> keyMapper, @NotNull Function<T, V> valueMapper) {
        return toMutableMap(keyMapper, valueMapper);
    }

    default <A, R> R collect(@NotNull Collector<T, A, R> collector) {
        A result = collector.supplier().get();
        final BiConsumer<A, T> accumulator = collector.accumulator();
        for (T t : this) {
            if (t != null) {
                accumulator.accept(result, t);
            }
        }
        return collector.finisher().apply(result);
    }

    default <A, R> R collect(@NotNull Supplier<A> supplier,
                             @NotNull BiConsumer<A, ? super T> accumulator,
                             @NotNull Function<A, R> finisher) {
        return collect(supplier, It::noFilter, It::self, It::noFilter, accumulator, finisher);
    }

    default <A, R> A collect(@NotNull Supplier<A> supplier,
                             @NotNull Predicate<T> filter,
                             @NotNull Function<T, R> mapper,
                             @NotNull BiConsumer<A, ? super R> accumulator) {
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

    default <R1, A1, R2, A2, R> R teeing(@NotNull Collector<? super T, A1, R1> downstream1,
                                 @NotNull Collector<? super T, A2, R2> downstream2,
                                 @NotNull BiFunction<? super R1, ? super R2, R> merger) {
        A1 result1 = downstream1.supplier().get();
        A2 result2 = downstream2.supplier().get();
        final BiConsumer<A1, ? super T> accumulator1 = downstream1.accumulator();
        final BiConsumer<A2, ? super T> accumulator2 = downstream2.accumulator();
        for (T t : this) {
            if (t != null) {
                accumulator1.accept(result1, t);
                accumulator2.accept(result2, t);
            }
        }
        final R1 r1 = downstream1.finisher().apply(result1);
        final R2 r2 = downstream2.finisher().apply(result2);
        return merger.apply(r1, r2);
    }

    default <R1, R2> Pair<R1, R2> teeing(@NotNull Collector<? super T, ?, R1> downstream1,
                                         @NotNull Collector<? super T, ?, R2> downstream2) {
        return teeing(downstream1, downstream2, Pair::of);
    }

    default <R1, A1, R2, A2, R3, A3, R> R branching(@NotNull Collector<? super T, A1, R1> downstream1,
                                        @NotNull Collector<? super T, A2, R2> downstream2,
                                        @NotNull Collector<? super T, A3, R3> downstream3,
                                        @NotNull TriFunction<? super R1, ? super R2, ? super R3, R> merger) {
        A1 result1 = downstream1.supplier().get();
        A2 result2 = downstream2.supplier().get();
        A3 result3 = downstream3.supplier().get();
        final BiConsumer<A1, ? super T> accumulator1 = downstream1.accumulator();
        final BiConsumer<A2, ? super T> accumulator2 = downstream2.accumulator();
        final BiConsumer<A3, ? super T> accumulator3 = downstream3.accumulator();
        for (T t : this) {
            if (t != null) {
                accumulator1.accept(result1, t);
                accumulator2.accept(result2, t);
                accumulator3.accept(result3, t);
            }
        }
        final R1 r1 = downstream1.finisher().apply(result1);
        final R2 r2 = downstream2.finisher().apply(result2);
        final R3 r3 = downstream3.finisher().apply(result3);
        return merger.apply(r1, r2, r3);
    }

    default <R1, R2, R3> Triple<R1, R2, R3> branching(@NotNull Collector<? super T, ?, R1> downstream1,
                                                      @NotNull Collector<? super T, ?, R2> downstream2,
                                                      @NotNull Collector<? super T, ?, R3> downstream3) {
        return branching(downstream1, downstream2, downstream3, Triple::of);
    }

    default <A1, R1, A2, R2, A3, R3, A4, R4, R> R branching(@NotNull Collector<? super T, A1, R1> downstream1,
                                        @NotNull Collector<? super T, A2, R2> downstream2,
                                        @NotNull Collector<? super T, A3, R3> downstream3,
                                        @NotNull Collector<? super T, A4, R4> downstream4,
                                        @NotNull QuadFunction<? super R1, ? super R2, ? super R3, ? super R4, R> merger) {
        A1 result1 = downstream1.supplier().get();
        A2 result2 = downstream2.supplier().get();
        A3 result3 = downstream3.supplier().get();
        A4 result4 = downstream4.supplier().get();
        final BiConsumer<A1, ? super T> accumulator1 = downstream1.accumulator();
        final BiConsumer<A2, ? super T> accumulator2 = downstream2.accumulator();
        final BiConsumer<A3, ? super T> accumulator3 = downstream3.accumulator();
        final BiConsumer<A4, ? super T> accumulator4 = downstream4.accumulator();
        for (T t : this) {
            if (t != null) {
                accumulator1.accept(result1, t);
                accumulator2.accept(result2, t);
                accumulator3.accept(result3, t);
                accumulator4.accept(result4, t);
            }
        }
        final R1 r1 = downstream1.finisher().apply(result1);
        final R2 r2 = downstream2.finisher().apply(result2);
        final R3 r3 = downstream3.finisher().apply(result3);
        final R4 r4 = downstream4.finisher().apply(result4);
        return merger.apply(r1, r2, r3, r4);
    }

    default MutableList<T> toMutableList() {
        return IterableXHelper.mapFilteringTo(this, MutableList::empty, Objects::nonNull, It::self, It::noFilter);
    }

    default ListView<T> toListView() {
        return toMutableList();
    }

    default List<T> toList() {
        return Collections.unmodifiableList(toMutableList());
    }

    default MutableSet<T> toMutableSet() {
        return IterableXHelper.mapFilteringTo(this, MutableSet::empty, Objects::nonNull, It::self, It::noFilter);
    }

    default SetView<T> toSetView() {
        return toMutableSet();
    }

    default Set<T> toSet() {
        return Collections.unmodifiableSet(toMutableSet());
    }
}
