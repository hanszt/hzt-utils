package org.hzt.utils.iterables;

import org.hzt.utils.It;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface ShortCircuitable<T> extends Iterable<T> {

    default T single() {
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

    default T single(final Predicate<T> predicate) {
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

    default Optional<T> findSingle() {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        @SuppressWarnings("squid:S1941") final var single = iterator.next();
        if (iterator.hasNext() || single == null) {
            return Optional.empty();
        }
        return Optional.of(single);
    }

    default Optional<T> findSingle(final Predicate<T> predicate) {
        T single = null;
        for (final var item : this) {
            if (predicate.test(item)) {
                if (single != null) {
                    return Optional.empty();
                }
                single = item;
            }
        }
        if (single == null) {
            return Optional.empty();
        }
        return Optional.of(single);
    }

    default T first() {
        return firstOf(It::self);
    }

    default T first(final Predicate<T> predicate) {
        for (final var next : this) {
            if (next != null && predicate.test(next)) {
                return next;
            }
        }
        throw IterableXHelper.noValuePresentException();
    }

    default T firstNot(final Predicate<T> predicate) {
        return first(predicate.negate());
    }

    default <R> R firstOf(final Function<? super T, ? extends R> mapper) {
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

    default Optional<T> findFirst() {
        return findFirstOf(It::self);
    }

    default Optional<T> findFirst(final Predicate<T> predicate) {
        for (final var next : this) {
            if (next != null && predicate.test(next)) {
                return Optional.of(next);
            }
        }
        return Optional.empty();
    }

    default <R> Optional<R> findFirstOf(final Function<? super T, ? extends R> mapper) {
        for (final var next : this) {
            if (next != null) {
                return Optional.of(next).map(mapper);
            }
        }
        return Optional.empty();
    }

    default T last() {
        return lastOf(It::self);
    }

    default T last(final Predicate<T> predicate) {
        return findLast(predicate).orElseThrow();
    }

    default <R> R lastOf(final Function<? super T, ? extends R> mapper) {
        return findLastOf(mapper).orElseThrow(NoSuchElementException::new);
    }

    default Optional<T> findLast() {
        return findLastOf(It::self);
    }

    default Optional<T> findLast(final Predicate<T> predicate) {
        return IterableReductions.findLast(this, predicate);
    }

    default <R> Optional<R> findLastOf(final Function<? super T, ? extends R> mapper) {
        return IterableReductions.findLastOf(this).map(mapper);
    }

    default boolean any(final Predicate<T> predicate) {
        return IterableReductions.any(this, predicate);
    }

    default boolean any() {
        return iterator().hasNext();
    }

    default boolean all(final Predicate<T> predicate) {
        return IterableReductions.all(this, predicate);
    }

    default boolean none(final Predicate<T> predicate) {
        return IterableReductions.none(this, predicate);
    }

    default boolean none() {
        return !iterator().hasNext();
    }
}
