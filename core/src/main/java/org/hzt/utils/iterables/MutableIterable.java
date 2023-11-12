package org.hzt.utils.iterables;

import java.util.Iterator;
import java.util.function.Predicate;

@FunctionalInterface
public interface MutableIterable<T> extends Iterable<T> {

    static <T> MutableIterable<T> of(final Iterable<T> iterable) {
        return iterable::iterator;
    }

    default boolean removeIf(final Predicate<? super T> predicate) {
        final Iterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            final T element = iterator.next();
            if (predicate.test(element)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
}
