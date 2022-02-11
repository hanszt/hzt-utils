package hzt.collections;

import hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface MutableMapX<K, V> extends Map<K, V>, MapX<K, V>, Transformable<MutableMapX<K, V>> {

    static <K, V> MutableMapX<K, V> empty() {
        return new HashMapX<>();
    }

    static <K, V> MutableMapX<K, V> ofMap(Map<K, V> map) {
        return new HashMapX<>(map);
    }

    static <K, V> MutableMapX<K, V> of(Iterable<Map.Entry<K, V>> entries) {
        return new HashMapX<>(entries);
    }

    static <K, V> MutableMapX<K, V> of(K k1, V v1) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        return MutableMapX.ofMap(map);
    }

    static <K, V> MutableMapX<K, V> of(K k1, V v1, K k2, V v2) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return MutableMapX.ofMap(map);
    }

    static <K, V> MutableMapX<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return MutableMapX.ofMap(map);
    }

    static <K, V> MutableMapX<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return MutableMapX.ofMap(MapHelper.mapAddFour(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4));
    }

    static <K, V> MutableMapX<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        Map<K, V> map = MapHelper.mapAddFour(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4);
        map.put(k5, v5);
        return MutableMapX.ofMap(map);
    }

    static <K, V> MutableMapX<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        Map<K, V> map = MapHelper.mapAddFour(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return MutableMapX.ofMap(map);
    }

    static <K, V> MutableMapX<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        Map<K, V> map = MapHelper.mapAddFour(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        return MutableMapX.ofMap(map);
    }

    static <K, V> MutableMapX<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                K k6, V v6, K k7, V v7, K k8, V v8) {
        Map<K, V> map = MapHelper.mapAddFour(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4);
        return MutableMapX.ofMap(MapHelper.mapAddFour(map, k5, v5, k6, v6, k7, v7, k8, v8));
    }

    static <K, V> MutableMapX<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        Map<K, V> map = MapHelper.mapAddFour(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4);
        MapHelper.mapAddFour(map, k5, v5, k6, v6, k7, v7, k8, v8);
        map.put(k9, v9);
        return MutableMapX.ofMap(map);
    }

    static <K, V> MutableMapX<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        Map<K, V> map = MapHelper.mapAddFour(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4);
        MapHelper.mapAddFour(map, k5, v5, k6, v6, k7, v7, k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);
        return MutableMapX.ofMap(map);
    }

    static <K, V> MutableMapX<K, V> ofEntries(Iterable<Map.Entry<K, V>> entries) {
        return new HashMapX<>(entries);
    }

    @Override
    default <K1, V1> MutableMapX<K1, V1> map(@NotNull Function<? super K, ? extends K1> keyMapper,
                                             @NotNull Function<? super V, ? extends V1> valueMapper) {
        MutableMapX<K1, V1> resultMap = MutableMapX.empty();
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
    default @NotNull MutableMapX<K, V> get() {
        return this;
    }

    default V removeFirst() {
        return remove(first().getKey());
    }

    default V removeLast() {
        return remove(last().getKey());
    }
}
