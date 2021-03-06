package org.hzt.utils.collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    TreeMapX(SortedMap<K, V> map) {
        this.map = new TreeMap<>(map);
    }

    TreeMapX(Function<K, R> selector) {
        this(new TreeMap<>(Comparator.comparing(selector)));
    }

    TreeMapX(Map<K, V> map, Function<? super K, ? extends R> selector) {
        this.map = new TreeMap<>(Comparator.comparing(selector));
        this.map.putAll(map);
    }
    TreeMapX(Iterable<Entry<K, V>> iterable, Function<? super K, ? extends R> selector) {
        map = new TreeMap<>(Comparator.comparing(selector));
        for (Map.Entry<K, V> entry : iterable) {
            map.put(entry.getKey(), entry.getValue());
        }
    }

    @SafeVarargs
    TreeMapX(Function<? super K, ? extends R> selector, Entry<K, V> first, Entry<K, V>... others) {
        map = new TreeMap<>(Comparator.comparing(selector));
        map.put(first.getKey(), first.getValue());
        for (Map.Entry<K, V> entry : others) {
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
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
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

    @NotNull
    @Override
    public SortedMutableMapX<K, V> subMap(K fromKey, K toKey) {
        return SortedMutableMapX.ofSortedMap(map.subMap(fromKey, toKey));
    }

    @NotNull
    @Override
    public SortedMutableMapX<K, V> headMap(K toKey) {
        return SortedMutableMapX.ofSortedMap(map.headMap(toKey));
    }

    @NotNull
    @Override
    public SortedMutableMapX<K, V> tailMap(K fromKey) {
        return SortedMutableMapX.ofSortedMap(map.tailMap(fromKey));
    }

    @Override
    public @NotNull Entry<K, V> first() {
        return firstEntry();
    }

    @Override
    public @NotNull Entry<K, V> last() {
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
    public @NotNull MutableSetX<K> keySet() {
        return MutableLinkedSetX.of(map.keySet());
    }

    @Override
    public @NotNull MutableListX<V> values() {
        return MutableListX.of(map.values());
    }

    @Override
    public @NotNull MutableSetX<Entry<K, V>> entrySet() {
        return MutableSetX.of(map.entrySet());
    }
    
    @Override
    public K lowerKey(K key) {
        return map.lowerKey(key);
    }

    @Override
    public K floorKey(K key) {
        return map.floorKey(key);
    }

    @Override
    public K ceilingKey(K key) {
        return map.ceilingKey(key);
    }

    @Override
    public K higherKey(K key) {
        return map.higherKey(key);
    }

    @Override
    public Entry<K, V> lowerEntry(K key) {
        return map.lowerEntry(key);
    }

    @Override
    public Entry<K, V> floorEntry(K key) {
        return map.floorEntry(key);
    }

    @Override
    public Entry<K, V> ceilingEntry(K key) {
        return map.ceilingEntry(key);
    }

    @Override
    public Entry<K, V> higherEntry(K key) {
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
    public SortedMutableMapX<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        return SortedMutableMapX.ofSortedMap(map.subMap(fromKey, fromInclusive, toKey, toInclusive));
    }

    @Override
    public SortedMutableMapX<K, V> headMap(K toKey, boolean inclusive) {
        return SortedMutableMapX.ofSortedMap(map.headMap(toKey, inclusive));
    }

    @Override
    public SortedMutableMapX<K, V> tailMap(K fromKey, boolean inclusive) {
        return SortedMutableMapX.ofSortedMap(map.tailMap(fromKey, inclusive));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TreeMapX<?, ?, ?> treeMapX = (TreeMapX<?, ?, ?>) o;
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

    @NotNull
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return map.entrySet().iterator();
    }
}
