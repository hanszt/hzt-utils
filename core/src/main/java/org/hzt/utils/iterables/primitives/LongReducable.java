package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.function.primitives.LongBiFunction;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.PrimitiveIterator;
import java.util.function.LongBinaryOperator;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;

@FunctionalInterface
public interface LongReducable extends PrimitiveIterable.OfLong, PrimitiveReducable<Long, LongBinaryOperator, LongPredicate, OptionalLong> {

    default long reduce(long initial, LongBinaryOperator operator) {
        PrimitiveIterator.OfLong iterator = iterator();
        long accumulator = initial;
        while (iterator.hasNext()) {
            accumulator = operator.applyAsLong(accumulator, iterator.nextLong());
        }
        return accumulator;
    }

    default OptionalLong reduce(LongBinaryOperator operator) {
        PrimitiveIterator.OfLong iterator = iterator();
        if (iterator.hasNext()) {
            long accumulator = iterator.nextLong();
            while (iterator.hasNext()) {
                accumulator = operator.applyAsLong(accumulator, iterator.nextLong());
            }
            return OptionalLong.of(accumulator);
        }
        return OptionalLong.empty();
    }

    default <R> R reduceToTwo(
            long initial1, LongBinaryOperator operator1,
            long initial2, LongBinaryOperator operator2,
            LongBiFunction<R> finisher) {
        PrimitiveIterator.OfLong iterator = iterator();
        long accumulator1 = initial1;
        long accumulator2 = initial2;
        while (iterator.hasNext()) {
            long nextLong = iterator.nextLong();
            accumulator1 = operator1.applyAsLong(accumulator1, nextLong);
            accumulator2 = operator2.applyAsLong(accumulator2, nextLong);
        }
        return finisher.apply(accumulator1, accumulator2);
    }

    default <R> Optional<R> reduceToTwo(LongBinaryOperator operator1,
                                        LongBinaryOperator operator2,
                                        LongBiFunction<R> finisher) {
        PrimitiveIterator.OfLong iterator = iterator();
        if (iterator.hasNext()) {
            final long first = iterator.nextLong();
            long accumulator1 = first;
            long accumulator2 = first;
            while (iterator.hasNext()) {
                long nextLong = iterator.nextLong();
                accumulator1 = operator1.applyAsLong(accumulator1, nextLong);
                accumulator2 = operator2.applyAsLong(accumulator2, nextLong);
            }
            return Optional.of(finisher.apply(accumulator1, accumulator2));
        }
        return Optional.empty();
    }

    default <R> R reduce(long initial,
                         LongFunction<R> mapper,
                         LongBinaryOperator operation) {
        return mapper.apply(reduce(initial, operation));
    }

    default long first() {
        return findFirst().orElseThrow(NoSuchElementException::new);
    }

    default long first(LongPredicate predicate) {
        return findFirst(predicate).orElseThrow(NoSuchElementException::new);
    }

    default long firstNot(LongPredicate predicate) {
        return first(predicate.negate());
    }

    default OptionalLong findFirst() {
        final PrimitiveIterator.OfLong iterator = iterator();
        return iterator.hasNext() ? OptionalLong.of(iterator.nextLong()) : OptionalLong.empty();
    }

    default OptionalLong findFirst(LongPredicate predicate) {
        PrimitiveIterator.OfLong iterator = this.iterator();
        while (iterator.hasNext()) {
            long next = iterator.nextLong();
            if (predicate.test(next)) {
                return OptionalLong.of(next);
            }
        }
        return OptionalLong.empty();
    }

    default long last() {
        return findLast().orElseThrow(NoSuchElementException::new);
    }

    default long last(LongPredicate predicate) {
        return findLast(predicate).orElseThrow(NoSuchElementException::new);
    }

    default OptionalLong findLast() {
        return findLast(It::noLongFilter);
    }

    default OptionalLong findLast(LongPredicate predicate) {
        PrimitiveIterator.OfLong iterator = iterator();
        long result = iterator.nextLong();
        if (!predicate.test(result) && !iterator.hasNext()) {
            return OptionalLong.empty();
        }
        while (iterator.hasNext()) {
            long next = iterator.nextLong();
            if (predicate.test(next)) {
                result = next;
            }
        }
        return OptionalLong.of(result);
    }

    default long single() {
        final PrimitiveIterator.OfLong iterator = iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchElementException("Sequence is empty");
        }
        @SuppressWarnings("squid:S1941")
        long single = iterator.nextLong();
        if (iterator.hasNext()) {
            throw new IllegalArgumentException("Sequence has more than one element");
        }
        return single;
    }

    default boolean any(LongPredicate predicate) {
        PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextLong())) {
                return true;
            }
        }
        return false;
    }

    default boolean all(LongPredicate predicate) {
        PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.nextLong())) {
                return false;
            }
        }
        return true;
    }

    default boolean none(LongPredicate predicate) {
        PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextLong())) {
                return false;
            }
        }
        return true;
    }
}
