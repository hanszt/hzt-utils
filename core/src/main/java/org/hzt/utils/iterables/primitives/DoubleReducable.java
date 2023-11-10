package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.function.primitives.DoubleBiFunction;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.PrimitiveIterator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;

@FunctionalInterface
public interface DoubleReducable extends PrimitiveIterable.OfDouble,
        PrimitiveReducable<Double, DoubleBinaryOperator, DoublePredicate, OptionalDouble> {

    default double reduce(double initial, DoubleBinaryOperator operator) {
        double accumulator = initial;
        PrimitiveIterator.OfDouble iterator = iterator();
        while (iterator.hasNext()) {
            accumulator = operator.applyAsDouble(accumulator, iterator.nextDouble());
        }
        return accumulator;
    }

    @Override
    default OptionalDouble reduce(DoubleBinaryOperator operator) {
        PrimitiveIterator.OfDouble iterator = iterator();
        if (iterator.hasNext()) {
            double accumulator = iterator.nextDouble();
            while (iterator.hasNext()) {
                accumulator = operator.applyAsDouble(accumulator, iterator.nextDouble());
            }
            return OptionalDouble.of(accumulator);
        }
        return OptionalDouble.empty();
    }

    default <R> R reduceToTwo(
            double initial1, DoubleBinaryOperator operator1,
            double initial2, DoubleBinaryOperator operator2,
            DoubleBiFunction<R> finisher) {
        PrimitiveIterator.OfDouble iterator = iterator();
        double accumulator1 = initial1;
        double accumulator2 = initial2;
        while (iterator.hasNext()) {
            double nextDouble = iterator.nextDouble();
            accumulator1 = operator1.applyAsDouble(accumulator1, nextDouble);
            accumulator2 = operator2.applyAsDouble(accumulator2, nextDouble);
        }
        return finisher.apply(accumulator1, accumulator2);
    }

    default <R> Optional<R> reduceToTwo(DoubleBinaryOperator operator1,
                                        DoubleBinaryOperator operator2,
                                        DoubleBiFunction<R> finisher) {
        PrimitiveIterator.OfDouble iterator = iterator();
        if (iterator.hasNext()) {
            final double first = iterator.nextDouble();
            double accumulator1 = first;
            double accumulator2 = first;
            while (iterator.hasNext()) {
                double nextDouble = iterator.nextDouble();
                accumulator1 = operator1.applyAsDouble(accumulator1, nextDouble);
                accumulator2 = operator2.applyAsDouble(accumulator2, nextDouble);
            }
            return Optional.of(finisher.apply(accumulator1, accumulator2));
        }
        return Optional.empty();
    }

    default <R> R reduce(double initial,
                         DoubleFunction<R> mapper,
                         DoubleBinaryOperator operation) {
        return mapper.apply(reduce(initial, operation));
    }

    default double first() {
        return findFirst().orElseThrow(NoSuchElementException::new);
    }

    default double first(DoublePredicate predicate) {
        return findFirst(predicate).orElseThrow(NoSuchElementException::new);
    }

    @Override
    default OptionalDouble findFirst() {
        final PrimitiveIterator.OfDouble iterator = iterator();
        return iterator.hasNext() ? OptionalDouble.of(iterator.nextDouble()) : OptionalDouble.empty();
    }

    @Override
    default OptionalDouble findFirst(DoublePredicate predicate) {
        PrimitiveIterator.OfDouble iterator = this.iterator();
        while (iterator.hasNext()) {
            double next = iterator.nextDouble();
            if (predicate.test(next)) {
                return OptionalDouble.of(next);
            }
        }
        return OptionalDouble.empty();
    }

    default double last() {
        return findLast().orElseThrow(NoSuchElementException::new);
    }

    default double last(DoublePredicate predicate) {
        return findLast(predicate).orElseThrow(NoSuchElementException::new);
    }

    @Override
    default OptionalDouble findLast() {
        return findLast(It::noDoubleFilter);
    }

    @Override
    default OptionalDouble findLast(DoublePredicate predicate) {
        PrimitiveIterator.OfDouble iterator = iterator();
        double result = iterator.nextDouble();
        if (!predicate.test(result) && !iterator.hasNext()) {
            return OptionalDouble.empty();
        }
        while (iterator.hasNext()) {
            double next = iterator.nextDouble();
            if (predicate.test(next)) {
                result = next;
            }
        }
        return OptionalDouble.of(result);
    }

    default double single() {
        final PrimitiveIterator.OfDouble iterator = iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchElementException("Sequence is empty");
        }
        @SuppressWarnings("squid:S1941")
        double single = iterator.nextDouble();
        if (iterator.hasNext()) {
            throw new IllegalArgumentException("Sequence has more than one element");
        }
        return single;
    }

    @Override
    default boolean any(DoublePredicate predicate) {
        PrimitiveIterator.OfDouble iterator = iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextDouble())) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean all(DoublePredicate predicate) {
        PrimitiveIterator.OfDouble iterator = iterator();
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.nextDouble())) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean none(DoublePredicate predicate) {
        PrimitiveIterator.OfDouble iterator = this.iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextDouble())) {
                return false;
            }
        }
        return true;
    }
}
