package hzt.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.function.Function;
import java.util.stream.Stream;

public interface NavigableMapX<K, V> extends NavigableMap<K, V>, MutableMapX<K, V> {

    static <K, V, R extends Comparable<R>> NavigableMapX<K, V> comparingByKey(Function<K, R> selector) {
        return new TreeMapX<>(selector);
    }

    static <K, V, R extends Comparable<R>> NavigableMapX<K, V> of(
            Iterable<Entry<K, V>> iterable, Function<? super K, ? extends R> selector) {
        return new TreeMapX<>(iterable, selector);
    }

    static <K, V, R extends Comparable<R>> NavigableMapX<K, V> ofMap(
            Map<K, V> map, Function<? super K, ? extends R> selector) {
        return new TreeMapX<>(map, selector);
    }

    static <K, V, R extends Comparable<R>> NavigableMapX<K, V> ofSortedMap(SortedMap<K, V> sortedMap) {
        return new TreeMapX<>(sortedMap);
    }

    @SafeVarargs
    static <K, V, R extends Comparable<R>> NavigableMapX<K, V> ofEntries(
            Function<K, R> selector, Map.Entry<K, V> first, Map.Entry<K, V>... others) {
        return new TreeMapX<>(selector, first, others);
    }

    @Override
    default Stream<Entry<K, V>> stream() {
        return MutableMapX.super.stream();
    }

    @NotNull
    @Override
    NavigableMapX<K, V> subMap(K fromKey, K toKey);

    @NotNull
    @Override
    NavigableMapX<K, V> headMap(K toKey);

    @NotNull
    @Override
    NavigableMapX<K, V> tailMap(K fromKey);

    @Override
    MutableSetX<K> keySet();

    @Override
    MutableCollection<V> values();

    @Override
    MutableSetX<Entry<K, V>> entrySet();

    @Override
    NavigableMapX<K, V> descendingMap();

    @Override
    NavigableSetX<K> navigableKeySet();

    @Override
    NavigableSetX<K> descendingKeySet();

    @Override
    NavigableMapX<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive);

    @Override
    NavigableMapX<K, V> headMap(K toKey, boolean inclusive);

    @Override
    NavigableMapX<K, V> tailMap(K fromKey, boolean inclusive);

    @Override
    @NotNull
    Entry<K, V> first();

    @Override
    Entry<K, V> last();

}
