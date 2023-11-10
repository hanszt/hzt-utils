package org.hzt.utils.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;

final class TreeMapX<K, V, R extends Comparable<? super R>> implements SortedMutableMapX<K, V> {

    private final NavigableMap<K, V> map;

    TreeMapX(final SortedMap<K, V> map) {
        this.map = new TreeMap<>(map);
    }

    TreeMapX(final Function<K, R> selector) {
        this(new TreeMap<>(Comparator.comparing(selector)));
    }

    TreeMapX(final Map<K, V> map, final Function<? super K, ? extends R> selector) {
        this.map = new TreeMap<>(Comparator.comparing(selector));
        this.map.putAll(map);
    }

    TreeMapX(final Iterable<Entry<K, V>> iterable, final Function<? super K, ? extends R> selector) {
        map = new TreeMap<>(Comparator.comparing(selector));
        for (final Map.Entry<K, V> entry : iterable) {
            map.put(entry.getKey(), entry.getValue());
        }
    }

    @SafeVarargs
    TreeMapX(final Function<? super K, ? extends R> selector, final Entry<K, V> first, final Entry<K, V>... others) {
        map = new TreeMap<>(Comparator.comparing(selector));
        map.put(first.getKey(), first.getValue());
        for (final Map.Entry<K, V> entry : others) {
            map.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(final Object key) {
        return map.get(key);
    }

    @Override
    public V put(final K key, final V value) {
        return map.put(key, value);
    }

    @Override
    public V remove(final Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Comparator<? super K> comparator() {
        return map.comparator();
    }

    @Override
    public SortedMutableMapX<K, V> subMap(final K fromKey, final K toKey) {
        return SortedMutableMapX.ofSortedMap(map.subMap(fromKey, toKey));
    }

    @Override
    public SortedMutableMapX<K, V> headMap(final K toKey) {
        return SortedMutableMapX.ofSortedMap(map.headMap(toKey));
    }

    @Override
    public SortedMutableMapX<K, V> tailMap(final K fromKey) {
        return SortedMutableMapX.ofSortedMap(map.tailMap(fromKey));
    }

    @Override
    public Entry<K, V> first() {
        return firstEntry();
    }

    @Override
    public Entry<K, V> last() {
        return lastEntry();
    }

    @Override
    public K firstKey() {
        return map.firstKey();
    }

    @Override
    public K lastKey() {
        return map.lastKey();
    }

    @Override
    public MutableSetX<K> keySet() {
        return MutableLinkedSetX.of(map.keySet());
    }

    @Override
    public MutableListX<V> values() {
        return MutableListX.of(map.values());
    }

    @Override
    public MutableSetX<Entry<K, V>> entrySet() {
        return MutableSetX.of(map.entrySet());
    }
    
    @Override
    public K lowerKey(final K key) {
        return map.lowerKey(key);
    }

    @Override
    public K floorKey(final K key) {
        return map.floorKey(key);
    }

    @Override
    public K ceilingKey(final K key) {
        return map.ceilingKey(key);
    }

    @Override
    public K higherKey(final K key) {
        return map.higherKey(key);
    }

    @Override
    public Entry<K, V> lowerEntry(final K key) {
        return map.lowerEntry(key);
    }

    @Override
    public Entry<K, V> floorEntry(final K key) {
        return map.floorEntry(key);
    }

    @Override
    public Entry<K, V> ceilingEntry(final K key) {
        return map.ceilingEntry(key);
    }

    @Override
    public Entry<K, V> higherEntry(final K key) {
        return map.higherEntry(key);
    }

    @Override
    public Entry<K, V> firstEntry() {
        return map.firstEntry();
    }

    @Override
    public Entry<K, V> lastEntry() {
        return map.lastEntry();
    }

    @Override
    public Entry<K, V> pollFirstEntry() {
        return map.pollFirstEntry();
    }

    @Override
    public Entry<K, V> pollLastEntry() {
        return map.pollLastEntry();
    }

    @Override
    public SortedMutableMapX<K, V> descendingMap() {
        return SortedMutableMapX.ofSortedMap(map.descendingMap());
    }

    @Override
    public SortedMutableSetX<K> navigableKeySet() {
        return SortedMutableSetX.of(map.navigableKeySet());
    }

    @Override
    public SortedMutableSetX<K> descendingKeySet() {
        return SortedMutableSetX.of(map.descendingKeySet());
    }

    @Override
    public SortedMutableMapX<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
        return SortedMutableMapX.ofSortedMap(map.subMap(fromKey, fromInclusive, toKey, toInclusive));
    }

    @Override
    public SortedMutableMapX<K, V> headMap(final K toKey, final boolean inclusive) {
        return SortedMutableMapX.ofSortedMap(map.headMap(toKey, inclusive));
    }

    @Override
    public SortedMutableMapX<K, V> tailMap(final K fromKey, final boolean inclusive) {
        return SortedMutableMapX.ofSortedMap(map.tailMap(fromKey, inclusive));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TreeMapX<?, ?, ?> treeMapX = (TreeMapX<?, ?, ?>) o;
        return this.map.equals(treeMapX.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    @Override
    public String toString() {
        return map.toString();
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return map.entrySet().iterator();
    }
}
