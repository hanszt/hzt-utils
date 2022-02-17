package hzt.collections;

import hzt.iterables.EntryIterable;
import hzt.sequences.EntrySequence;
import hzt.tuples.Pair;
import hzt.utils.It;
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

@SuppressWarnings("unused")
public interface MapView<K, V> extends CollectionView<Map.Entry<K, V>>, EntryIterable<K, V> {

    static <K, V> MapView<K, V> empty() {
        return MutableMap.empty();
    }

    static <K, V> MapView<K, V> of(Map<K, V> map) {
        return MutableMap.ofMap(map);
    }

    static <K, V> MapView<K, V> of(Iterable<Map.Entry<K, V>> entries) {
        return new HashMapX<>(entries);
    }

    static <K, V> MapView<K, V> of(K k1, V v1) {
        return MapView.of(Map.of(k1, v1));
    }

    static <K, V> MapView<K, V> of(K k1, V v1, K k2, V v2) {
        return MapView.of(Map.of(k1, v1, k2, v2));
    }

    static <K, V> MapView<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return MapView.of(Map.of(k1, v1, k2, v2, k3, v3));
    }

    static <K, V> MapView<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return MapView.of(Map.of(k1, v1, k2, v2, k3, v3, k4, v4));
    }

    static <K, V> MapView<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return MapView.of(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
    }

    static <K, V> MapView<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return MapView.of(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6));
    }

    static <K, V> MapView<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        return MapView.of(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7));
    }

    static <K, V> MapView<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                   K k6, V v6, K k7, V v7, K k8, V v8) {
        return MapView.of(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8));
    }

    static <K, V> MapView<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                   K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        return MapView.of(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9));
    }

    static <K, V> MapView<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                   K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        return MapView.of(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10));
    }

    @SafeVarargs
    static <K, V> MapView<K, V> ofEntries(Map.Entry<? extends K, ? extends V>... entries) {
        return new HashMapX<>(entries);
    }

    static <K, V> MapView<K, V> ofPairs(Iterable<Pair<K, V>> pairs) {
        return new HashMapX<>(EntrySequence.ofPairs(pairs));
    }

    @SafeVarargs
    static <K, V> MapView<K, V> ofPairs(Pair<K, V>... pairs) {
        return new HashMapX<>(pairs);
    }

    static <K, V> MapView<K, V> build(Consumer<MutableMap<K, V>> mapConsumer) {
        MutableMap<K, V> map = MutableMap.empty();
        mapConsumer.accept(map);
        return map;
    }

    <K1, V1> MapView<K1, V1> map(@NotNull Function<? super K, ? extends K1> keyMapper,
                                 @NotNull Function<? super V, ? extends V1> valueMapper);

    default <R> ListView<R> map(@NotNull BiFunction<K, V, R> mapper) {
        return MutableList.of(this).mapNotNull(e -> mapper.apply(e.getKey(), e.getValue()));
    }

    @Override
    default <K1> MapView<K1, V> mapKeys(@NotNull Function<? super K, ? extends K1> keyMapper) {
        return map(keyMapper, It::self);
    }

    @Override
    default <K1> MapView<K1, V> mapKeys(@NotNull BiFunction<? super K, ? super V, K1> toKeyMapper) {
        return asSequence().mapKeys(toKeyMapper).toMapView();
    }

    @Override
    default <V1> MapView<K, V1> mapValues(@NotNull Function<? super V, ? extends V1> valueMapper) {
        return map(It::self, valueMapper);
    }

    @Override
    default <V1> MapView<K, V1> mapValues(@NotNull BiFunction<? super K, ? super V, V1> toValueMapper) {
        return asSequence().mapValues(toValueMapper).toMutableMap();
    }

    @Override
    default MapView<K, V> filter(@NotNull BiPredicate<K, V> biPredicate) {
        return asSequence().filter(biPredicate).toMapView();
    }

    default MapView<K, V> filterKeys(@NotNull Predicate<K> predicate) {
        return asSequence().filterKeys(predicate).toMapView();
    }

    default MapView<K, V> filterValues(@NotNull Predicate<V> predicate) {
        return asSequence().filterValues(predicate).toMapView();
    }

    @Override
    default MapView<K, V> onEachKey(@NotNull Consumer<? super K> consumer) {
        throw new UnsupportedOperationException();
    }

    @Override
    default MapView<K, V> onEachValue(@NotNull Consumer<? super V> consumer) {
        throw new UnsupportedOperationException();
    }

    @Override
    @NotNull
    default MapView<K, V> onEach(@NotNull Consumer<? super Map.Entry<K, V>> consumer) {
        return MapView.of(onEach(It::self, consumer));
    }

    @Override
    default MapView<K, V> onEach(@NotNull BiConsumer<? super K, ? super V> biConsumer) {
        return MapView.of(CollectionView.super.onEach(c -> biConsumer.accept(c.getKey(), c.getValue())));
    }

    default <K1, V1> MapView<K1, V1> inverted(Function<K, V1> toValueMapper, Function<V, K1> toKeyMapper) {
        Map<K1, V1> resultMap = new HashMap<>();
        for (Map.Entry<K, V> entry : this) {
            V value = entry.getValue();
            if (value != null) {
                resultMap.put(toKeyMapper.apply(value), toValueMapper.apply(entry.getKey()));
            }
        }
        return MapView.of(resultMap);
    }

    default MapView<V, K> inverted() {
        return inverted(It::self, It::self);
    }

    boolean containsKey(Object key);

    boolean containsValue(Object value);

    V get(Object key);

    MutableSet<K> keySet();

    MutableList<V> values();

    MutableSet<Map.Entry<K, V>> entrySet();

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

    static <K, V> MapView<K, V> copyOf(MapView<K, V> map) {
        return new HashMapX<>(map);
    }
}
