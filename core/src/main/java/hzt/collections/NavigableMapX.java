package hzt.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.function.Function;
import java.util.stream.Stream;

public interface NavigableMapX<K, V> extends NavigableMap<K, V>, MutableMapX<K, V> {

    static <K, V, R extends Comparable<R>> NavigableMapX<K, V> comparingByKey(Function<K, R> selector) {
        return new TreeMapX<>(selector);
    }

    static <K, V> NavigableMapX<K, V> of(NavigableMap<K, V> map) {
        return new TreeMapX<>(map);
    }

    static <K, V, R extends Comparable<R>> NavigableMapX<K, V> of(
            Iterable<Entry<K, V>> iterable, Function<? super K, ? extends R> selector) {
        return new TreeMapX<>(iterable, selector);
    }

    static <K, V, R extends Comparable<R>> NavigableMapX<K, V> ofMap(
            Map<K, V> map, Function<? super K, ? extends R> selector) {
        return new TreeMapX<>(map, selector);
    }

    @SafeVarargs
    static <K, V, R extends Comparable<R>> NavigableMapX<K, V> ofEntries(
            Function<K, R> selector, Map.Entry<K, V> first, Map.Entry<K, V>... others) {
        return new TreeMapX<>(selector, first, others);
    }

    @NotNull
    @Override
    default Iterator<Entry<K, V>> iterator() {
        return iterable().iterator();
    }

    @Override
    default Stream<Entry<K, V>> stream() {
        return MutableMapX.super.stream();
    }

    @Override
    @NotNull
    default Entry<K, V> first() {
        return MutableMapX.super.first();
    }

    @Override
    default Entry<K, V> last() {
        return MutableMapX.super.last();
    }

}
