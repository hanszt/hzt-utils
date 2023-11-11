package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.function.primitives.IntBiFunction;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

@FunctionalInterface
public interface IntReducable extends PrimitiveIterable.OfInt, PrimitiveReducable<Integer, IntBinaryOperator, IntPredicate, OptionalInt> {

    default int reduce(final int initial, final IntBinaryOperator operator) {
        var accumulator = initial;
        final var iterator = this.iterator();
        while (iterator.hasNext()) {
            accumulator = operator.applyAsInt(accumulator, iterator.nextInt());
        }
        return accumulator;
    }

    default <R> R reduceToTwo(
            final int initial1, final IntBinaryOperator operator1,
            final int initial2, final IntBinaryOperator operator2,
            final IntBiFunction<R> finisher) {
        final var iterator = iterator();
        var accumulator1 = initial1;
        var accumulator2 = initial2;
        while (iterator.hasNext()) {
            final var nextInt = iterator.nextInt();
            accumulator1 = operator1.applyAsInt(accumulator1, nextInt);
            accumulator2 = operator2.applyAsInt(accumulator2, nextInt);
        }
        return finisher.apply(accumulator1, accumulator2);
    }

    default <R> Optional<R> reduceToTwo(
            final IntBinaryOperator operator1,
            final IntBinaryOperator operator2,
            final IntBiFunction<R> finisher) {
        final var iterator = iterator();
        if (iterator.hasNext()) {
            final var first = iterator.nextInt();
            var accumulator1 = first;
            var accumulator2 = first;
            while (iterator.hasNext()) {
                final var nextInt = iterator.nextInt();
                accumulator1 = operator1.applyAsInt(accumulator1, nextInt);
                accumulator2 = operator2.applyAsInt(accumulator2, nextInt);
            }
            return Optional.of(finisher.apply(accumulator1, accumulator2));
        }
        return Optional.empty();
    }

    default OptionalInt reduce(final IntBinaryOperator operator) {
        final var iterator = iterator();
        if (iterator.hasNext()) {
            var accumulator = iterator.nextInt();
            while (iterator.hasNext()) {
                accumulator = operator.applyAsInt(accumulator, iterator.nextInt());
            }
            return OptionalInt.of(accumulator);
        }
        return OptionalInt.empty();
    }

    default <R> R reduce(final int initial,
                         final IntFunction<R> mapper,
                         final IntBinaryOperator operation) {
        return mapper.apply(reduce(initial, operation));
    }

    default int first() {
        return findFirst().orElseThrow();
    }

    default int first(final IntPredicate predicate) {
        final var iterator = this.iterator();
        while (iterator.hasNext()) {
            final var next = iterator.nextInt();
            if (predicate.test(next)) {
                return next;
            }
        }
        throw new NoSuchElementException();
    }

    default int firstNot(final IntPredicate predicate) {
        return first(predicate.negate());
    }

    default OptionalInt findFirst() {
        final var iterator = iterator();
        return iterator.hasNext() ? OptionalInt.of(iterator.nextInt()) : OptionalInt.empty();
    }

    default OptionalInt findFirst(final IntPredicate predicate) {
        final var iterator = this.iterator();
        while (iterator.hasNext()) {
            final var next = iterator.nextInt();
            if (predicate.test(next)) {
                return OptionalInt.of(next);
            }
        }
        return OptionalInt.empty();
    }

    default int last() {
        return findLast().orElseThrow();
    }

    default int last(final IntPredicate predicate) {
        return findLast(predicate).orElseThrow();
    }

    default OptionalInt findLast() {
        return findLast(It::noIntFilter);
    }

    default OptionalInt findLast(final IntPredicate predicate) {
        final var iterator = iterator();
        var result = iterator.nextInt();
        if (!predicate.test(result) && !iterator.hasNext()) {
            return OptionalInt.empty();
        }
        while (iterator.hasNext()) {
            final var next = iterator.nextInt();
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
        @SuppressWarnings("squid:S1941") final var single = iterator.nextInt();
        if (iterator.hasNext()) {
            throw new IllegalArgumentException("Sequence has more than one element");
        }
        return single;
    }

    default boolean any(final IntPredicate predicate) {
        final var iterator = this.iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextInt())) {
                return true;
            }
        }
        return false;
    }

    default boolean all(final IntPredicate predicate) {
        final var iterator = iterator();
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.nextInt())) {
                return false;
            }
        }
        return true;
    }

    default boolean none(final IntPredicate predicate) {
        final var iterator = this.iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextInt())) {
                return false;
            }
        }
        return true;
    }
}
