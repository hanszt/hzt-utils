package hzt.iterables;

import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@FunctionalInterface
public interface Reducable<T> extends Iterable<T> {

    default <R> @NotNull R fold(@NotNull R initial,
                                @NotNull BiFunction<? super R, ? super T, ? extends R> operation) {
        R accumulator = initial;
        for (T t : this) {
            if (t != null) {
                accumulator = operation.apply(accumulator, t);
            }
        }
        return accumulator;
    }

    default @NotNull Optional<T> reduce(@NotNull BinaryOperator<T> operation) {
        return IterableReductions.reduce(iterator(), operation);
    }

    default @NotNull T reduce(@NotNull T initial, @NotNull BinaryOperator<T> operation) {
        return IterableReductions.reduce(this, initial, operation);
    }

    default <R> @NotNull R reduce(@NotNull R initial,
                                  @NotNull Function<? super T, ? extends R> mapper,
                                  @NotNull BinaryOperator<R> operation) {
        return fold(initial, (acc, next) -> operation.apply(acc, mapper.apply(next)));
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

    default @NotNull T findFirstOrElseGet(@NotNull Supplier<T> supplier) {
        return findFirstOf(It::self).orElseGet(supplier);
    }

    default @NotNull T findFirstOrElse(@NotNull T defaultValue) {
        return findFirstOf(It::self).orElse(defaultValue);
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

    default <R> @NotNull R lastOf(@NotNull Function<? super T, ? extends R> mapper) {
        final Iterator<T> iterator = iterator();
        if (!iterator.hasNext()) {
            throw IterableXHelper.noValuePresentException();
        } else if (this instanceof List) {
            //noinspection unchecked
            return IterableXHelper.findLastIfInstanceOfList(Objects::nonNull, (List<T>) this).map(mapper)
                    .orElseThrow(IllegalStateException::new);
        } else {
            return IterableXHelper.findLastIfUnknownIterable(Objects::nonNull, iterator).map(mapper)
                    .orElseThrow(IllegalStateException::new);
        }
    }

    default Optional<T> findLast() {
        return findLastOf(It::self);
    }

    default @NotNull T findLastOrElse(@NotNull T defaultVal) {
        return findFirstOrElseGet(() -> defaultVal);
    }

    default @NotNull T findLastOrElseGet(@NotNull Supplier<T> supplier) {
        return findLast().orElseGet(supplier);
    }

    default @NotNull Optional<T> findLast(@NotNull Predicate<T> predicate) {
        return IterableReductions.findLast(this, predicate);
    }

    default <R> @NotNull Optional<R> findLastOf(@NotNull Function<T, R> mapper) {
        return IterableReductions.findLastOf(this, mapper);
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
