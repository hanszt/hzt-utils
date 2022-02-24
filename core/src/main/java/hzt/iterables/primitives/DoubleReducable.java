package hzt.iterables.primitives;

import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.OptionalDouble;
import java.util.PrimitiveIterator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoublePredicate;

@FunctionalInterface
public interface DoubleReducable extends DoubleIterable, PrimitiveReducable<Double, DoubleBinaryOperator, DoublePredicate, OptionalDouble> {
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

    default double first() {
        return findFirst().orElseThrow(NoSuchElementException::new);
    }

    default double first(@NotNull DoublePredicate predicate) {
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

    default double last(@NotNull DoublePredicate predicate) {
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
