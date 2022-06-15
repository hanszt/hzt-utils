package org.hzt.utils.collections;

import org.hzt.utils.It;
import org.hzt.utils.iterables.EntryIterable;
import org.hzt.utils.sequences.EntrySequence;
import org.hzt.utils.tuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("squid:S107")
public interface MapX<K, V> extends CollectionX<Map.Entry<K, V>>, EntryIterable<K, V> {

    static <K, V> MapX<K, V> empty() {
        return MutableMapX.empty();
    }

    static <K, V> MapX<K, V> of(Map<? extends K, ? extends V> map) {
        return MutableMapX.ofMap(map);
    }

    static <K, V> MapX<K, V> of(Iterable<Map.Entry<K, V>> entries) {
        return new HashMapX<>(entries);
    }

    static <K, V> MapX<K, V> of(K k1, V v1) {
        return MapX.of(Map.of(k1, v1));
    }

    static <K, V> MapX<K, V> of(K k1, V v1, K k2, V v2) {
        return MapX.of(Map.of(k1, v1, k2, v2));
    }

    static <K, V> MapX<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return MapX.of(Map.of(k1, v1, k2, v2, k3, v3));
    }

    static <K, V> MapX<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return MapX.of(Map.of(k1, v1, k2, v2, k3, v3, k4, v4));
    }

    static <K, V> MapX<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return MapX.of(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
    }

    static <K, V> MapX<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return MapX.of(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6));
    }

    static <K, V> MapX<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        return MapX.of(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7));
    }

    static <K, V> MapX<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                K k6, V v6, K k7, V v7, K k8, V v8) {
        return MapX.of(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8));
    }

    static <K, V> MapX<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        return MapX.of(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9));
    }

    static <K, V> MapX<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        return MapX.of(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10));
    }

    @SafeVarargs
    static <K, V> MapX<K, V> ofEntries(Map.Entry<? extends K, ? extends V>... entries) {
        return new HashMapX<>(entries);
    }

    static <K, V> MapX<K, V> ofPairs(Iterable<Pair<K, V>> pairs) {
        return new HashMapX<>(EntrySequence.ofPairs(pairs));
    }

    @SafeVarargs
    static <K, V> MapX<K, V> ofPairs(Pair<K, V>... pairs) {
        return new HashMapX<>(pairs);
    }

    static <K, V> MapX<K, V> build(Consumer<MutableMapX<K, V>> mapConsumer) {
        MutableMapX<K, V> map = MutableMapX.empty();
        mapConsumer.accept(map);
        return map;
    }

    <K1, V1> MapX<K1, V1> map(@NotNull Function<? super K, ? extends K1> keyMapper,
                              @NotNull Function<? super V, ? extends V1> valueMapper);

    default <R> ListX<R> map(@NotNull BiFunction<? super K, ? super V, ? extends R> mapper) {
        return MutableListX.of(this).mapNotNull(e -> mapper.apply(e.getKey(), e.getValue()));
    }

    @Override
    default <K1> MapX<K1, V> mapByKeys(@NotNull Function<? super K, ? extends K1> keyMapper) {
        return map(keyMapper, It::self);
    }

    @Override
    default <K1> MapX<K1, V> mapKeys(@NotNull BiFunction<? super K, ? super V, ? extends K1> toKeyMapper) {
        return MapX.of(asSequence().mapKeys(toKeyMapper));
    }

    @Override
    default <V1> MapX<K, V1> mapByValues(@NotNull Function<? super V, ? extends V1> valueMapper) {
        return map(It::self, valueMapper);
    }

    @Override
    default <V1> MapX<K, V1> mapValues(@NotNull BiFunction<? super K, ? super V, ? extends V1> toValueMapper) {
        return MapX.of(asSequence().mapValues(toValueMapper));
    }

    @Override
    default MapX<K, V> filter(@NotNull BiPredicate<? super K, ? super V> biPredicate) {
        return asSequence().filter(biPredicate).toMapX();
    }

    default MapX<K, V> filterKeys(@NotNull Predicate<? super K> predicate) {
        return asSequence().filterKeys(predicate).toMapX();
    }

    default MapX<K, V> filterValues(@NotNull Predicate<? super V> predicate) {
        return asSequence().filterValues(predicate).toMapX();
    }

    @Override
    default MapX<K, V> onEachKey(@NotNull Consumer<? super K> consumer) {
        return mapByKeys(k -> {
            consumer.accept(k);
            return k;
        });
    }

    @Override
    default MapX<K, V> onEachValue(@NotNull Consumer<? super V> consumer) {
        return mapByValues(v -> {
            consumer.accept(v);
            return v;
        });
    }

    @Override
    @NotNull
    default MapX<K, V> onEach(@NotNull Consumer<? super Map.Entry<K, V>> consumer) {
        return MapX.of(CollectionX.super.onEach(consumer));
    }

    @Override
    default MapX<K, V> onEach(@NotNull BiConsumer<? super K, ? super V> biConsumer) {
        return MapX.of(CollectionX.super.onEach(c -> biConsumer.accept(c.getKey(), c.getValue())));
    }

    default <K1, V1> MapX<K1, V1> inverted(@NotNull Function<? super V, ? extends K1> toKeyMapper,
                                           @NotNull Function<? super K, ? extends V1> toValueMapper) {
        Map<K1, V1> resultMap = new HashMap<>();
        for (Map.Entry<K, V> entry : this) {
            V value = entry.getValue();
            if (value != null) {
                resultMap.put(toKeyMapper.apply(value), toValueMapper.apply(entry.getKey()));
            }
        }
        return MapX.of(resultMap);
    }

    default MapX<V, K> inverted() {
        return inverted(It::self, It::self);
    }

    boolean containsKey(Object key);

    boolean containsValue(Object value);

    V get(Object key);

    MutableSetX<K> keySet();

    MutableListX<V> values();

    MutableSetX<Map.Entry<K, V>> entrySet();

    default V getOrDefault(Object key, V defaultValue) {
        V value = get(key);
        return (value != null || containsKey(key)) ? value : defaultValue;
    }

    default void forEach(@NotNull BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        for (Map.Entry<K, V> entry : entrySet()) {
            try {
                K k = entry.getKey();
                V v = entry.getValue();
                action.accept(k, v);
            } catch (IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }
        }
    }

    @Override
    default EntrySequence<K, V> asSequence() {
        return EntrySequence.of(this);
    }

    static <K, V> MapX<K, V> copyOf(MapX<K, V> map) {
        return new HashMapX<>(map);
    }
}
