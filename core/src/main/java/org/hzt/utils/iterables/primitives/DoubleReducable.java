package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.function.primitives.DoubleBiFunction;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;

@FunctionalInterface
public interface DoubleReducable extends PrimitiveIterable.OfDouble,
        PrimitiveReducable<Double, DoubleBinaryOperator, DoublePredicate, OptionalDouble> {

    default double reduce(double initial, DoubleBinaryOperator operator) {
        var accumulator = initial;
        var iterator = iterator();
        while (iterator.hasNext()) {
            accumulator = operator.applyAsDouble(accumulator, iterator.nextDouble());
        }
        return accumulator;
    }

    @Override
    @NotNull
    default OptionalDouble reduce(@NotNull DoubleBinaryOperator operator) {
        var iterator = iterator();
        if (iterator.hasNext()) {
            var accumulator = iterator.nextDouble();
            while (iterator.hasNext()) {
                accumulator = operator.applyAsDouble(accumulator, iterator.nextDouble());
            }
            return OptionalDouble.of(accumulator);
        }
        return OptionalDouble.empty();
    }

    default <R> R reduceToTwo(
            double initial1, @NotNull DoubleBinaryOperator operator1,
            double initial2, @NotNull DoubleBinaryOperator operator2,
            @NotNull DoubleBiFunction<R> finisher) {
        var iterator = iterator();
        var accumulator1 = initial1;
        var accumulator2 = initial2;
        while (iterator.hasNext()) {
            var nextDouble = iterator.nextDouble();
            accumulator1 = operator1.applyAsDouble(accumulator1, nextDouble);
            accumulator2 = operator2.applyAsDouble(accumulator2, nextDouble);
        }
        return finisher.apply(accumulator1, accumulator2);
    }

    default @NotNull <R> Optional<R> reduceToTwo(@NotNull DoubleBinaryOperator operator1,
                                                 @NotNull DoubleBinaryOperator operator2,
                                                 @NotNull DoubleBiFunction<R> finisher) {
        var iterator = iterator();
        if (iterator.hasNext()) {
            final var first = iterator.nextDouble();
            var accumulator1 = first;
            var accumulator2 = first;
            while (iterator.hasNext()) {
                var nextDouble = iterator.nextDouble();
                accumulator1 = operator1.applyAsDouble(accumulator1, nextDouble);
                accumulator2 = operator2.applyAsDouble(accumulator2, nextDouble);
            }
            return Optional.of(finisher.apply(accumulator1, accumulator2));
        }
        return Optional.empty();
    }

    default <R> @NotNull R reduce(double initial,
                                  @NotNull DoubleFunction<R> mapper,
                                  @NotNull DoubleBinaryOperator operation) {
        return mapper.apply(reduce(initial, operation));
    }

    default double first() {
        return findFirst().orElseThrow();
    }

    default double first(@NotNull DoublePredicate predicate) {
        return findFirst(predicate).orElseThrow();
    }

    @Override
    default OptionalDouble findFirst() {
        final var iterator = iterator();
        return iterator.hasNext() ? OptionalDouble.of(iterator.nextDouble()) : OptionalDouble.empty();
    }

    @Override
    default OptionalDouble findFirst(DoublePredicate predicate) {
        var iterator = this.iterator();
        while (iterator.hasNext()) {
            var next = iterator.nextDouble();
            if (predicate.test(next)) {
                return OptionalDouble.of(next);
            }
        }
        return OptionalDouble.empty();
    }

    default double last() {
        return findLast().orElseThrow();
    }

    default double last(@NotNull DoublePredicate predicate) {
        return findLast(predicate).orElseThrow();
    }

    @Override
    default OptionalDouble findLast() {
        return findLast(It::noDoubleFilter);
    }

    @Override
    default OptionalDouble findLast(DoublePredicate predicate) {
        var iterator = iterator();
        var result = iterator.nextDouble();
        if (!predicate.test(result) && !iterator.hasNext()) {
            return OptionalDouble.empty();
        }
        while (iterator.hasNext()) {
            var next = iterator.nextDouble();
            if (predicate.test(next)) {
                result = next;
            }
        }
        return OptionalDouble.of(result);
    }

    default double single() {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchElementException("Sequence is empty");
        }
        @SuppressWarnings("squid:S1941")
        var single = iterator.nextDouble();
        if (iterator.hasNext()) {
            throw new IllegalArgumentException("Sequence has more than one element");
        }
        return single;
    }

    @Override
    default boolean any(@NotNull DoublePredicate predicate) {
        var iterator = iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextDouble())) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean all(@NotNull DoublePredicate predicate) {
        var iterator = iterator();
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.nextDouble())) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean none(@NotNull DoublePredicate predicate) {
        var iterator = this.iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextDouble())) {
                return false;
            }
        }
        return true;
    }
}
