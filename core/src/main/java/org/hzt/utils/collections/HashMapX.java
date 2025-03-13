package org.hzt.utils.collections;

import org.hzt.utils.tuples.Pair;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

final class HashMapX<K, V> extends AbstractMap<K, V> implements MutableMapX<K, V> {

    private final Map<K, V> map;
    private boolean isUnmodifiable = false;

    HashMapX(final Map<? extends K, ? extends V> map) {
        this.map = new HashMap<>(map);
    }

    HashMapX() {
        this(new HashMap<>());
    }

    HashMapX(final int capacity) {
        this(HashMap.newHashMap(capacity));
    }

    HashMapX(final Iterable<Entry<K, V>> iterable) {
        map = new HashMap<>();
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

    HashMapX(final Consumer<MutableMapX<K, V>> factory) {
        map = new HashMap<>();
        factory.accept(this);
        isUnmodifiable = true;
    }

    HashMapX(final int nrOfMappings, final Consumer<MutableMapX<K, V>> factory) {
        map = HashMap.newHashMap(nrOfMappings);
        factory.accept(this);
        isUnmodifiable = true;
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
        throwIfUnmodifiable();
        return map.put(key, value);
    }

    @Override
    public V remove(final Object key) {
        throwIfUnmodifiable();
        return map.remove(key);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        throwIfUnmodifiable();
        map.putAll(m);
    }

    @Override
    public void clear() {
        throwIfUnmodifiable();
        map.clear();
    }

    @Override
    public MutableSetX<K> keySet() {
        return MutableSetX.of(map.keySet());
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
        final var iterator = map.entrySet().iterator();
        return new Iterator<>() {

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Entry<K, V> next() {
                return iterator.next();
            }

            @Override
            public void remove() {
                throwIfUnmodifiable();
                iterator.remove();
            }
        };
    }

    private void throwIfUnmodifiable() {
        if (isUnmodifiable) {
            throw new UnsupportedOperationException();
        }
    }
}
