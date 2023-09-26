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

    default long reduce(final long initial, final LongBinaryOperator operator) {
        final var iterator = iterator();
        var accumulator = initial;
        while (iterator.hasNext()) {
            accumulator = operator.applyAsLong(accumulator, iterator.nextLong());
        }
        return accumulator;
    }

    default @NotNull OptionalLong reduce(@NotNull final LongBinaryOperator operator) {
        final var iterator = iterator();
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
            final long initial1, @NotNull final LongBinaryOperator operator1,
            final long initial2, @NotNull final LongBinaryOperator operator2,
            @NotNull final LongBiFunction<R> finisher) {
        final var iterator = iterator();
        var accumulator1 = initial1;
        var accumulator2 = initial2;
        while (iterator.hasNext()) {
            final var nextLong = iterator.nextLong();
            accumulator1 = operator1.applyAsLong(accumulator1, nextLong);
            accumulator2 = operator2.applyAsLong(accumulator2, nextLong);
        }
        return finisher.apply(accumulator1, accumulator2);
    }

    default @NotNull <R> Optional<R> reduceToTwo(@NotNull final LongBinaryOperator operator1,
                                                 @NotNull final LongBinaryOperator operator2,
                                                 @NotNull final LongBiFunction<R> finisher) {
        final var iterator = iterator();
        if (iterator.hasNext()) {
            final var first = iterator.nextLong();
            var accumulator1 = first;
            var accumulator2 = first;
            while (iterator.hasNext()) {
                final var nextLong = iterator.nextLong();
                accumulator1 = operator1.applyAsLong(accumulator1, nextLong);
                accumulator2 = operator2.applyAsLong(accumulator2, nextLong);
            }
            return Optional.of(finisher.apply(accumulator1, accumulator2));
        }
        return Optional.empty();
    }

    default <R> @NotNull R reduce(final long initial,
                                  @NotNull final LongFunction<R> mapper,
                                  @NotNull final LongBinaryOperator operation) {
        return mapper.apply(reduce(initial, operation));
    }

    default long first() {
        return findFirst().orElseThrow();
    }

    default long first(@NotNull final LongPredicate predicate) {
        return findFirst(predicate).orElseThrow();
    }

    default long firstNot(@NotNull final LongPredicate predicate) {
        return first(predicate.negate());
    }

    default @NotNull OptionalLong findFirst() {
        final var iterator = iterator();
        return iterator.hasNext() ? OptionalLong.of(iterator.nextLong()) : OptionalLong.empty();
    }

    default @NotNull OptionalLong findFirst(@NotNull final LongPredicate predicate) {
        final var iterator = this.iterator();
        while (iterator.hasNext()) {
            final var next = iterator.nextLong();
            if (predicate.test(next)) {
                return OptionalLong.of(next);
            }
        }
        return OptionalLong.empty();
    }

    default long last() {
        return findLast().orElseThrow();
    }

    default long last(@NotNull final LongPredicate predicate) {
        return findLast(predicate).orElseThrow();
    }

    default @NotNull OptionalLong findLast() {
        return findLast(It::noLongFilter);
    }

    default @NotNull OptionalLong findLast(@NotNull final LongPredicate predicate) {
        final var iterator = iterator();
        var result = iterator.nextLong();
        if (!predicate.test(result) && !iterator.hasNext()) {
            return OptionalLong.empty();
        }
        while (iterator.hasNext()) {
            final var next = iterator.nextLong();
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
        @SuppressWarnings("squid:S1941") final var single = iterator.nextLong();
        if (iterator.hasNext()) {
            throw new IllegalArgumentException("Sequence has more than one element");
        }
        return single;
    }

    default boolean any(@NotNull final LongPredicate predicate) {
        final var iterator = iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextLong())) {
                return true;
            }
        }
        return false;
    }

    default boolean all(@NotNull final LongPredicate predicate) {
        final var iterator = iterator();
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.nextLong())) {
                return false;
            }
        }
        return true;
    }

    default boolean none(@NotNull final LongPredicate predicate) {
        final var iterator = iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextLong())) {
                return false;
            }
        }
        return true;
    }
}
