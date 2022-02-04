package hzt.iterables;

import hzt.tuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public interface EntryIterable<K, V> extends Iterable<Map.Entry<K, V>> {

    <K1, V1> EntryIterable<K1, V1> map(@NotNull BiFunction<K, V, Pair<K1, V1>> biFunction);

    <K1, V1> EntryIterable<K1, V1> map(@NotNull Function<K, K1> keyMapper, Function<V, V1> valueMapper);

    <K1> EntryIterable<K1, V> mapKeys(@NotNull Function<K, K1> keyMapper);

    <V1> EntryIterable<K, V1> mapValues(@NotNull Function<V, V1> valueMapper);

    EntryIterable<K, V> filterByKeys(@NotNull Predicate<K> predicate);

    EntryIterable<K, V> filterByValues(@NotNull Predicate<V> predicate);

    void forEach(@NotNull BiConsumer<? super K, ? super V> biConsumer);
}
