package org.hzt.utils.iterators.functional_iterator;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@FunctionalInterface
public interface IteratorX<T> {

    static <T> IteratorX<T> of(Iterator<T> iterator) {
        return action -> {
            boolean hasNext = iterator.hasNext();
            if (hasNext) {
                action.accept(iterator.next());
            }
            return hasNext;
        };
    }

    boolean tryAdvance(Consumer<? super T> action);

    default void forEachRemaining(Consumer<? super T> action) {
        //noinspection StatementWithEmptyBody
        do {
        } while (tryAdvance(action));
    }

    /**
     * Use of the asIterator() function requires the user to always call the hasNext() function before the next() function.
     * Otherwise, it will throw a NoSuchElementException.
     *
     * @return an iterator object from a iteratorX object
     */
    default @NotNull Iterator<T> asIterator() {
        return new Iterator<>() {

            private final AtomicReference<T> sink = new AtomicReference<>();

            @Override
            public boolean hasNext() {
                return tryAdvance(sink::set);
            }

            @Override
            public T next() {
                final var value = sink.get();
                sink.set(null);
                if (value == null) {
                    throw new NoSuchElementException();
                }
                return value;
            }
        };
    }

    default Spliterator<T> asSpliterator() {
        return Spliterators.spliteratorUnknownSize(asIterator(), 0);
    }

}
