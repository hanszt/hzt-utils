package hzt.collections;

import hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface MutableMap<K, V> extends Map<K, V>, MapView<K, V>, Transformable<MutableMap<K, V>> {

    static <K, V> MutableMap<K, V> empty() {
        return new HashMapX<>();
    }

    static <K, V> MutableMap<K, V> withInitCapacity(int capacity) {
        return new HashMapX<>(capacity);
    }

    static <K, V> MutableMap<K, V> ofMap(Map<K, V> map) {
        return new HashMapX<>(map);
    }

    static <K, V> MutableMap<K, V> of(Iterable<Map.Entry<K, V>> entries) {
        return new HashMapX<>(entries);
    }

    static <K, V> MutableMap<K, V> of(K k1, V v1) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        return MutableMap.ofMap(map);
    }

    static <K, V> MutableMap<K, V> of(K k1, V v1, K k2, V v2) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return MutableMap.ofMap(map);
    }

    static <K, V> MutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return MutableMap.ofMap(map);
    }

    static <K, V> MutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return MutableMap.ofMap(MapHelper.mapAddFour(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4));
    }

    static <K, V> MutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        Map<K, V> map = MapHelper.mapAddFour(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4);
        map.put(k5, v5);
        return MutableMap.ofMap(map);
    }

    static <K, V> MutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        Map<K, V> map = MapHelper.mapAddFour(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return MutableMap.ofMap(map);
    }

    static <K, V> MutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        Map<K, V> map = MapHelper.mapAddFour(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        return MutableMap.ofMap(map);
    }

    static <K, V> MutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                      K k6, V v6, K k7, V v7, K k8, V v8) {
        Map<K, V> map = MapHelper.mapAddFour(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4);
        return MutableMap.ofMap(MapHelper.mapAddFour(map, k5, v5, k6, v6, k7, v7, k8, v8));
    }

    static <K, V> MutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                      K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        Map<K, V> map = MapHelper.mapAddFour(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4);
        MapHelper.mapAddFour(map, k5, v5, k6, v6, k7, v7, k8, v8);
        map.put(k9, v9);
        return MutableMap.ofMap(map);
    }

    static <K, V> MutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                      K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        Map<K, V> map = MapHelper.mapAddFour(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4);
        MapHelper.mapAddFour(map, k5, v5, k6, v6, k7, v7, k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);
        return MutableMap.ofMap(map);
    }

    @Override
    default <K1, V1> MutableMap<K1, V1> map(@NotNull Function<? super K, ? extends K1> keyMapper,
                                            @NotNull Function<? super V, ? extends V1> valueMapper) {
        MutableMap<K1, V1> resultMap = MutableMap.empty();
        for (Map.Entry<K, V> entry : this) {
            K key = entry.getKey();
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
    default V getOrDefault(Object key, V defaultValue) {
        return Map.super.getOrDefault(key, defaultValue);
    }

    @Override
    default void forEach(@NotNull BiConsumer<? super K, ? super V> action) {
        Map.super.forEach(action);
    }

    @Override
    default @NotNull MutableMap<K, V> get() {
        return this;
    }

    default V removeFirst() {
        return remove(first().getKey());
    }

    default V removeLast() {
        return remove(last().getKey());
    }
}
