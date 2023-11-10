package org.hzt.utils.iterables;

import org.hzt.utils.It;
import org.hzt.utils.function.TriFunction;
import org.hzt.utils.tuples.Pair;
import org.hzt.utils.tuples.Triple;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface Reducable<T> extends Iterable<T> {

    default <R> R fold(R initial, BiFunction<? super R, ? super T, ? extends R> operation) {
        R accumulator = initial;
        for (T t : this) {
            if (t != null) {
                accumulator = operation.apply(accumulator, t);
            }
        }
        return accumulator;
    }

    default <R1, R2> Pair<R1, R2> foldTwo(
            R1 initial1, BiFunction<? super R1, ? super T, ? extends R1> operator1,
            R2 initial2, BiFunction<? super R2, ? super T, ? extends R2> operator2) {
        return foldTwo(initial1, operator1, initial2, operator2, Pair::of);
    }

    default <R1, R2, R> R foldTwo(
            R1 initial1, BiFunction<? super R1, ? super T, ? extends R1> operator1,
            R2 initial2, BiFunction<? super R2, ? super T, ? extends R2> operator2,
            BiFunction<? super R1, ? super R2, ? extends R> finisher) {
        R1 accumulator1 = initial1;
        R2 accumulator2 = initial2;
        for (T next : this) {
            accumulator1 = operator1.apply(accumulator1, next);
            accumulator2 = operator2.apply(accumulator2, next);
        }
        return finisher.apply(accumulator1, accumulator2);
    }

    default <R1, R2, R3> Triple<R1, R2, R3> foldToThree(
            R1 initial1, BiFunction<? super R1, ? super T, ? extends R1> operator1,
            R2 initial2, BiFunction<? super R2, ? super T, ? extends R2> operator2,
            R3 initial3, BiFunction<? super R3, ? super T, ? extends R3> operator3
    ) {
        return foldToThree(initial1, operator1, initial2, operator2, initial3, operator3, Triple::of);
    }

    default <R1, R2, R3, R> R foldToThree(
            R1 initial1, BiFunction<? super R1, ? super T, ? extends R1> operator1,
            R2 initial2, BiFunction<? super R2, ? super T, ? extends R2> operator2,
            R3 initial3, BiFunction<? super R3, ? super T, ? extends R3> operator3,
            TriFunction<? super R1, ? super R2, ? super R3, ? extends R> finisher
    ) {
        R1 accumulator1 = initial1;
        R2 accumulator2 = initial2;
        R3 accumulator3 = initial3;
        for (T next : this) {
            accumulator1 = operator1.apply(accumulator1, next);
            accumulator2 = operator2.apply(accumulator2, next);
            accumulator3 = operator3.apply(accumulator3, next);
        }
        return finisher.apply(accumulator1, accumulator2, accumulator3);
    }

    default Optional<T> reduce(BinaryOperator<T> operation) {
        return IterableReductions.reduce(iterator(), operation);
    }

    default T reduce(T initial, BinaryOperator<T> operation) {
        return IterableReductions.reduce(this, initial, operation);
    }

    default <R> R reduce(T initial,
                         Function<? super T, ? extends R> mapper,
                         BinaryOperator<T> operation) {
        return mapper.apply(reduce(initial, operation));
    }

    default Optional<Pair<T, T>> reduceToTwo(
            BinaryOperator<T> operator1,
            BinaryOperator<T> operator2) {
        return reduceToTwo(operator1, operator2, Pair::of);
    }

    default <R> Optional<R> reduceToTwo(
            BinaryOperator<T> operator1,
            BinaryOperator<T> operator2,
            BiFunction<? super T, ? super T, ? extends R> finisher) {
        Iterator<T> iterator = iterator();
        if (iterator.hasNext()) {
            final T first = iterator.next();
            T accumulator1 = first;
            T accumulator2 = first;
            while (iterator.hasNext()) {
                final T next = iterator.next();
                accumulator1 = operator1.apply(accumulator1, next);
                accumulator2 = operator2.apply(accumulator2, next);
            }
            return Optional.of(finisher.apply(accumulator1, accumulator2));
        }
        return Optional.empty();
    }

    default T single() {
        final Iterator<T> iterator = iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchElementException("Sequence is empty");
        }
        @SuppressWarnings("squid:S1941")
        T single = iterator.next();
        if (iterator.hasNext()) {
            throw new IllegalArgumentException("Sequence has more than one element");
        }
        return single;
    }

    default T single(Predicate<T> predicate) {
        T single = null;
        for (T item : this) {
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

    default T first() {
        return firstOf(It::self);
    }

    default T first(Predicate<T> predicate) {
        for (T next : this) {
            if (next != null && predicate.test(next)) {
                return next;
            }
        }
        throw IterableXHelper.noValuePresentException();
    }

    default T firstNot(Predicate<T> predicate) {
        return first(predicate.negate());
    }

    default <R> R firstOf(Function<? super T, ? extends R> mapper) {
        for (T next : this) {
            if (next != null) {
                final R result = mapper.apply(next);
                if (result != null) {
                    return result;
                }
            }
        }
        throw IterableXHelper.noValuePresentException();
    }

    default Optional<T> findFirst() {
        return findFirstOf(It::self);
    }

    default Optional<T> findFirst(Predicate<T> predicate) {
        for (T next : this) {
            if (next != null && predicate.test(next)) {
                return Optional.of(next);
            }
        }
        return Optional.empty();
    }

    default <R> Optional<R> findFirstOf(Function<? super T, ? extends R> mapper) {
        for (T next : this) {
            if (next != null) {
                return Optional.of(next).map(mapper);
            }
        }
        return Optional.empty();
    }

    default T last() {
        return lastOf(It::self);
    }

    default T last(Predicate<T> predicate) {
        return findLast(predicate).orElseThrow(NoSuchElementException::new);
    }

    default <R> R lastOf(Function<? super T, ? extends R> mapper) {
        return findLastOf(mapper).orElseThrow(NoSuchElementException::new);
    }

    default Optional<T> findLast() {
        return findLastOf(It::self);
    }

    default Optional<T> findLast(Predicate<T> predicate) {
        return IterableReductions.findLast(this, predicate);
    }

    default <R> Optional<R> findLastOf(Function<? super T, ? extends R> mapper) {
        return IterableReductions.findLastOf(this).map(mapper);
    }

    default boolean any(Predicate<T> predicate) {
        return IterableReductions.any(this, predicate);
    }

    default boolean any() {
        return iterator().hasNext();
    }

    default boolean all(Predicate<T> predicate) {
        return IterableReductions.all(this, predicate);
    }

    default boolean none(Predicate<T> predicate) {
        return IterableReductions.none(this, predicate);
    }

    default boolean none() {
        return !iterator().hasNext();
    }
}
