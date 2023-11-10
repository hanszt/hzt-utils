package org.hzt.utils.collections;

import org.hzt.utils.tuples.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;

final class ImmutableMapX<K, V> implements MapX<K, V> {

    private final Map<K, V> map;

    ImmutableMapX(final Map<? extends K, ? extends V> map) {
        this.map = Collections.unmodifiableMap(map);
    }

    ImmutableMapX() {
        this(Collections.emptyMap());
    }

    ImmutableMapX(final Iterable<Entry<K, V>> iterable) {
        final HashMap<K, V> hashMap = new HashMap<>();
        for (final Entry<K, V> entry : iterable) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        this.map = Collections.unmodifiableMap(hashMap);
    }

    @SafeVarargs
    ImmutableMapX(final Pair<K, V>... pairs) {
        final Map<K, V> hashMap = new HashMap<>();
        for (final Pair<K, V> pair : pairs) {
            hashMap.put(pair.first(), pair.second());
        }
        this.map = Collections.unmodifiableMap(hashMap);
    }

    @SafeVarargs
    ImmutableMapX(final Entry<? extends K, ? extends V>... entries) {
        final HashMap<K, V> hashMap = new HashMap<>();
        for (final Entry<? extends K, ? extends V> entry : entries) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        this.map = Collections.unmodifiableMap(hashMap);
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
    public <K1, V1> MapX<K1, V1> map(final Function<? super K, ? extends K1> keyMapper,
                                     final Function<? super V, ? extends V1> valueMapper) {
        final Map<K1, V1> resultMap = new HashMap<>();
        for (final Map.Entry<K, V> entry : this) {
            final K key = entry.getKey();
            if (key != null) {
                resultMap.put(keyMapper.apply(key), valueMapper.apply(entry.getValue()));
            }
        }
        return MapX.of(resultMap);
    }

    @Override
    public boolean containsKey(final Object key) {
        //noinspection SuspiciousMethodCalls
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        //noinspection SuspiciousMethodCalls
        return map.containsValue(value);
    }

    @Override
    public V get(final Object key) {
        //noinspection SuspiciousMethodCalls
        return map.get(key);
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
    public Iterator<Entry<K, V>> iterator() {
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
        final ImmutableMapX<?, ?> mapX = (ImmutableMapX<?, ?>) o;
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
