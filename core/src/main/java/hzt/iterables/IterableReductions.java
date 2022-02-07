package hzt.iterables;

import hzt.collections.ListX;
import hzt.collections.MapX;
import hzt.collections.MutableListX;
import hzt.collections.MutableMapX;
import hzt.collections.MutableSetX;
import hzt.collections.SetX;
import hzt.tuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

public final class IterableReductions {

    private IterableReductions() {
    }

    public static <T> T reduce(Iterable<T> iterable, T initial, BinaryOperator<T> operator) {
        T accumulator = initial;
        for (T t : iterable) {
            accumulator = operator.apply(accumulator, t);
        }
        return initial;
    }

    public static <T> Optional<T> reduce(Iterator<T> iterator, BinaryOperator<T> operator) {
        if (iterator.hasNext()) {
            T accumulator = iterator.next();
            while (iterator.hasNext()) {
                accumulator = operator.apply(accumulator, iterator.next());
            }
            return Optional.of(accumulator);
        }
        return Optional.empty();
    }

    public static <T, A, R> R collect(Iterable<T> iterable, Collector<T, A, R> collector) {
        A result = collector.supplier().get();
        final BiConsumer<A, T> accumulator = collector.accumulator();
        iterable.forEach(t -> accumulator.accept(result, t));
        return collector.finisher().apply(result);
    }

    public static <T, R, K> MapX<K, MutableListX<R>> groupMapping(
            @NotNull Iterable<T> iterable,
            @NotNull Function<? super T, ? extends K> classifier,
            @NotNull Function<? super T, ? extends R> valueMapper) {
        MutableMapX<K, MutableListX<R>> groupedMap = MutableMapX.empty();
        for (T t : iterable) {
            groupedMap.computeIfAbsent(classifier.apply(t), key -> MutableListX.empty()).add(valueMapper.apply(t));
        }
        return groupedMap;
    }

    public static <T, R> Pair<ListX<R>, ListX<R>> partitionMapping(
            @NotNull Iterable<T> iterable,
            @NotNull Predicate<T> predicate,
            @NotNull Function<? super T, ? extends R> resultMapper) {
        Pair<MutableListX<R>, MutableListX<R>> pair = Pair.of(MutableListX.empty(), MutableListX.empty());
        for (T t : iterable) {
            if (t != null) {
                R r = resultMapper.apply(t);
                if (predicate.test(t)) {
                    pair.first().add(r);
                } else {
                    pair.second().add(r);
                }
            }
        }
        return Pair.of(pair.first().toListX(), pair.second().toListX());
    }

    public static <T, S, C extends Collection<S>, R> SetX<R> intersectionOf(
            @NotNull Iterable<T> iterable,
            @NotNull Function<? super T, ? extends C> toCollectionMapper,
            @NotNull Function<? super S, ? extends R> selector) {
        MutableSetX<R> common = MutableSetX.empty();
        for (T t : iterable) {
            final C collection = toCollectionMapper.apply(t);
            final MutableListX<R> resultList = MutableListX.empty();
            for (S s : collection) {
                final R r = selector.apply(s);
                resultList.add(r);
            }
            if (common.isEmpty()) {
                common.addAll(resultList);
            } else {
                common.retainAll(resultList);
            }
        }
        return common;
    }

    public static <T> boolean any(@NotNull Iterable<T> iterable, @NotNull Predicate<T> predicate) {
        for (T element : iterable) {
            if (predicate.test(element)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean all(@NotNull Iterable<T> iterable, @NotNull Predicate<T> predicate) {
        for (T t : iterable) {
            if (!predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean none(@NotNull Iterable<T> iterable, @NotNull Predicate<T> predicate) {
        for (T t : iterable) {
            if (predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

    public static <T, R> Optional<R> findLastOf(Iterable<T> iterable, @NotNull Function<T, R> mapper) {
        final Iterator<T> iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        } else if (iterable instanceof List<T> list) {
            return Optional.ofNullable(list.get(list.size() - 1)).map(mapper);
        } else {
            T result = iterator.next();
            while (iterator.hasNext()) {
                result = iterator.next();
            }
            return Optional.ofNullable(result).map(mapper);
        }
    }

    public static <T> Optional<T> findLast(Iterable<T> iterable, @NotNull Predicate<T> predicate) {
        final Iterator<T> iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            throw IterableXHelper.noValuePresentException();
        } else if (iterable instanceof List<T> list) {
            return IterableXHelper.findLastIfInstanceOfList(predicate, list);
        } else {
            return IterableXHelper.findLastIfUnknownIterable(predicate, iterator);
        }
    }
}
