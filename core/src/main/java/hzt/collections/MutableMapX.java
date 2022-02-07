package hzt.collections;

import hzt.utils.It;
import hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface MutableMapX<K, V> extends Map<K, V>, MapX<K, V>, Transformable<MutableMapX<K, V>> {

    static <K, V> MutableMapX<K, V> empty() {
        return new HashMapX<>();
    }

    static <K, V> MutableMapX<K, V> of(Map<K, V> map) {
        return new HashMapX<>(map);
    }

    static <K, V> MutableMapX<K, V> ofEntries(Iterable<Map.Entry<K, V>> entries) {
        return new HashMapX<>(entries);
    }

    @Override
    default <K1, V1> MutableMapX<K1, V1> map(@NotNull Function<K, K1> keyMapper, Function<V, V1> valueMapper) {
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
    default <K1> MutableMapX<K1, V> mapKeys(@NotNull Function<K, K1> keyMapper) {
        return map(keyMapper, It::self);
    }

    @Override
    default <V1> MutableMapX<K, V1> mapValues(@NotNull Function<V, V1> valueMapper) {
        return map(It::self, valueMapper);
    }

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
}
