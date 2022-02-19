package hzt.iterables.primitives;

import hzt.function.IntFoldFunction;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.OptionalInt;
import java.util.PrimitiveIterator;
import java.util.function.BinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

@FunctionalInterface
public interface IntReducable extends IntIterable, PrimitiveReducable<Integer, IntBinaryOperator, IntPredicate, OptionalInt> {

    default int reduce(int initial, IntBinaryOperator operator) {
        int accumulator = initial;
        PrimitiveIterator.OfInt iterator = this.iterator();
        while (iterator.hasNext()) {
            int t = iterator.nextInt();
            accumulator = operator.applyAsInt(accumulator, t);
        }
        return initial;
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

    default <R> @NotNull R reduce(@NotNull R initial,
                                  @NotNull IntFunction<R> mapper,
                                  @NotNull BinaryOperator<R> operation) {
        return fold(initial, (acc, next) -> operation.apply(acc, mapper.apply(next)));
    }

    default <R> @NotNull R fold(@NotNull R initial,
                                @NotNull IntFoldFunction<R> operation) {
        R accumulator = initial;
        PrimitiveIterator.OfInt iterator = this.iterator();
        while (iterator.hasNext()) {
            int t = iterator.nextInt();
            accumulator = operation.apply(accumulator, t);
        }
        return accumulator;
    }

    default long first() {
        return findFirst().orElseThrow();
    }

    default long first(@NotNull IntPredicate predicate) {
        PrimitiveIterator.OfInt iterator = this.iterator();
        while (iterator.hasNext()) {
            int next = iterator.nextInt();
            if (predicate.test(next)) {
                return next;
            }
        }
        throw new NoSuchElementException();
    }

    default long firstNot(@NotNull IntPredicate predicate) {
        return first(predicate.negate());
    }

    default @NotNull OptionalInt findFirst() {
        final var iterator = iterator();
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

    default long last() {
        return findLast().orElseThrow();
    }

    default @NotNull OptionalInt findLast() {
        return findLast(It::noIntFilter);
    }

    default @NotNull OptionalInt findLast(@NotNull IntPredicate predicate) {
        var iterator = iterator();
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

    default long count(@NotNull IntPredicate predicate) {
        long count = 0;
        PrimitiveIterator.OfInt iterator = this.iterator();
        while (iterator.hasNext()) {
            int i = iterator.nextInt();
            if (predicate.test(i)) {
                count++;
            }
        }
        return count;
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
        PrimitiveIterator.OfInt iterator = this.iterator();
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

    @Override
    default long count() {
        long counter = 0;
        final var iterator = iterator();
        while (iterator.hasNext()) {
            iterator.nextInt();
            counter++;
        }
        return counter;
    }
}
