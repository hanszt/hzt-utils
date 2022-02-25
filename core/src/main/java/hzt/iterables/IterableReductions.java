package hzt.iterables;

import hzt.collections.ListX;
import hzt.collections.MapX;
import hzt.collections.MutableListX;
import hzt.collections.MutableMapX;
import hzt.collections.MutableSetX;
import hzt.collections.SetX;
import hzt.tuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public final class IterableReductions {

    private IterableReductions() {
    }

    public static <T> T reduce(Iterable<T> iterable, T initial, BinaryOperator<T> operator) {
        T accumulator = initial;
        for (T t : iterable) {
            accumulator = operator.apply(accumulator, t);
        }
        return accumulator;
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
        MutableListX<R> matchingList = MutableListX.empty();
        MutableListX<R> nonMatchingList = MutableListX.empty();
        for (T t : iterable) {
            if (t != null) {
                R r = resultMapper.apply(t);
                if (predicate.test(t)) {
                    matchingList.add(r);
                } else {
                    nonMatchingList.add(r);
                }
            }
        }
        return Pair.of(matchingList, nonMatchingList);
    }

    public static <T, S, I extends Iterable<S>, R> SetX<R> intersectionOf(
            @NotNull Iterable<T> iterable,
            @NotNull Function<? super T, ? extends I> toCollectionMapper,
            @NotNull Function<? super S, ? extends R> selector) {
        MutableSetX<R> common = MutableSetX.empty();
        for (T t : iterable) {
            final I otherIterable = toCollectionMapper.apply(t);
            final MutableListX<R> resultList = MutableListX.empty();
            for (S s : otherIterable) {
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

    public static <T> Optional<T> findLastOf(Iterable<T> iterable) {
        final Iterator<T> iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        } else if (iterable instanceof List) {
            List<T> list = (List<T>) iterable;
            return Optional.ofNullable(list.get(list.size() - 1));
        } else {
            T result = iterator.next();
            while (iterator.hasNext()) {
                result = iterator.next();
            }
            return Optional.ofNullable(result);
        }
    }

    public static <T> Optional<T> findLast(Iterable<T> iterable, @NotNull Predicate<T> predicate) {
        final Iterator<T> iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            throw IterableXHelper.noValuePresentException();
        } else if (iterable instanceof List) {
            return IterableXHelper.findLastIfInstanceOfList(predicate, ((List<T>) iterable));
        } else {
            return IterableXHelper.findLastIfUnknownIterable(predicate, iterator);
        }
    }
}
