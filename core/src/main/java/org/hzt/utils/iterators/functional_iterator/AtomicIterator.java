package org.hzt.utils.iterators.functional_iterator;


import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@FunctionalInterface
@SuppressWarnings("squid:S1711")
public interface AtomicIterator<T> {

    static <T> AtomicIterator<T> of(final Iterator<T> iterator) {
        return action -> {
            final boolean hasNext = iterator.hasNext();
            if (hasNext) {
                action.accept(iterator.next());
            }
            return hasNext;
        };
    }

    static <T> AtomicIterator<T> of(final Spliterator<T> spliterator) {
        return spliterator::tryAdvance;
    }

    boolean tryAdvance(Consumer<? super T> action);

    default void forEachRemaining(final Consumer<? super T> action) {
        //noinspection StatementWithEmptyBody
        while (tryAdvance(action)) {
        }
    }

    /**
     * Use of the asIterator() function requires the user to always call the hasNext() function before the next() function.
     * Otherwise, it will throw a NoSuchElementException.
     *
     * @return an iterator object from a iteratorX object
     */
    default Iterator<T> asIterator() {
        return new Iterator<T>() {

            private final AtomicReference<T> sink = new AtomicReference<>();

            @Override
            public boolean hasNext() {
                return tryAdvance(sink::set);
            }

            @Override
            public T next() {
                final T value = sink.get();
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
