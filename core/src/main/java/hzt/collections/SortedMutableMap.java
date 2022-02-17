package hzt.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.function.Function;
import java.util.stream.Stream;

public interface SortedMutableMap<K, V> extends NavigableMap<K, V>, MutableMap<K, V> {

    static <K, V, R extends Comparable<R>> SortedMutableMap<K, V> comparingByKey(Function<K, R> selector) {
        return new TreeMapX<>(selector);
    }

    static <K, V, R extends Comparable<R>> SortedMutableMap<K, V> of(
            Iterable<Entry<K, V>> iterable, Function<? super K, ? extends R> selector) {
        return new TreeMapX<>(iterable, selector);
    }

    static <K, V, R extends Comparable<R>> SortedMutableMap<K, V> ofMap(
            Map<K, V> map, Function<? super K, ? extends R> selector) {
        return new TreeMapX<>(map, selector);
    }

    static <K, V, R extends Comparable<R>> SortedMutableMap<K, V> ofSortedMap(SortedMap<K, V> sortedMap) {
        return new TreeMapX<>(sortedMap);
    }

    @SafeVarargs
    static <K, V, R extends Comparable<R>> SortedMutableMap<K, V> ofEntries(
            Function<K, R> selector, Map.Entry<K, V> first, Map.Entry<K, V>... others) {
        return new TreeMapX<>(selector, first, others);
    }

    @Override
    default Stream<Entry<K, V>> stream() {
        return MutableMap.super.stream();
    }

    @NotNull
    @Override
    SortedMutableMap<K, V> subMap(K fromKey, K toKey);

    @NotNull
    @Override
    SortedMutableMap<K, V> headMap(K toKey);

    @NotNull
    @Override
    SortedMutableMap<K, V> tailMap(K fromKey);

    @Override
    MutableSet<K> keySet();

    @Override
    MutableList<V> values();

    @Override
    MutableSet<Entry<K, V>> entrySet();

    @Override
    SortedMutableMap<K, V> descendingMap();

    @Override
    SortedMutableSet<K> navigableKeySet();

    @Override
    SortedMutableSet<K> descendingKeySet();

    @Override
    SortedMutableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive);

    @Override
    SortedMutableMap<K, V> headMap(K toKey, boolean inclusive);

    @Override
    SortedMutableMap<K, V> tailMap(K fromKey, boolean inclusive);

    @Override
    @NotNull
    Entry<K, V> first();

    @Override
    @NotNull
    Entry<K, V> last();
}
