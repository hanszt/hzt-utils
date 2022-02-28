package hzt.iterables;

import hzt.function.TriFunction;
import hzt.tuples.Pair;
import hzt.tuples.Triple;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface Reducable<T> extends Iterable<T> {

    default <R> @NotNull R fold(@NotNull R initial, @NotNull BiFunction<? super R, ? super T, ? extends R> operation) {
        R accumulator = initial;
        for (T t : this) {
            if (t != null) {
                accumulator = operation.apply(accumulator, t);
            }
        }
        return accumulator;
    }

    default <R1, R2> Pair<R1, R2> foldTwo(
            @NotNull R1 initial1, @NotNull BiFunction<? super R1, ? super T, ? extends R1> operator1,
            @NotNull R2 initial2, @NotNull BiFunction<? super R2, ? super T, ? extends R2> operator2
    ) {
        return foldTwo(initial1, operator1, initial2, operator2, Pair::of);
    }

    default <R1, R2, R> R foldTwo(
            @NotNull R1 initial1, @NotNull BiFunction<? super R1, ? super T, ? extends R1> operator1,
            @NotNull R2 initial2, @NotNull BiFunction<? super R2, ? super T, ? extends R2> operator2,
            @NotNull BiFunction<? super R1, ? super R2, ? extends R> finisher
    ) {
        R1 accumulator1 = initial1;
        R2 accumulator2 = initial2;
        for (T next : this) {
            accumulator1 = operator1.apply(accumulator1, next);
            accumulator2 = operator2.apply(accumulator2, next);
        }
        return finisher.apply(accumulator1, accumulator2);
    }

    default <R1, R2, R3> Triple<R1, R2, R3> foldThree(
            @NotNull R1 initial1, @NotNull BiFunction<? super R1, ? super T, ? extends R1> operator1,
            @NotNull R2 initial2, @NotNull BiFunction<? super R2, ? super T, ? extends R2> operator2,
            @NotNull R3 initial3, @NotNull BiFunction<? super R3, ? super T, ? extends R3> operator3
    ) {
        return foldThree(initial1, operator1, initial2, operator2, initial3, operator3, Triple::of);
    }

    default <R1, R2, R3, R> R foldThree(
            @NotNull R1 initial1, @NotNull BiFunction<? super R1, ? super T, ? extends R1> operator1,
            @NotNull R2 initial2, @NotNull BiFunction<? super R2, ? super T, ? extends R2> operator2,
            @NotNull R3 initial3, @NotNull BiFunction<? super R3, ? super T, ? extends R3> operator3,
            @NotNull TriFunction<? super R1, ? super R2, ? super R3, ? extends R> finisher
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

    default @NotNull Optional<T> reduce(@NotNull BinaryOperator<T> operation) {
        return IterableReductions.reduce(iterator(), operation);
    }

    default @NotNull T reduce(@NotNull T initial, @NotNull BinaryOperator<T> operation) {
        return IterableReductions.reduce(this, initial, operation);
    }

    default <R> @NotNull R reduce(@NotNull T initial,
                                  @NotNull Function<? super T, ? extends R> mapper,
                                  @NotNull BinaryOperator<T> operation) {
        T accumulator = initial;
        for (T t : this) {
            if (t != null) {
                accumulator = operation.apply(accumulator, t);
            }
        }
        return mapper.apply(accumulator);
    }

    default Optional<Pair<T, T>> reduceTwo(
            @NotNull BinaryOperator<T> operator1,
            @NotNull BinaryOperator<T> operator2) {
        return reduceTwo(operator1, operator2, Pair::of);
    }

    default <R> Optional<R> reduceTwo(
            @NotNull BinaryOperator<T> operator1,
            @NotNull BinaryOperator<T> operator2,
            @NotNull BiFunction<? super T, ? super T, ? extends R> finisher) {
        Iterator<T> iterator = iterator();
        if (iterator.hasNext()) {
            final var first = iterator.next();
            T accumulator1 = first;
            T accumulator2 = first;
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
        T single = iterator.next();
        if (iterator.hasNext()) {
            throw new IllegalArgumentException("Sequence has more than one element");
        }
        return single;
    }

    default @NotNull T single(@NotNull Predicate<T> predicate) {
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

    default @NotNull T first() {
        return firstOf(It::self);
    }

    default @NotNull T first(@NotNull Predicate<T> predicate) {
        for (T next : this) {
            if (next != null && predicate.test(next)) {
                return next;
            }
        }
        throw IterableXHelper.noValuePresentException();
    }

    default @NotNull T firstNot(@NotNull Predicate<T> predicate) {
        return first(predicate.negate());
    }

    default <R> @NotNull R firstOf(@NotNull Function<? super T, ? extends R> mapper) {
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

    default @NotNull Optional<T> findFirst() {
        return findFirstOf(It::self);
    }

    default @NotNull Optional<T> findFirst(@NotNull Predicate<T> predicate) {
        for (T next : this) {
            if (next != null && predicate.test(next)) {
                return Optional.of(next);
            }
        }
        return Optional.empty();
    }

    default <R> @NotNull Optional<R> findFirstOf(@NotNull Function<? super T, ? extends R> mapper) {
        for (T next : this) {
            if (next != null) {
                return Optional.of(next).map(mapper);
            }
        }
        return Optional.empty();
    }

    default @NotNull T last() {
        return lastOf(It::self);
    }

    default @NotNull T last(Predicate<T> predicate) {
        return findLast(predicate).orElseThrow();
    }

    default <R> @NotNull R lastOf(@NotNull Function<? super T, ? extends R> mapper) {
        return findLastOf(mapper).orElseThrow();
    }

    default Optional<T> findLast() {
        return findLastOf(It::self);
    }

    default @NotNull Optional<T> findLast(@NotNull Predicate<T> predicate) {
        return IterableReductions.findLast(this, predicate);
    }

    default <R> @NotNull Optional<R> findLastOf(@NotNull Function<? super T, ? extends R> mapper) {
        return IterableReductions.findLastOf(this).map(mapper);
    }

    default boolean any(@NotNull Predicate<T> predicate) {
        return IterableReductions.any(this, predicate);
    }

    default boolean any() {
        return iterator().hasNext();
    }

    default boolean all(@NotNull Predicate<T> predicate) {
        return IterableReductions.all(this, predicate);
    }

    default boolean none(@NotNull Predicate<T> predicate) {
        return IterableReductions.none(this, predicate);
    }

    default boolean none() {
        return !iterator().hasNext();
    }
}
