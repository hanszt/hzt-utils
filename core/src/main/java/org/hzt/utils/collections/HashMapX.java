package org.hzt.utils.collections;

import org.hzt.utils.tuples.Pair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

final class HashMapX<K, V> implements MutableMapX<K, V> {

    private final Map<K, V> map;

    HashMapX(final Map<? extends K, ? extends V> map) {
        this.map = new LinkedHashMap<>(map);
    }

    HashMapX() {
        this(new HashMap<>());
    }

    HashMapX(final int capacity) {
        this(HashMap.newHashMap(capacity));
    }

    HashMapX(final Iterable<Entry<K, V>> iterable) {
        map = new LinkedHashMap<>();
        for (final var entry : iterable) {
            map.put(entry.getKey(), entry.getValue());
        }
    }

    @SafeVarargs
    HashMapX(final Pair<K, V>... pairs) {
        map = new HashMap<>();
        for (final var pair : pairs) {
            map.put(pair.first(), pair.second());
        }
    }

    @SafeVarargs
    HashMapX(final Entry<? extends K, ? extends V>... entries) {
        map = new HashMap<>();
        for (final var entry : entries) {
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
    public Iterator<Map.Entry<K, V>> iterator() {
        return map.entrySet().iterator();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final var mapX = (HashMapX<?, ?>) o;
        return map.equals(mapX.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
