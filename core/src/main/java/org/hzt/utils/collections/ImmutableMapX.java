package org.hzt.utils.collections;

import org.hzt.utils.tuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;

final class ImmutableMapX<K, V> implements MapX<K, V> {

    private final Map<K, V> map;

    ImmutableMapX(@NotNull Map<? extends K, ? extends V> map) {
        this.map = Map.copyOf(map);
    }

    ImmutableMapX() {
        this(Map.of());
    }

    ImmutableMapX(Iterable<Entry<K, V>> iterable) {
        final var hashMap = new HashMap<K, V>();
        for (Entry<K, V> entry : iterable) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        this.map = Map.copyOf(hashMap);
    }

    @SafeVarargs
    ImmutableMapX(Pair<K, V>... pairs) {
        final Map<K, V> hashMap = new HashMap<>();
        for (Pair<K, V> pair : pairs) {
            hashMap.put(pair.first(), pair.second());
        }
        this.map = Map.copyOf(hashMap);
    }

    @SafeVarargs
    ImmutableMapX(Entry<? extends K, ? extends V>... entries) {
        final HashMap<K, V> hashMap = new HashMap<>();
        for (Entry<? extends K, ? extends V> entry : entries) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        this.map = Map.copyOf(hashMap);
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
    public <K1, V1> MapX<K1, V1> map(@NotNull Function<? super K, ? extends K1> keyMapper,
                                     @NotNull Function<? super V, ? extends V1> valueMapper) {
        Map<K1, V1> resultMap = new HashMap<>();
        for (Map.Entry<K, V> entry : this) {
            K key = entry.getKey();
            if (key != null) {
                resultMap.put(keyMapper.apply(key), valueMapper.apply(entry.getValue()));
            }
        }
        return MapX.of(resultMap);
    }

    @Override
    public boolean containsKey(Object key) {
        //noinspection SuspiciousMethodCalls
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        //noinspection SuspiciousMethodCalls
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        //noinspection SuspiciousMethodCalls
        return map.get(key);
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

    @NotNull
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return map.entrySet().iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImmutableMapX<?, ?> mapX = (ImmutableMapX<?, ?>) o;
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
