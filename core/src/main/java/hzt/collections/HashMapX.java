package hzt.collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

final class HashMapX<K, V> implements MutableMapX<K, V> {

    private final Map<K, V> map;

    HashMapX(Map<K, V> map) {
        this.map = map;
    }

    HashMapX() {
        this(new HashMap<>());
    }

    HashMapX(Iterable<Entry<K, V>> iterable) {
        Map<K, V> newMap = new HashMap<>();
        for (Map.Entry<K, V> entry : iterable) {
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

    @NotNull
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
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
        HashMapX<?, ?> mapX = (HashMapX<?, ?>) o;
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
