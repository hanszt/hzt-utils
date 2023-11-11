package org.hzt.utils.streams;

import org.hzt.utils.It;
import org.hzt.utils.iterables.EntryIterable;
import org.hzt.utils.sequences.EntrySequence;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@FunctionalInterface
public interface EntryStreamX<K, V> extends EntryIterable<K, V>, StreamX<Map.Entry<K, V>> {

    static <K, V> EntryStreamX<K, V> ofStream(final Stream<Map.Entry<K, V>> stream) {
        return new EntryStreamXImpl<>(stream);
    }

    static <K, V> EntryStreamX<K, V> of(final EntryIterable<K, V> iterable) {
        return iterable::spliterator;
    }

    static <K, V> EntryStreamX<K, V> ofMap(final Map<K, V> map) {
        return () -> map.entrySet().spliterator();
    }
    
    @Override
    default <R> StreamX<R> map(final BiFunction<? super K, ? super V, ? extends R> biFunction) {
        return map(e -> biFunction.apply(e.getKey(), e.getValue()));
    }

    @Override
    default EntrySequence<K, V> asSequence() {
        return this::iterator;
    }

    @Override
    default <K1, V1> EntryStreamX<K1, V1> map(final Function<? super K, ? extends K1> keyMapper,
                                              final Function<? super V, ? extends V1> valueMapper) {
        return ofStream(map(e -> Map.entry(keyMapper.apply(e.getKey()), valueMapper.apply(e.getValue()))));
    }

    @Override
    default <K1, V1> EntryStreamX<K1, V1> inverted(final Function<? super V, ? extends K1> toKeyMapper,
                                                   final Function<? super K, ? extends V1> toValueMapper) {
        return ofStream(map((k, v) -> Map.entry(toKeyMapper.apply(v), toValueMapper.apply(k))));
    }

    @Override
    default EntryStreamX<V, K> inverted() {
        return inverted(It::self, It::self);
    }

    @Override
    default EntryStreamX<K, V> parallel() {
        return EntryStreamX.ofStream(StreamX.super.parallel());
    }

    @Override
    default EntryStreamX<K, V> isParallel(final Consumer<Boolean> resultSupplier) {
        return peek(s -> resultSupplier.accept(isParallel()));
    }

    @Override
    default EntryStreamX<K, V> sequential() {
        return EntryStreamX.ofStream(StreamX.super.sequential());
    }

    @Override
    default <K1> EntryStreamX<K1, V> mapByKeys(final Function<? super K, ? extends K1> keyMapper) {
        return ofStream(map((k , v) -> Map.entry(keyMapper.apply(k), v)));
    }

    @Override
    default <K1> EntryStreamX<K1, V> mapKeys(final BiFunction<? super K, ? super V, ? extends K1> toKeyMapper) {
        return ofStream(map((k, v) -> Map.entry(toKeyMapper.apply(k, v), v)));
    }

    @Override
    default <V1> EntryStreamX<K, V1> mapByValues(final Function<? super V, ? extends V1> valueMapper) {
        return ofStream(map((k, v) -> Map.entry(k, valueMapper.apply(v))));
    }

    @Override
    default <V1> EntryStreamX<K, V1> mapValues(final BiFunction<? super K, ? super V, ? extends V1> toValueMapper) {
        return ofStream(map((k, v) -> Map.entry(k, toValueMapper.apply(k, v))));
    }

    @Override
    default EntryStreamX<K, V> filter(final BiPredicate<? super K, ? super V> biPredicate) {
        return ofStream(filter(e -> biPredicate.test(e.getKey(), e.getValue())));
    }

    @Override
    default EntryStreamX<K, V> filterKeys(final Predicate<? super K> predicate) {
        return ofStream(filter(e -> predicate.test(e.getKey())));
    }

    @Override
    default EntryStreamX<K, V> filterValues(final Predicate<? super V> predicate) {
        return ofStream(filter(e -> predicate.test(e.getValue())));
    }

    @Override
    default EntryStreamX<K, V> onEachKey(final Consumer<? super K> consumer) {
        return ofStream(peek(e -> consumer.accept(e.getKey())));
    }

    @Override
    default EntryStreamX<K, V> onEachValue(final Consumer<? super V> consumer) {
        return ofStream(peek(e -> consumer.accept(e.getValue())));
    }

    @Override
    default EntryStreamX<K, V> onEach(final BiConsumer<? super K, ? super V> biConsumer) {
        return ofStream(peek(e -> biConsumer.accept(e.getKey(), e.getValue())));
    }

    @Override
    @SuppressWarnings("squid:S3864")
    default EntryStreamX<K, V> peek(final Consumer<? super Map.Entry<K, V>> action) {
        return EntryStreamX.ofStream(StreamX.super.peek(action));
    }

    @Override
    default void forEach(final BiConsumer<? super K, ? super V> biConsumer) {
        forEach(e -> biConsumer.accept(e.getKey(), e.getValue()));
    }
    
}
