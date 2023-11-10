package org.hzt.utils.collections;


import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.function.Function;

public interface SortedMutableMapX<K, V> extends NavigableMap<K, V>, MutableMapX<K, V> {

    static <K, V, R extends Comparable<? super R>> SortedMutableMapX<K, V> comparingByKey(Function<K, R> selector) {
        return new TreeMapX<>(selector);
    }

    static <K, V, R extends Comparable<? super R>> SortedMutableMapX<K, V> of(
            Iterable<Entry<K, V>> iterable, Function<? super K, ? extends R> selector) {
        return new TreeMapX<>(iterable, selector);
    }

    static <K, V, R extends Comparable<? super R>> SortedMutableMapX<K, V> ofMap(
            Map<K, V> map, Function<? super K, ? extends R> selector) {
        return new TreeMapX<>(map, selector);
    }

    static <K, V> SortedMutableMapX<K, V> ofSortedMap(SortedMap<K, V> sortedMap) {
        return new TreeMapX<>(sortedMap);
    }

    @SafeVarargs
    static <K, V, R extends Comparable<? super R>> SortedMutableMapX<K, V> ofEntries(
            Function<K, R> selector, Map.Entry<K, V> first, Map.Entry<K, V>... others) {
        return new TreeMapX<>(selector, first, others);
    }

    @Override
    SortedMutableMapX<K, V> subMap(K fromKey, K toKey);

    @Override
    SortedMutableMapX<K, V> headMap(K toKey);

    @Override
    SortedMutableMapX<K, V> tailMap(K fromKey);

    @Override
    MutableSetX<K> keySet();

    @Override
    MutableListX<V> values();

    @Override
    MutableSetX<Entry<K, V>> entrySet();

    @Override
    SortedMutableMapX<K, V> descendingMap();

    @Override
    SortedMutableSetX<K> navigableKeySet();

    @Override
    SortedMutableSetX<K> descendingKeySet();

    @Override
    SortedMutableMapX<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive);

    @Override
    SortedMutableMapX<K, V> headMap(K toKey, boolean inclusive);

    @Override
    SortedMutableMapX<K, V> tailMap(K fromKey, boolean inclusive);

    @Override
    Entry<K, V> first();

    @Override
    Entry<K, V> last();
}
