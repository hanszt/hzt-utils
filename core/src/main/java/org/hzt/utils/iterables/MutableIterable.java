package org.hzt.utils.iterables;

import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
public interface MutableIterable<T> extends Iterable<T> {

    static <T> MutableIterable<T> of(final Iterable<T> iterable) {
        return iterable::iterator;
    }

    default boolean removeIf(final Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        boolean removed = false;
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var element = iterator.next();
            if (predicate.test(element)) {
                iterator.remove();
                removed = true;
            }
        }
        return removed;
    }
}
