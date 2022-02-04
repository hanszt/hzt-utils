package hzt.collections;

import hzt.iterables.EntryIterableX;
import hzt.iterables.IterableX;
import hzt.sequences.EntrySequence;
import hzt.tuples.Pair;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public interface MapX<K, V> extends CollectionView<Map.Entry<K, V>>, EntryIterableX<K, V> {

    static <K, V> MapX<K, V> empty() {
        return MutableMapX.empty();
    }

    static <K, V> MapX<K, V> of(Map<K, V> map) {
        return MutableMapX.of(map);
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

    @SafeVarargs
    static <K, V> MapX<K, V> ofPairs(Pair<K, V>... pairs) {
        return new HashMapX<>(pairs);
    }

    static <K, V> MapX<K, V> build(Consumer<MutableMapX<K, V>> mapXConsumer) {
        MutableMapX<K, V> mapX = MutableMapX.empty();
        mapXConsumer.accept(mapX);
        return mapX;
    }

    <K1, V1> MapX<K1, V1> map(@NotNull Function<K, K1> keyMapper, Function<V, V1> valueMapper);

    default <K1, V1> MutableMapX<K1, V1> map(@NotNull BiFunction<K, V, Pair<K1, V1>> biFunction) {
        return asSequence().map(biFunction).toMutableMap();
    }

    <K1> MapX<K1, V> mapKeys(@NotNull Function<K, K1> keyMapper);

    <V1> MapX<K, V1> mapValues(@NotNull Function<V, V1> valueMapper);

    default MapX<K, V> filterByKeys(@NotNull Predicate<K> predicate) {
        return asSequence().filterByKeys(predicate).toMapX();
    }

    default MapX<K, V> filterByValues(@NotNull Predicate<V> predicate) {
        return asSequence().filterByValues(predicate).toMapX();
    }

    default <K1, V1> MapX<K1, V1> toInvertedMapOf(Function<K, V1> toValueMapper, Function<V, K1> toKeyMapper) {
        Map<K1, V1> resultMap = new HashMap<>();
        for (Map.Entry<K, V> entry : this) {
            V value = entry.getValue();
            if (value != null) {
                resultMap.put(toKeyMapper.apply(value), toValueMapper.apply(entry.getKey()));
            }
        }
        return MapX.of(resultMap);
    }

    default MapX<V, K> toInvertedMap() {
        return toInvertedMapOf(It::self, It::self);
    }

    default <R> ListX<R> toListOf(BiFunction<K, V, R> mapper) {
        return MutableListX.of(this).toListXOf(e -> mapper.apply(e.getKey(), e.getValue()));
    }

    default <R> MutableListX<R> valuesToMutableListOf(Function<V, R> mapper) {
        return MutableListX.of(this::valueIterator).toMutableListOf(mapper);
    }

    @NotNull
    default Iterator<V> valueIterator() {
        Iterator<Map.Entry<K, V>> iterator = iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public V next() {
                return iterator.next().getValue();
            }
        };
    }

    default <R> ListX<R> valuesToListXOf(Function<V, R> mapper) {
        return valuesToMutableListOf(mapper);
    }

    default <R> List<R> valuesToListOf(Function<V, R> mapper) {
        return Collections.unmodifiableList(valuesToMutableListOf(mapper));
    }

    default <R> SetX<R> toSetOf(BiFunction<K, V, R> mapper) {
        return MutableListX.of(this).toSetXOf(e -> mapper.apply(e.getKey(), e.getValue()));
    }

    default <R> SetX<R> keysToSetOf(Function<K, R> mapper) {
        return MutableListX.of(this::keyIterator).toSetXOf(mapper);
    }

    @NotNull
    default Iterator<K> keyIterator() {
        Iterator<Map.Entry<K, V>> iterator = iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public K next() {
                return iterator.next().getKey();
            }
        };
    }

    default <R> SetX<R> valuesToSetOf(Function<V, R> mapper) {
        return MutableListX.of(this::valueIterator).toSetXOf(mapper);
    }

    default <R> IterableX<R> keysToIterXOf(Function<K, R> mapper) {
        return keysToSetOf(mapper);
    }

    default <R> IterableX<R> valuesToIterX(Function<V, R> mapper) {
        return valuesToListXOf(mapper);
    }

    default <R, C extends Collection<R>> C valuesTo(Supplier<C> collectionFactory, Function<V, R> mapper) {
        return MutableListX.of(this::valueIterator).mapTo(collectionFactory, mapper);
    }

    default <R, C extends Collection<R>> C flatMapTo(Supplier<C> collectionFactory, Function<Map.Entry<K, V>, C> mapper) {
        C destination = collectionFactory.get();
        for (Map.Entry<K, V> e : this) {
            C collection = mapper.apply(e);
            destination.addAll(collection);
        }
        return destination;
    }

    default <R, C extends Collection<R>> C flatMapValuesTo(Supplier<C> collectionFactory, Function<V, C> mapper) {
        C destination = collectionFactory.get();
        for (V e : (Iterable<V>) this::valueIterator) {
            C collection = mapper.apply(e);
            destination.addAll(collection);
        }
        return destination;
    }

    default <R, C extends Collection<R>> C flatMapKeysTo(Supplier<C> collectionFactory, Function<K, C> mapper) {
        C destination = collectionFactory.get();
        for (K e : (Iterable<K>) this::keyIterator) {
            C collection = mapper.apply(e);
            destination.addAll(collection);
        }
        return destination;
    }

    default <R, C extends Collection<R>> MapX<K, R> flatMapValues(Function<V, C> mapper) {
        Map<K, R> resultMap = new HashMap<>();
        for (Map.Entry<K, V> e : this) {
            C collection = mapper.apply(e.getValue());
            for (R v : collection) {
                resultMap.put(e.getKey(), v);
            }
        }
        return MapX.of(resultMap);
    }

    default <R> MutableListX<R> flatMapKeysToMutableListOf(Function<K, Collection<R>> mapper) {
        return (MutableListX<R>) flatMapKeysTo(MutableListX::empty, mapper);
    }

    default <R> ListX<R> flatMapKeysToListXOf(Function<K, Collection<R>> mapper) {
        return flatMapKeysToMutableListOf(mapper);
    }

    default <R> MutableSetX<R> flatMapKeysToMutableSetOf(Function<K, Collection<R>> mapper) {
        return (MutableSetX<R>) flatMapKeysTo(MutableSetX::empty, mapper);
    }

    default <R> MutableListX<R> flatMapValuesToMutableListOf(Function<V, Collection<R>> mapper) {
        return (MutableListX<R>) flatMapValuesTo(MutableListX::empty, mapper);
    }

    default <R> List<R> flatMapValuesToListOf(Function<V, Collection<R>> mapper) {
        return flatMapValuesToMutableListOf(mapper);
    }

    @Override
    default boolean isNotEmpty() {
        return !isEmpty();
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

    default MutableMapX<K, V> toMutableMap() {
        return MutableMapX.ofEntries(this);
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
