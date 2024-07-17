package org.hzt.utils.iterables;

import org.hzt.utils.function.IndexedBiFunction;
import org.hzt.utils.function.TriFunction;
import org.hzt.utils.tuples.Pair;
import org.hzt.utils.tuples.Triple;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

@FunctionalInterface
public interface Reducable<T> extends ShortCircuitable<T> {

    default <R> R fold(final R initial, final BiFunction<? super R, ? super T, ? extends R> operation) {
        var accumulator = initial;
        for (final var t : this) {
            if (t != null) {
                accumulator = operation.apply(accumulator, t);
            }
        }
        return accumulator;
    }

    default <R> R foldIndexed(final R initial, final IndexedBiFunction<? super R, ? super T, ? extends R> operation) {
        var accumulator = initial;
        var index = 0;
        for (final var t : this) {
            if (t != null) {
                accumulator = operation.apply(index++, accumulator, t);
            }
        }
        return accumulator;
    }

    default <R1, R2> Pair<R1, R2> foldTwo(
            final R1 initial1, final BiFunction<? super R1, ? super T, ? extends R1> operator1,
            final R2 initial2, final BiFunction<? super R2, ? super T, ? extends R2> operator2) {
        return foldTwo(initial1, operator1, initial2, operator2, Pair::of);
    }

    default <R1, R2, R> R foldTwo(
            final R1 initial1, final BiFunction<? super R1, ? super T, ? extends R1> operator1,
            final R2 initial2, final BiFunction<? super R2, ? super T, ? extends R2> operator2,
            final BiFunction<? super R1, ? super R2, ? extends R> finisher) {
        var accumulator1 = initial1;
        var accumulator2 = initial2;
        for (final var next : this) {
            accumulator1 = operator1.apply(accumulator1, next);
            accumulator2 = operator2.apply(accumulator2, next);
        }
        return finisher.apply(accumulator1, accumulator2);
    }

    default <R1, R2, R3> Triple<R1, R2, R3> foldThree(
            final R1 initial1, final BiFunction<? super R1, ? super T, ? extends R1> operator1,
            final R2 initial2, final BiFunction<? super R2, ? super T, ? extends R2> operator2,
            final R3 initial3, final BiFunction<? super R3, ? super T, ? extends R3> operator3
    ) {
        return foldThree(initial1, operator1, initial2, operator2, initial3, operator3, Triple::of);
    }

    default <R1, R2, R3, R> R foldThree(
            final R1 initial1, final BiFunction<? super R1, ? super T, ? extends R1> operator1,
            final R2 initial2, final BiFunction<? super R2, ? super T, ? extends R2> operator2,
            final R3 initial3, final BiFunction<? super R3, ? super T, ? extends R3> operator3,
            final TriFunction<? super R1, ? super R2, ? super R3, ? extends R> finisher
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

    default Optional<T> reduce(final BinaryOperator<T> operation) {
        return IterableReductions.reduce(iterator(), operation);
    }

    default T reduce(final T initial, final BinaryOperator<T> operation) {
        return IterableReductions.reduce(this, initial, operation);
    }

    default <R> R reduce(final T initial,
                         final Function<? super T, ? extends R> mapper,
                         final BinaryOperator<T> operation) {
        return mapper.apply(reduce(initial, operation));
    }

    default Optional<Pair<T, T>> reduceTwo(
            final BinaryOperator<T> operator1,
            final BinaryOperator<T> operator2) {
        return reduceTwo(operator1, operator2, Pair::of);
    }

    default <R> Optional<R> reduceTwo(
            final BinaryOperator<T> operator1,
            final BinaryOperator<T> operator2,
            final BiFunction<? super T, ? super T, ? extends R> finisher) {
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
}
