package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.PrimitiveIterator;
import java.util.function.BiFunction;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

@FunctionalInterface
public interface IntReducable extends IntIterable, PrimitiveReducable<Integer, IntBinaryOperator, IntPredicate, OptionalInt> {

    default int reduce(int initial, IntBinaryOperator operator) {
        int accumulator = initial;
        PrimitiveIterator.OfInt iterator = this.iterator();
        while (iterator.hasNext()) {
            int nextInt = iterator.nextInt();
            accumulator = operator.applyAsInt(accumulator, nextInt);
        }
        return accumulator;
    }

    default <R> R reduceTwo(
            int initial1, @NotNull IntBinaryOperator operator1,
            int initial2, @NotNull IntBinaryOperator operator2,
            @NotNull BiFunction<Integer, Integer, R> finisher) {
        PrimitiveIterator.OfInt iterator = iterator();
        int accumulator1 = initial1;
        int accumulator2 = initial2;
        while (iterator.hasNext()) {
            int nextInt = iterator.nextInt();
            accumulator1 = operator1.applyAsInt(accumulator1, nextInt);
            accumulator2 = operator2.applyAsInt(accumulator2, nextInt);
        }
        return finisher.apply(accumulator1, accumulator2);
    }

    default <R> Optional<R> reduceTwo(
            @NotNull IntBinaryOperator operator1,
            @NotNull IntBinaryOperator operator2,
            @NotNull BiFunction<Integer, Integer, R> finisher) {
        PrimitiveIterator.OfInt iterator = iterator();
        if (iterator.hasNext()) {
            final int first = iterator.nextInt();
            int accumulator1 = first;
            int accumulator2 = first;
            while (iterator.hasNext()) {
                final int nextInt = iterator.nextInt();
                accumulator1 = operator1.applyAsInt(accumulator1, nextInt);
                accumulator2 = operator2.applyAsInt(accumulator2, nextInt);
            }
            return Optional.of(finisher.apply(accumulator1, accumulator2));
        }
        return Optional.empty();
    }

    default @NotNull OptionalInt reduce(@NotNull IntBinaryOperator operator) {
        PrimitiveIterator.OfInt iterator = iterator();
        if (iterator.hasNext()) {
            int accumulator = iterator.nextInt();
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
        int accumulator = initial;
        PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            int t = iterator.nextInt();
            accumulator = operation.applyAsInt(accumulator, t);
        }
        return mapper.apply(accumulator);
    }

    default int first() {
        return findFirst().orElseThrow(NoSuchElementException::new);
    }

    default int first(@NotNull IntPredicate predicate) {
        PrimitiveIterator.OfInt iterator = this.iterator();
        while (iterator.hasNext()) {
            int next = iterator.nextInt();
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
        final PrimitiveIterator.OfInt iterator = iterator();
        return iterator.hasNext() ? OptionalInt.of(iterator.nextInt()) : OptionalInt.empty();
    }

    default @NotNull OptionalInt findFirst(@NotNull IntPredicate predicate) {
        PrimitiveIterator.OfInt iterator = this.iterator();
        while (iterator.hasNext()) {
            int next = iterator.nextInt();
            if (predicate.test(next)) {
                return OptionalInt.of(next);
            }
        }
        return OptionalInt.empty();
    }

    default int last() {
        return findLast().orElseThrow(NoSuchElementException::new);
    }

    default @NotNull OptionalInt findLast() {
        return findLast(It::noIntFilter);
    }

    default @NotNull OptionalInt findLast(@NotNull IntPredicate predicate) {
        PrimitiveIterator.OfInt iterator = iterator();
        int result = iterator.nextInt();
        if (!predicate.test(result) && !iterator.hasNext()) {
            return OptionalInt.empty();
        }
        while (iterator.hasNext()) {
            int next = iterator.nextInt();
            if (predicate.test(next)) {
                result = next;
            }
        }
        return OptionalInt.of(result);
    }

    default int single() {
        final PrimitiveIterator.OfInt iterator = iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchElementException("Sequence is empty");
        }
        int single = iterator.nextInt();
        if (iterator.hasNext()) {
            throw new IllegalArgumentException("Sequence has more than one element");
        }
        return single;
    }

    default boolean any(@NotNull IntPredicate predicate) {
        PrimitiveIterator.OfInt iterator = this.iterator();
        while (iterator.hasNext()) {
            int element = iterator.nextInt();
            if (predicate.test(element)) {
                return true;
            }
        }
        return false;
    }

    default boolean all(@NotNull IntPredicate predicate) {
        PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            int t = iterator.nextInt();
            if (!predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

    default boolean none(@NotNull IntPredicate predicate) {
        PrimitiveIterator.OfInt iterator = this.iterator();
        while (iterator.hasNext()) {
            int t = iterator.nextInt();
            if (predicate.test(t)) {
                return false;
            }
        }
        return true;
    }
}
