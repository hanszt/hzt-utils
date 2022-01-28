package hzt.collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;

final class TreeMapX<K, V, R extends Comparable<R>> implements NavigableMapX<K, V> {

    private final NavigableMap<K, V> map;

    TreeMapX(NavigableMap<K, V> map) {
        this.map = map;
    }

    TreeMapX(Function<K, R> selector) {
        this(new TreeMap<>(Comparator.comparing(selector)));
    }

    TreeMapX(Map<K, V> map, Function<? super K, ? extends R> selector) {
        TreeMap<K, V> newMap = new TreeMap<>(Comparator.comparing(selector));
        newMap.putAll(map);
        this.map = newMap;
    }
    TreeMapX(Iterable<Entry<K, V>> iterable, Function<? super K, ? extends R> selector) {
        TreeMap<K, V> newMap = new TreeMap<>(Comparator.comparing(selector));
        for (Map.Entry<K, V> entry : iterable) {
            newMap.put(entry.getKey(), entry.getValue());
        }
        this.map = newMap;
    }

    @SafeVarargs
    TreeMapX(Function<? super K, ? extends R> selector, Entry<K, V> first, Entry<K, V>... others) {
        TreeMap<K, V> newMap = new TreeMap<>(Comparator.comparing(selector));
        newMap.put(first.getKey(), first.getValue());
        for (Map.Entry<K, V> entry : others) {
            newMap.put(entry.getKey(), entry.getValue());
        }
        this.map = newMap;
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
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        return map.subMap(fromKey, toKey);
    }

    @NotNull
    @Override
    public SortedMap<K, V> headMap(K toKey) {
        return map.headMap(toKey);
    }

    @NotNull
    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        return map.tailMap(fromKey);
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
        return MutableSetX.of(map.keySet());
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
    public Iterable<Entry<K, V>> iterable() {
        return map.entrySet();
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
    public NavigableMap<K, V> descendingMap() {
        return map.descendingMap();
    }

    @Override
    public NavigableSet<K> navigableKeySet() {
        return map.navigableKeySet();
    }

    @Override
    public NavigableSet<K> descendingKeySet() {
        return map.descendingKeySet();
    }

    @Override
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        return map.subMap(fromKey, fromInclusive, toKey, toInclusive);
    }

    @Override
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        return map.headMap(toKey, inclusive);
    }

    @Override
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        return map.tailMap(fromKey, inclusive);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TreeMapX<?, ?, ?> mapX = (TreeMapX<?, ?, ?>) o;
        return map.equals(mapX.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    @Override
    public String toString() {
        return "MapX{" +
                "map=" + map +
                '}';
    }

}
