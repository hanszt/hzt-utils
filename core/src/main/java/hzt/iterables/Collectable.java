package hzt.iterables;

import hzt.collections.ArrayX;
import hzt.collections.ListX;
import hzt.collections.MapX;
import hzt.collections.MutableListX;
import hzt.collections.MutableMapX;
import hzt.collections.MutableSetX;
import hzt.collections.SetX;
import hzt.collectors.CollectorsX;
import hzt.function.QuadFunction;
import hzt.function.TriFunction;
import hzt.tuples.Pair;
import hzt.tuples.Triple;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

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

@FunctionalInterface
public interface Collectable<T> extends Iterable<T> {

    default T[] toTypedArray(IntFunction<T[]> generator) {
        return toArrayOf(It::self, generator);
    }

    default <R> R[] toArrayOf(@NotNull Function<? super T, ? extends R> mapper, @NotNull IntFunction<R[]> generator) {
        MutableListX<R> list = MutableListX.empty();
        for (T t : this) {
            if (t != null) {
                list.add(mapper.apply(t));
            }
        }
        return list.toArray(generator.apply(0));
    }

    default ArrayX<T> toArrayX(IntFunction<T[]> generator) {
        return ArrayX.of(toTypedArray(generator));
    }

    default <R> ArrayX<R> toArrayXOf(@NotNull Function<? super T, ? extends R> mapper, @NotNull IntFunction<R[]> generator) {
        return ArrayX.of(toArrayOf(mapper, generator));
    }

    default int[] toIntArray(@NotNull ToIntFunction<? super T> mapper) {
        int[] array = new int[(int) IterableXHelper.count(this, Objects::nonNull)];
        IterableXHelper.exposeIntIndexedNonNullVal(this, (i, t) -> array[i] = mapper.applyAsInt(t));
        return array;
    }

    default long[] toLongArray(@NotNull ToLongFunction<? super T> mapper) {
        long[] array = new long[(int) IterableXHelper.count(this, Objects::nonNull)];
        IterableXHelper.exposeIntIndexedNonNullVal(this, (i, t) -> array[i] = mapper.applyAsLong(t));
        return array;
    }

    default double[] toDoubleArray(@NotNull ToDoubleFunction<? super T> mapper) {
        double[] array = new double[(int) IterableXHelper.count(this, Objects::nonNull)];
        IterableXHelper.exposeIntIndexedNonNullVal(this, (i, t) -> array[i] = mapper.applyAsDouble(t));
        return array;
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

    default <R1, R2, R> R teeing(@NotNull Collector<? super T, ?, R1> downstream1,
                                 @NotNull Collector<? super T, ?, R2> downstream2,
                                 @NotNull BiFunction<? super R1, ? super R2, R> merger) {
        return collect(CollectorsX.teeing(downstream1, downstream2, merger));
    }

    default <R1, R2> Pair<R1, R2> teeing(@NotNull Collector<? super T, ?, R1> downstream1,
                                         @NotNull Collector<? super T, ?, R2> downstream2) {
        return teeing(downstream1, downstream2, Pair::of);
    }

    default <R1, R2, R3, R> R branching(@NotNull Collector<? super T, ?, R1> downstream1,
                                        @NotNull Collector<? super T, ?, R2> downstream2,
                                        @NotNull Collector<? super T, ?, R3> downstream3,
                                        @NotNull TriFunction<? super R1, ? super R2, ? super R3, R> merger) {
        return collect(CollectorsX.branching(downstream1, downstream2, downstream3, merger));
    }

    default <R1, R2, R3> Triple<R1, R2, R3> branching(@NotNull Collector<? super T, ?, R1> downstream1,
                                                      @NotNull Collector<? super T, ?, R2> downstream2,
                                                      @NotNull Collector<? super T, ?, R3> downstream3) {
        return branching(downstream1, downstream2, downstream3, Triple::of);
    }

    default <R1, R2, R3, R4, R> R branching(@NotNull Collector<? super T, ?, R1> downstream1,
                                            @NotNull Collector<? super T, ?, R2> downstream2,
                                            @NotNull Collector<? super T, ?, R3> downstream3,
                                            @NotNull Collector<? super T, ?, R4> downstream4,
                                            @NotNull QuadFunction<? super R1, ? super R2, ? super R3, ? super R4, R> merger) {
        return collect(CollectorsX.branching(downstream1, downstream2, downstream3, downstream4, merger));
    }

    default MutableListX<T> toMutableList() {
        return IterableXHelper.mapFilteringTo(this, MutableListX::of, Objects::nonNull, It::self, It::noFilter);
    }

    default ListX<T> toListX() {
        return toMutableList();
    }

    default List<T> toList() {
        return List.copyOf(toMutableList());
    }

    default MutableSetX<T> toMutableSet() {
        return IterableXHelper.mapFilteringTo(this, MutableSetX::of, Objects::nonNull, It::self, It::noFilter);
    }

    default SetX<T> toSetX() {
        return toMutableSet();
    }

    default Set<T> toSet() {
        return Set.copyOf(toMutableSet());
    }
}
