package org.hzt.utils.collections;

import org.hzt.utils.Transformable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@SuppressWarnings("squid:S107")
public interface MutableMapX<K, V> extends Map<K, V>, MapX<K, V>, Transformable<MutableMapX<K, V>> {

    static <K, V> MutableMapX<K, V> empty() {
        return new HashMapX<>();
    }

    static <K, V> MutableMapX<K, V> withInitCapacity(final int capacity) {
        return new HashMapX<>(capacity);
    }

    static <K, V> MutableMapX<K, V> ofMap(final Map<? extends K, ? extends V> map) {
        return new HashMapX<>(map);
    }

    static <K, V> MutableMapX<K, V> of(final Iterable<Map.Entry<K, V>> entries) {
        return new HashMapX<>(entries);
    }

    static <K, V> MutableMapX<K, V> of(final K k1, final V v1) {
        final Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        return MutableMapX.ofMap(map);
    }

    static <K, V> MutableMapX<K, V> of(final K k1, final V v1, final K k2, final V v2) {
        final Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return MutableMapX.ofMap(map);
    }

    static <K, V> MutableMapX<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        final Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return MutableMapX.ofMap(map);
    }

    static <K, V> MutableMapX<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        return MutableMapX.ofMap(Map.of(k1, v1, k2, v2, k3, v3, k4, v4));
    }

    static <K, V> MutableMapX<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
       return MutableMapX.ofMap(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
    }

    static <K, V> MutableMapX<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6) {
        return MutableMapX.ofMap(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6));
    }

    static <K, V> MutableMapX<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7) {
        return MutableMapX.ofMap(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7));
    }

    static <K, V> MutableMapX<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5,
                                       final K k6, final V v6, final K k7, final V v7, final K k8, final V v8) {
        return MutableMapX.ofMap(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8));
    }

    static <K, V> MutableMapX<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5,
                                       final K k6, final V v6, final K k7, final V v7, final K k8, final V v8, final K k9, final V v9) {
        return MutableMapX.ofMap(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9));
    }

    static <K, V> MutableMapX<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5,
                                       final K k6, final V v6, final K k7, final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10) {
        return MutableMapX.ofMap(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10));
    }

    @Override
    default <K1, V1> MutableMapX<K1, V1> map(final Function<? super K, ? extends K1> keyMapper,
                                             final Function<? super V, ? extends V1> valueMapper) {
        final MutableMapX<K1, V1> resultMap = MutableMapX.empty();
        for (final var entry : this) {
            final var key = entry.getKey();
            if (key != null) {
                resultMap.put(keyMapper.apply(key), valueMapper.apply(entry.getValue()));
            }
        }
        return resultMap;
    }

    @Override
    boolean isEmpty();

    @Override
    int size();

    @Override
    default V getOrDefault(final Object key, final V defaultValue) {
        return Map.super.getOrDefault(key, defaultValue);
    }

    @Override
    default void forEach(final BiConsumer<? super K, ? super V> action) {
        Map.super.forEach(action);
    }

    @Override
    default MutableMapX<K, V> get() {
        return this;
    }

    default V removeFirst() {
        return remove(first().getKey());
    }

    default V removeLast() {
        return remove(last().getKey());
    }
}
