package hzt.iterables.primitives;

import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.OptionalDouble;
import java.util.PrimitiveIterator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;

@FunctionalInterface
public interface DoubleReducable extends DoubleIterable, PrimitiveReducable<Double, DoubleBinaryOperator, DoublePredicate, OptionalDouble> {

    default double reduce(double initial, DoubleBinaryOperator operator) {
        double accumulator = initial;
        PrimitiveIterator.OfDouble iterator = iterator();
        while (iterator.hasNext()) {
            accumulator = operator.applyAsDouble(accumulator, iterator.nextDouble());
        }
        return accumulator;
    }

    @Override
    @NotNull
    default OptionalDouble reduce(@NotNull DoubleBinaryOperator operator) {
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

    default <R> @NotNull R reduce(double initial,
                                  @NotNull DoubleFunction<R> mapper,
                                  @NotNull DoubleBinaryOperator operation) {
        double accumulator = initial;
        PrimitiveIterator.OfDouble iterator = this.iterator();
        while (iterator.hasNext()) {
            double t = iterator.nextDouble();
            accumulator = operation.applyAsDouble(accumulator, t);
        }
        return mapper.apply(accumulator);
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
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchElementException("Sequence is empty");
        }
        double single = iterator.nextDouble();
        if (iterator.hasNext()) {
            throw new IllegalArgumentException("Sequence has more than one element");
        }
        return single;
    }

    @Override
    default boolean any(@NotNull DoublePredicate predicate) {
        PrimitiveIterator.OfDouble iterator = this.iterator();
        while (iterator.hasNext()) {
            double element = iterator.nextDouble();
            if (predicate.test(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean all(@NotNull DoublePredicate predicate) {
        PrimitiveIterator.OfDouble iterator = this.iterator();
        while (iterator.hasNext()) {
            double t = iterator.nextDouble();
            if (!predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean none(@NotNull DoublePredicate predicate) {
        PrimitiveIterator.OfDouble iterator = this.iterator();
        while (iterator.hasNext()) {
            double t = iterator.nextDouble();
            if (predicate.test(t)) {
                return false;
            }
        }
        return true;
    }
}
