package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.function.primitives.LongBiFunction;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.LongBinaryOperator;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;

@FunctionalInterface
public interface LongReducable extends PrimitiveIterable.OfLong, PrimitiveReducable<Long, LongBinaryOperator, LongPredicate, OptionalLong> {

    default long reduce(long initial, LongBinaryOperator operator) {
        var iterator = iterator();
        var accumulator = initial;
        while (iterator.hasNext()) {
            accumulator = operator.applyAsLong(accumulator, iterator.nextLong());
        }
        return accumulator;
    }

    default @NotNull OptionalLong reduce(@NotNull LongBinaryOperator operator) {
        var iterator = iterator();
        if (iterator.hasNext()) {
            var accumulator = iterator.nextLong();
            while (iterator.hasNext()) {
                accumulator = operator.applyAsLong(accumulator, iterator.nextLong());
            }
            return OptionalLong.of(accumulator);
        }
        return OptionalLong.empty();
    }

    default <R> R reduceToTwo(
            long initial1, @NotNull LongBinaryOperator operator1,
            long initial2, @NotNull LongBinaryOperator operator2,
            @NotNull LongBiFunction<R> finisher) {
        var iterator = iterator();
        var accumulator1 = initial1;
        var accumulator2 = initial2;
        while (iterator.hasNext()) {
            var nextLong = iterator.nextLong();
            accumulator1 = operator1.applyAsLong(accumulator1, nextLong);
            accumulator2 = operator2.applyAsLong(accumulator2, nextLong);
        }
        return finisher.apply(accumulator1, accumulator2);
    }

    default @NotNull <R> Optional<R> reduceToTwo(@NotNull LongBinaryOperator operator1,
                                                 @NotNull LongBinaryOperator operator2,
                                                 @NotNull LongBiFunction<R> finisher) {
        var iterator = iterator();
        if (iterator.hasNext()) {
            final var first = iterator.nextLong();
            var accumulator1 = first;
            var accumulator2 = first;
            while (iterator.hasNext()) {
                var nextLong = iterator.nextLong();
                accumulator1 = operator1.applyAsLong(accumulator1, nextLong);
                accumulator2 = operator2.applyAsLong(accumulator2, nextLong);
            }
            return Optional.of(finisher.apply(accumulator1, accumulator2));
        }
        return Optional.empty();
    }

    default <R> @NotNull R reduce(long initial,
                                  @NotNull LongFunction<R> mapper,
                                  @NotNull LongBinaryOperator operation) {
        return mapper.apply(reduce(initial, operation));
    }

    default long first() {
        return findFirst().orElseThrow();
    }

    default long first(@NotNull LongPredicate predicate) {
        return findFirst(predicate).orElseThrow();
    }

    default long firstNot(@NotNull LongPredicate predicate) {
        return first(predicate.negate());
    }

    default @NotNull OptionalLong findFirst() {
        final var iterator = iterator();
        return iterator.hasNext() ? OptionalLong.of(iterator.nextLong()) : OptionalLong.empty();
    }

    default @NotNull OptionalLong findFirst(@NotNull LongPredicate predicate) {
        var iterator = this.iterator();
        while (iterator.hasNext()) {
            var next = iterator.nextLong();
            if (predicate.test(next)) {
                return OptionalLong.of(next);
            }
        }
        return OptionalLong.empty();
    }

    default long last() {
        return findLast().orElseThrow();
    }

    default long last(@NotNull LongPredicate predicate) {
        return findLast(predicate).orElseThrow();
    }

    default @NotNull OptionalLong findLast() {
        return findLast(It::noLongFilter);
    }

    default @NotNull OptionalLong findLast(@NotNull LongPredicate predicate) {
        var iterator = iterator();
        var result = iterator.nextLong();
        if (!predicate.test(result) && !iterator.hasNext()) {
            return OptionalLong.empty();
        }
        while (iterator.hasNext()) {
            var next = iterator.nextLong();
            if (predicate.test(next)) {
                result = next;
            }
        }
        return OptionalLong.of(result);
    }

    default long single() {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchElementException("Sequence is empty");
        }
        @SuppressWarnings("squid:S1941")
        var single = iterator.nextLong();
        if (iterator.hasNext()) {
            throw new IllegalArgumentException("Sequence has more than one element");
        }
        return single;
    }

    default boolean any(@NotNull LongPredicate predicate) {
        var iterator = iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextLong())) {
                return true;
            }
        }
        return false;
    }

    default boolean all(@NotNull LongPredicate predicate) {
        var iterator = iterator();
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.nextLong())) {
                return false;
            }
        }
        return true;
    }

    default boolean none(@NotNull LongPredicate predicate) {
        var iterator = iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextLong())) {
                return false;
            }
        }
        return true;
    }
}
