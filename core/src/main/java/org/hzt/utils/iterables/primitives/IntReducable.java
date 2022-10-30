package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.function.primitives.IntBiFunction;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

@FunctionalInterface
public interface IntReducable extends PrimitiveIterable.OfInt, PrimitiveReducable<Integer, IntBinaryOperator, IntPredicate, OptionalInt> {

    default int reduce(int initial, IntBinaryOperator operator) {
        var accumulator = initial;
        var iterator = this.iterator();
        while (iterator.hasNext()) {
            accumulator = operator.applyAsInt(accumulator, iterator.nextInt());
        }
        return accumulator;
    }

    default <R> R reduceToTwo(
            int initial1, @NotNull IntBinaryOperator operator1,
            int initial2, @NotNull IntBinaryOperator operator2,
            @NotNull IntBiFunction<R> finisher) {
        var iterator = iterator();
        var accumulator1 = initial1;
        var accumulator2 = initial2;
        while (iterator.hasNext()) {
            var nextInt = iterator.nextInt();
            accumulator1 = operator1.applyAsInt(accumulator1, nextInt);
            accumulator2 = operator2.applyAsInt(accumulator2, nextInt);
        }
        return finisher.apply(accumulator1, accumulator2);
    }

    default <R> @NotNull Optional<R> reduceToTwo(
            @NotNull IntBinaryOperator operator1,
            @NotNull IntBinaryOperator operator2,
            @NotNull IntBiFunction<R> finisher) {
        var iterator = iterator();
        if (iterator.hasNext()) {
            final var first = iterator.nextInt();
            var accumulator1 = first;
            var accumulator2 = first;
            while (iterator.hasNext()) {
                var nextInt = iterator.nextInt();
                accumulator1 = operator1.applyAsInt(accumulator1, nextInt);
                accumulator2 = operator2.applyAsInt(accumulator2, nextInt);
            }
            return Optional.of(finisher.apply(accumulator1, accumulator2));
        }
        return Optional.empty();
    }

    default @NotNull OptionalInt reduce(@NotNull IntBinaryOperator operator) {
        var iterator = iterator();
        if (iterator.hasNext()) {
            var accumulator = iterator.nextInt();
            while (iterator.hasNext()) {
                accumulator = operator.applyAsInt(accumulator, iterator.nextInt());
            }
            return OptionalInt.of(accumulator);
        }
        return OptionalInt.empty();
    }

    default <R> @NotNull R reduce(int initial,
                                  @NotNull IntFunction<R> mapper,
                                  @NotNull IntBinaryOperator operation) {
        return mapper.apply(reduce(initial, operation));
    }

    default int first() {
        return findFirst().orElseThrow();
    }

    default int first(@NotNull IntPredicate predicate) {
        var iterator = this.iterator();
        while (iterator.hasNext()) {
            var next = iterator.nextInt();
            if (predicate.test(next)) {
                return next;
            }
        }
        throw new NoSuchElementException();
    }

    default int firstNot(@NotNull IntPredicate predicate) {
        return first(predicate.negate());
    }

    default @NotNull OptionalInt findFirst() {
        final var iterator = iterator();
        return iterator.hasNext() ? OptionalInt.of(iterator.nextInt()) : OptionalInt.empty();
    }

    default @NotNull OptionalInt findFirst(@NotNull IntPredicate predicate) {
        var iterator = this.iterator();
        while (iterator.hasNext()) {
            var next = iterator.nextInt();
            if (predicate.test(next)) {
                return OptionalInt.of(next);
            }
        }
        return OptionalInt.empty();
    }

    default int last() {
        return findLast().orElseThrow();
    }

    default int last(@NotNull IntPredicate predicate) {
        return findLast(predicate).orElseThrow();
    }

    default @NotNull OptionalInt findLast() {
        return findLast(It::noIntFilter);
    }

    default @NotNull OptionalInt findLast(@NotNull IntPredicate predicate) {
        var iterator = iterator();
        var result = iterator.nextInt();
        if (!predicate.test(result) && !iterator.hasNext()) {
            return OptionalInt.empty();
        }
        while (iterator.hasNext()) {
            var next = iterator.nextInt();
            if (predicate.test(next)) {
                result = next;
            }
        }
        return OptionalInt.of(result);
    }

    default int single() {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchElementException("Sequence is empty");
        }
        @SuppressWarnings("squid:S1941")
        var single = iterator.nextInt();
        if (iterator.hasNext()) {
            throw new IllegalArgumentException("Sequence has more than one element");
        }
        return single;
    }

    default boolean any(@NotNull IntPredicate predicate) {
        var iterator = this.iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextInt())) {
                return true;
            }
        }
        return false;
    }

    default boolean all(@NotNull IntPredicate predicate) {
        var iterator = iterator();
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.nextInt())) {
                return false;
            }
        }
        return true;
    }

    default boolean none(@NotNull IntPredicate predicate) {
        var iterator = this.iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextInt())) {
                return false;
            }
        }
        return true;
    }
}
