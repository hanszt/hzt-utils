package org.hzt.utils.iterables;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collections.MutableSetX;
import org.hzt.utils.collections.SetX;
import org.hzt.utils.tuples.Pair;
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

    public static <T> T reduce(final Iterable<T> iterable, final T initial, final BinaryOperator<T> operator) {
        var accumulator = initial;
        for (final var t : iterable) {
            accumulator = operator.apply(accumulator, t);
        }
        return accumulator;
    }

    public static <T> Optional<T> reduce(final Iterator<T> iterator, final BinaryOperator<T> operator) {
        if (iterator.hasNext()) {
            var accumulator = iterator.next();
            while (iterator.hasNext()) {
                accumulator = operator.apply(accumulator, iterator.next());
            }
            return Optional.of(accumulator);
        }
        return Optional.empty();
    }

    public static <T, R, K> MapX<K, MutableListX<R>> groupMapping(
            @NotNull final Iterable<T> iterable,
            @NotNull final Function<? super T, ? extends K> classifier,
            @NotNull final Function<? super T, ? extends R> valueMapper) {
        final MutableMapX<K, MutableListX<R>> groupedMap = MutableMapX.empty();
        for (final var t : iterable) {
            groupedMap.computeIfAbsent(classifier.apply(t), key -> MutableListX.empty()).add(valueMapper.apply(t));
        }
        return groupedMap;
    }

    public static <T, R> Pair<ListX<R>, ListX<R>> partitionMapping(
            @NotNull final Iterable<T> iterable,
            @NotNull final Predicate<T> predicate,
            @NotNull final Function<? super T, ? extends R> resultMapper) {
        final MutableListX<R> matchingList = MutableListX.empty();
        final MutableListX<R> nonMatchingList = MutableListX.empty();
        for (final var t : iterable) {
            if (t != null) {
                final var r = resultMapper.apply(t);
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
            @NotNull final Iterable<T> iterable,
            @NotNull final Function<? super T, ? extends I> toCollectionMapper,
            @NotNull final Function<? super S, ? extends R> selector) {
        final MutableSetX<R> common = MutableSetX.empty();
        for (final var t : iterable) {
            final var otherIterable = toCollectionMapper.apply(t);
            final MutableListX<R> resultList = MutableListX.empty();
            for (final var s : otherIterable) {
                final var r = selector.apply(s);
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

    public static <T> boolean any(@NotNull final Iterable<T> iterable, @NotNull final Predicate<T> predicate) {
        for (final var element : iterable) {
            if (predicate.test(element)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean all(@NotNull final Iterable<T> iterable, @NotNull final Predicate<T> predicate) {
        for (final var t : iterable) {
            if (!predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean none(@NotNull final Iterable<T> iterable, @NotNull final Predicate<T> predicate) {
        for (final var t : iterable) {
            if (predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

    public static <T> Optional<T> findLastOf(final Iterable<T> iterable) {
        final var iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        } else if (iterable instanceof final List<T> list) {
            return Optional.ofNullable(list.get(list.size() - 1));
        } else {
            var result = iterator.next();
            while (iterator.hasNext()) {
                result = iterator.next();
            }
            return Optional.ofNullable(result);
        }
    }

    public static <T> Optional<T> findLast(final Iterable<T> iterable, @NotNull final Predicate<T> predicate) {
        final var iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            throw IterableXHelper.noValuePresentException();
        } else if (iterable instanceof final List<T> list) {
            return IterableXHelper.findLastIfInstanceOfList(predicate, list);
        } else {
            return IterableXHelper.findLastIfUnknownIterable(predicate, iterator);
        }
    }
}
