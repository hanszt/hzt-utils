package org.hzt.utils.collections;

import org.hzt.utils.It;
import org.hzt.utils.iterables.EntryIterable;
import org.hzt.utils.sequences.EntrySequence;
import org.hzt.utils.tuples.Pair;

import java.util.AbstractMap;
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
        return new ImmutableMapX<>();
    }

    static <K, V> MapX<K, V> of(final Map<? extends K, ? extends V> map) {
        return new ImmutableMapX<>(map);
    }

    static <K, V> MapX<K, V> of(final Iterable<Map.Entry<K, V>> entries) {
        return new ImmutableMapX<>(entries);
    }

    static <K, V> MapX<K, V> of(final K k1, final V v1) {
        return MutableMapX.of(k1, v1);
    }

    static <K, V> MapX<K, V> of(final K k1, final V v1, final K k2, final V v2) {
        return MutableMapX.of(k1, v1, k2, v2);
    }

    static <K, V> MapX<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        return MutableMapX.of(k1, v1, k2, v2, k3, v3);
    }

    static <K, V> MapX<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        return MutableMapX.of(k1, v1, k2, v2, k3, v3, k4, v4);
    }

    static <K, V> MapX<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        return MutableMapX.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    static <K, V> MapX<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6) {
        return MutableMapX.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6);
    }

    static <K, V> MapX<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7) {
        return MutableMapX.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7);
    }

    static <K, V> MapX<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5,
                                final K k6, final V v6, final K k7, final V v7, final K k8, final V v8) {
        return MutableMapX.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8);
    }

    static <K, V> MapX<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5,
                                final K k6, final V v6, final K k7, final V v7, final K k8, final V v8, final K k9, final V v9) {
        return MutableMapX.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9);
    }

    static <K, V> MapX<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5,
                                final K k6, final V v6, final K k7, final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10) {
        return MutableMapX.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10);
    }

    @SafeVarargs
    static <K, V> MapX<K, V> ofEntries(final Map.Entry<? extends K, ? extends V>... entries) {
        return new ImmutableMapX<>(entries);
    }

    static <K, V> MapX<K, V> ofPairs(final Iterable<Pair<K, V>> pairs) {
        return new ImmutableMapX<>(EntrySequence.ofPairs(pairs));
    }

    @SafeVarargs
    static <K, V> MapX<K, V> ofPairs(final Pair<K, V>... pairs) {
        return new ImmutableMapX<>(pairs);
    }

    static <K, V> MapX<K, V> build(final Consumer<MutableMapX<K, V>> mapConsumer) {
        final MutableMapX<K, V> map = MutableMapX.empty();
        mapConsumer.accept(map);
        return MapX.copyOf(map);
    }

    static <K, V> Map.Entry<K, V> entry(final K key, final V value) {
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

    <K1, V1> MapX<K1, V1> map(Function<? super K, ? extends K1> keyMapper,
                              Function<? super V, ? extends V1> valueMapper);

    default <R> ListX<R> map(final BiFunction<? super K, ? super V, ? extends R> mapper) {
        return MutableListX.of(this).mapNotNull(e -> mapper.apply(e.getKey(), e.getValue()));
    }

    @Override
    default <K1> MapX<K1, V> mapByKeys(final Function<? super K, ? extends K1> keyMapper) {
        return map(keyMapper, It::self);
    }

    @Override
    default <K1> MapX<K1, V> mapKeys(final BiFunction<? super K, ? super V, ? extends K1> toKeyMapper) {
        return MapX.of(asSequence().mapKeys(toKeyMapper));
    }

    @Override
    default <V1> MapX<K, V1> mapByValues(final Function<? super V, ? extends V1> valueMapper) {
        return map(It::self, valueMapper);
    }

    @Override
    default <V1> MapX<K, V1> mapValues(final BiFunction<? super K, ? super V, ? extends V1> toValueMapper) {
        return MapX.of(asSequence().mapValues(toValueMapper));
    }

    @Override
    default MapX<K, V> filter(final BiPredicate<? super K, ? super V> biPredicate) {
        return asSequence().filter(biPredicate).toMapX();
    }

    default MapX<K, V> filterKeys(final Predicate<? super K> predicate) {
        return asSequence().filterKeys(predicate).toMapX();
    }

    default MapX<K, V> filterValues(final Predicate<? super V> predicate) {
        return asSequence().filterValues(predicate).toMapX();
    }

    @Override
    default MapX<K, V> onEachKey(final Consumer<? super K> consumer) {
        return mapByKeys(k -> {
            consumer.accept(k);
            return k;
        });
    }

    @Override
    default MapX<K, V> onEachValue(final Consumer<? super V> consumer) {
        return mapByValues(v -> {
            consumer.accept(v);
            return v;
        });
    }

    @Override
    default MapX<K, V> onEach(final Consumer<? super Map.Entry<K, V>> consumer) {
        return MapX.of(CollectionX.super.onEach(consumer));
    }

    @Override
    default MapX<K, V> onEach(final BiConsumer<? super K, ? super V> biConsumer) {
        return MapX.of(CollectionX.super.onEach(c -> biConsumer.accept(c.getKey(), c.getValue())));
    }

    default <K1, V1> MapX<K1, V1> inverted(final Function<? super V, ? extends K1> toKeyMapper,
                                           final Function<? super K, ? extends V1> toValueMapper) {
        final Map<K1, V1> resultMap = new HashMap<>();
        for (final Map.Entry<K, V> entry : this) {
            final V value = entry.getValue();
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

    default V getOrDefault(final Object key, final V defaultValue) {
        final V value = get(key);
        return (value != null || containsKey(key)) ? value : defaultValue;
    }

    default void forEach(final BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        for (final Map.Entry<K, V> entry : entrySet()) {
            try {
                final K k = entry.getKey();
                final V v = entry.getValue();
                action.accept(k, v);
            } catch (final IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }
        }
    }

    @Override
    default EntrySequence<K, V> asSequence() {
        return EntrySequence.of(this);
    }

    static <K, V> MapX<K, V> copyOf(final MapX<K, V> map) {
        return new ImmutableMapX<>(map);
    }
}
