package hzt.iterables.primitives;

import hzt.function.LongFoldFunction;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.OptionalLong;
import java.util.PrimitiveIterator;
import java.util.function.BinaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;

@FunctionalInterface
public interface LongReducable extends LongIterable, PrimitiveReducable<Long, LongBinaryOperator, LongPredicate, OptionalLong> {

    default long reduce(long initial, LongBinaryOperator operator) {
        long accumulator = initial;
        PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            long t = iterator.nextLong();
            accumulator = operator.applyAsLong(accumulator, t);
        }
        return accumulator;
    }

    default @NotNull OptionalLong reduce(@NotNull LongBinaryOperator operator) {
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

    default <R> @NotNull R reduce(@NotNull R initial,
                                  @NotNull LongFunction<R> mapper,
                                  @NotNull BinaryOperator<R> operation) {
        return fold(initial, (acc, next) -> operation.apply(acc, mapper.apply(next)));
    }

    default <R> @NotNull R fold(@NotNull R initial,
                                @NotNull LongFoldFunction<R> operation) {
        R accumulator = initial;
        PrimitiveIterator.OfLong iterator = this.iterator();
        while (iterator.hasNext()) {
            long t = iterator.nextLong();
            accumulator = operation.apply(accumulator, t);
        }
        return accumulator;
    }

    default long first() {
        return findFirst().orElseThrow(NoSuchElementException::new);
    }

    default long first(@NotNull LongPredicate predicate) {
        return findFirst(predicate).orElseThrow(NoSuchElementException::new);
    }

    default long firstNot(@NotNull LongPredicate predicate) {
        return first(predicate.negate());
    }

    default @NotNull OptionalLong findFirst() {
        final PrimitiveIterator.OfLong iterator = iterator();
        return iterator.hasNext() ? OptionalLong.of(iterator.nextLong()) : OptionalLong.empty();
    }

    default @NotNull OptionalLong findFirst(@NotNull LongPredicate predicate) {
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

    default long last(@NotNull LongPredicate predicate) {
        return findLast(predicate).orElseThrow(NoSuchElementException::new);
    }

    default @NotNull OptionalLong findLast() {
        return findLast(It::noLongFilter);
    }

    default @NotNull OptionalLong findLast(@NotNull LongPredicate predicate) {
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

    default boolean any(@NotNull LongPredicate predicate) {
        PrimitiveIterator.OfLong iterator = this.iterator();
        while (iterator.hasNext()) {
            long element = iterator.nextLong();
            if (predicate.test(element)) {
                return true;
            }
        }
        return false;
    }

    default boolean all(@NotNull LongPredicate predicate) {
        PrimitiveIterator.OfLong iterator = this.iterator();
        while (iterator.hasNext()) {
            long t = iterator.nextLong();
            if (!predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

    default boolean none(@NotNull LongPredicate predicate) {
        PrimitiveIterator.OfLong iterator = this.iterator();
        while (iterator.hasNext()) {
            long t = iterator.nextLong();
            if (predicate.test(t)) {
                return false;
            }
        }
        return true;
    }
}
