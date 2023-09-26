package org.hzt.utils.iterables;

import org.hzt.utils.It;
import org.hzt.utils.function.TriFunction;
import org.hzt.utils.tuples.Pair;
import org.hzt.utils.tuples.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface Reducable<T> extends Iterable<T> {

    default <R> @NotNull R fold(@NotNull final R initial, @NotNull final BiFunction<? super R, ? super T, ? extends R> operation) {
        var accumulator = initial;
        for (final var t : this) {
            if (t != null) {
                accumulator = operation.apply(accumulator, t);
            }
        }
        return accumulator;
    }

    default <R1, R2> Pair<R1, R2> foldTwo(
            @NotNull final R1 initial1, @NotNull final BiFunction<? super R1, ? super T, ? extends R1> operator1,
            @NotNull final R2 initial2, @NotNull final BiFunction<? super R2, ? super T, ? extends R2> operator2) {
        return foldTwo(initial1, operator1, initial2, operator2, Pair::of);
    }

    default <R1, R2, R> R foldTwo(
            @NotNull final R1 initial1, @NotNull final BiFunction<? super R1, ? super T, ? extends R1> operator1,
            @NotNull final R2 initial2, @NotNull final BiFunction<? super R2, ? super T, ? extends R2> operator2,
            @NotNull final BiFunction<? super R1, ? super R2, ? extends R> finisher) {
        var accumulator1 = initial1;
        var accumulator2 = initial2;
        for (final var next : this) {
            accumulator1 = operator1.apply(accumulator1, next);
            accumulator2 = operator2.apply(accumulator2, next);
        }
        return finisher.apply(accumulator1, accumulator2);
    }

    default <R1, R2, R3> Triple<R1, R2, R3> foldToThree(
            @NotNull final R1 initial1, @NotNull final BiFunction<? super R1, ? super T, ? extends R1> operator1,
            @NotNull final R2 initial2, @NotNull final BiFunction<? super R2, ? super T, ? extends R2> operator2,
            @NotNull final R3 initial3, @NotNull final BiFunction<? super R3, ? super T, ? extends R3> operator3
    ) {
        return foldToThree(initial1, operator1, initial2, operator2, initial3, operator3, Triple::of);
    }

    default <R1, R2, R3, R> R foldToThree(
            @NotNull final R1 initial1, @NotNull final BiFunction<? super R1, ? super T, ? extends R1> operator1,
            @NotNull final R2 initial2, @NotNull final BiFunction<? super R2, ? super T, ? extends R2> operator2,
            @NotNull final R3 initial3, @NotNull final BiFunction<? super R3, ? super T, ? extends R3> operator3,
            @NotNull final TriFunction<? super R1, ? super R2, ? super R3, ? extends R> finisher
    ) {
        var accumulator1 = initial1;
        var accumulator2 = initial2;
        var accumulator3 = initial3;
        for (final var next : this) {
            accumulator1 = operator1.apply(accumulator1, next);
            accumulator2 = operator2.apply(accumulator2, next);
            accumulator3 = operator3.apply(accumulator3, next);
        }
        return finisher.apply(accumulator1, accumulator2, accumulator3);
    }

    default @NotNull Optional<T> reduce(@NotNull final BinaryOperator<T> operation) {
        return IterableReductions.reduce(iterator(), operation);
    }

    default @NotNull T reduce(@NotNull final T initial, @NotNull final BinaryOperator<T> operation) {
        return IterableReductions.reduce(this, initial, operation);
    }

    default <R> @NotNull R reduce(@NotNull final T initial,
                                  @NotNull final Function<? super T, ? extends R> mapper,
                                  @NotNull final BinaryOperator<T> operation) {
        return mapper.apply(reduce(initial, operation));
    }

    default Optional<Pair<T, T>> reduceToTwo(
            @NotNull final BinaryOperator<T> operator1,
            @NotNull final BinaryOperator<T> operator2) {
        return reduceToTwo(operator1, operator2, Pair::of);
    }

    default <R> Optional<R> reduceToTwo(
            @NotNull final BinaryOperator<T> operator1,
            @NotNull final BinaryOperator<T> operator2,
            @NotNull final BiFunction<? super T, ? super T, ? extends R> finisher) {
        final var iterator = iterator();
        if (iterator.hasNext()) {
            final var first = iterator.next();
            var accumulator1 = first;
            var accumulator2 = first;
            while (iterator.hasNext()) {
                final var next = iterator.next();
                accumulator1 = operator1.apply(accumulator1, next);
                accumulator2 = operator2.apply(accumulator2, next);
            }
            return Optional.of(finisher.apply(accumulator1, accumulator2));
        }
        return Optional.empty();
    }

    default @NotNull T single() {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchElementException("Sequence is empty");
        }
        @SuppressWarnings("squid:S1941") final var single = iterator.next();
        if (iterator.hasNext()) {
            throw new IllegalArgumentException("Sequence has more than one element");
        }
        return single;
    }

    default @NotNull T single(@NotNull final Predicate<T> predicate) {
        T single = null;
        for (final var item : this) {
            if (predicate.test(item)) {
                if (single != null) {
                    throw new IllegalArgumentException("More than one element found matching condition");
                }
                single = item;
            }
        }
        if (single == null) {
            throw new NoSuchElementException("No element matching condition");
        }
        return single;
    }

    default @NotNull T first() {
        return firstOf(It::self);
    }

    default @NotNull T first(@NotNull final Predicate<T> predicate) {
        for (final var next : this) {
            if (next != null && predicate.test(next)) {
                return next;
            }
        }
        throw IterableXHelper.noValuePresentException();
    }

    default @NotNull T firstNot(@NotNull final Predicate<T> predicate) {
        return first(predicate.negate());
    }

    default <R> @NotNull R firstOf(@NotNull final Function<? super T, ? extends R> mapper) {
        for (final var next : this) {
            if (next != null) {
                final var result = mapper.apply(next);
                if (result != null) {
                    return result;
                }
            }
        }
        throw IterableXHelper.noValuePresentException();
    }

    default @NotNull Optional<T> findFirst() {
        return findFirstOf(It::self);
    }

    default @NotNull Optional<T> findFirst(@NotNull final Predicate<T> predicate) {
        for (final var next : this) {
            if (next != null && predicate.test(next)) {
                return Optional.of(next);
            }
        }
        return Optional.empty();
    }

    default <R> @NotNull Optional<R> findFirstOf(@NotNull final Function<? super T, ? extends R> mapper) {
        for (final var next : this) {
            if (next != null) {
                return Optional.of(next).map(mapper);
            }
        }
        return Optional.empty();
    }

    default @NotNull T last() {
        return lastOf(It::self);
    }

    default @NotNull T last(final Predicate<T> predicate) {
        return findLast(predicate).orElseThrow();
    }

    default <R> @NotNull R lastOf(@NotNull final Function<? super T, ? extends R> mapper) {
        return findLastOf(mapper).orElseThrow();
    }

    default Optional<T> findLast() {
        return findLastOf(It::self);
    }

    default @NotNull Optional<T> findLast(@NotNull final Predicate<T> predicate) {
        return IterableReductions.findLast(this, predicate);
    }

    default <R> @NotNull Optional<R> findLastOf(@NotNull final Function<? super T, ? extends R> mapper) {
        return IterableReductions.findLastOf(this).map(mapper);
    }

    default boolean any(@NotNull final Predicate<T> predicate) {
        return IterableReductions.any(this, predicate);
    }

    default boolean any() {
        return iterator().hasNext();
    }

    default boolean all(@NotNull final Predicate<T> predicate) {
        return IterableReductions.all(this, predicate);
    }

    default boolean none(@NotNull final Predicate<T> predicate) {
        return IterableReductions.none(this, predicate);
    }

    default boolean none() {
        return !iterator().hasNext();
    }
}
