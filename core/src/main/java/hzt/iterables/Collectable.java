package hzt.iterables;

import hzt.collections.ArrayX;
import hzt.collections.ListX;
import hzt.collections.MapX;
import hzt.collections.MutableListX;
import hzt.collections.MutableMapX;
import hzt.collections.MutableSetX;
import hzt.collections.SetX;
import hzt.sequences.Sequence;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;

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
        IterableXHelper.exposeIndexedNonNullVal(this, (i, t) -> array[i] = mapper.applyAsInt(t));
        return array;
    }

    default long[] toLongArray(@NotNull ToLongFunction<? super T> mapper) {
        long[] array = new long[(int) IterableXHelper.count(this, Objects::nonNull)];
        IterableXHelper.exposeIndexedNonNullVal(this, (i, t) -> array[i] = mapper.applyAsLong(t));
        return array;
    }

    default double[] toDoubleArray(@NotNull ToDoubleFunction<? super T> mapper) {
        double[] array = new double[(int) IterableXHelper.count(this, Objects::nonNull)];
        IterableXHelper.exposeIndexedNonNullVal(this, (i, t) -> array[i] = mapper.applyAsDouble(t));
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

    <K> EntryIterable<K, T> associateBy(@NotNull Function<? super T, ? extends K> keyMapper);

    <V> EntryIterable<T, V> associateWith(@NotNull Function<? super T, ? extends V> valueMapper);

    default <A, R> R collect(@NotNull Collector<T, A, R> collector) {
        A result = collector.supplier().get();
        final BiConsumer<A, T> accumulator = collector.accumulator();
        Sequence.of(this).filter(Objects::nonNull).forEach(t -> accumulator.accept(result, t));
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

    default MutableListX<T> toMutableList() {
        return IterableXHelper.mapFilteringTo(this, MutableListX::of, Objects::nonNull, It::self, It::noFilter);
    }

    default ListX<T> toListX() {
        return toMutableList();
    }

    default List<T> toList() {
        return Collections.unmodifiableList(toMutableList());
    }

    default MutableSetX<T> toMutableSet() {
        return IterableXHelper.mapFilteringTo(this, MutableSetX::of, Objects::nonNull, It::self, It::noFilter);
    }

    default SetX<T> toSetX() {
        return toMutableSet();
    }

    default Set<T> toSet() {
        return Collections.unmodifiableSet(toMutableSet());
    }
}
