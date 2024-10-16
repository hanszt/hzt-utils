package org.hzt.utils.collections;

import org.hzt.utils.tuples.Pair;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

final class HashMapX<K, V> extends AbstractMap<K, V> implements MutableMapX<K, V> {

    private final Map<K, V> map;

    HashMapX(final Map<? extends K, ? extends V> map) {
        this.map = new LinkedHashMap<>(map);
    }

    HashMapX() {
        this(new HashMap<>());
    }

    HashMapX(final int capacity) {
        this(new HashMap<>(capacity));
    }

    HashMapX(final Iterable<Entry<K, V>> iterable) {
        map = new LinkedHashMap<>();
        for (final Map.Entry<K, V> entry : iterable) {
            map.put(entry.getKey(), entry.getValue());
        }
    }

    @SafeVarargs
    HashMapX(final Pair<K, V>... pairs) {
        map = new HashMap<>();
        for (final Pair<K, V> pair : pairs) {
            map.put(pair.first(), pair.second());
        }
    }

    @SafeVarargs
    HashMapX(final Entry<? extends K, ? extends V>... entries) {
        map = new HashMap<>();
        for (final Entry<? extends K, ? extends V> entry : entries) {
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
}
