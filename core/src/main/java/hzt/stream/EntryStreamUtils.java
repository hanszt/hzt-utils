package hzt.stream;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class EntryStreamUtils {

    private EntryStreamUtils() {
    }

    public static <K, V, R> Function<Map.Entry<K, V>, R> toSingle(BiFunction<? super K, ? super V, ? extends R> entryMapper) {
        Objects.requireNonNull(entryMapper);
        return entry -> entryMapper.apply(entry.getKey(), entry.getValue());
    }

    public static <K, V, K1, V1> Function<Map.Entry<K, V>, Map.Entry<V1, K1>> toInvertedEntry(
            Function<? super K, ? extends K1> keyToValueMapper, Function<? super V, ? extends V1> valueToKeyMapper) {
        Objects.requireNonNull(keyToValueMapper);
        Objects.requireNonNull(valueToKeyMapper);
        return entry -> new AbstractMap.SimpleEntry<>(
                valueToKeyMapper.apply(entry.getValue()),
                keyToValueMapper.apply(entry.getKey()));
    }

    public static <K, V> Function<Map.Entry<K, V>, Map.Entry<V, K>> toInvertedEntry() {
        return toInvertedEntry(Function.identity(), Function.identity());
    }

    public static <K, V, R1, R2> Function<Map.Entry<K, V>, Map.Entry<R1, R2>> toEntry(
            Function<? super K, ? extends R1> keyMapper, Function<? super V, ? extends R2> valueMapper) {
        Objects.requireNonNull(keyMapper);
        Objects.requireNonNull(valueMapper);
        return entry -> new AbstractMap.SimpleEntry<>(
                keyMapper.apply(entry.getKey()),
                valueMapper.apply(entry.getValue()));
    }

    public static <K, V, R> Function<Map.Entry<K, V>, Map.Entry<R, V>> key(Function<? super K, ? extends R> keyMapper) {
        Objects.requireNonNull(keyMapper);
        return entry -> new AbstractMap.SimpleEntry<>(keyMapper.apply(entry.getKey()), entry.getValue());
    }

    public static <K, V, R> Function<Map.Entry<K, V>, Map.Entry<K, R>> value(Function<? super V, ? extends R> valueMapper) {
        Objects.requireNonNull(valueMapper);
        return entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), valueMapper.apply(entry.getValue()));
    }

    public static <K, V> Predicate<Map.Entry<K, V>> byKey(Predicate<? super K> keyPredicate) {
        Objects.requireNonNull(keyPredicate);
        return e -> e != null && e.getKey() != null && keyPredicate.test(e.getKey());
    }

    public static <K, V> Predicate<Map.Entry<K, V>> byValue(Predicate<? super V> valuePredicate) {
        Objects.requireNonNull(valuePredicate);
        return e -> e != null && e.getValue() != null && valuePredicate.test(e.getValue());
    }

    public static <K, V> Predicate<Map.Entry<K, V>> byEntry(BiPredicate<? super K, ? super V> entryPredicate) {
        Objects.requireNonNull(entryPredicate);
        return e -> e != null && entryPredicate.test(e.getKey(), e.getValue());
    }

    public static <K, V> Consumer<Map.Entry<K, V>> entry(BiConsumer<? super K, ? super V> biConsumer) {
        Objects.requireNonNull(biConsumer);
        return entry -> biConsumer.accept(entry.getKey(), entry.getValue());
    }

}
