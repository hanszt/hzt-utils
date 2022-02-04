package hzt.iterables;

import hzt.tuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public interface EntryIterableX<K, V> {

    <K1, V1> EntryIterableX<K1, V1> map(@NotNull BiFunction<K, V, Pair<K1, V1>> biFunction);

    <K1, V1> EntryIterableX<K1, V1> map(@NotNull Function<K, K1> keyMapper, Function<V, V1> valueMapper);

    <K1> EntryIterableX<K1, V> mapKeys(@NotNull Function<K, K1> keyMapper);

    <V1> EntryIterableX<K, V1> mapValues(@NotNull Function<V, V1> valueMapper);

    EntryIterableX<K, V> filterByKeys(@NotNull Predicate<K> predicate);

    EntryIterableX<K, V> filterByValues(@NotNull Predicate<V> predicate);

    void forEach(@NotNull BiConsumer<? super K, ? super V> biConsumer);
}
