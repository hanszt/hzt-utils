package org.hzt.utils.iterators.functional_iterator;


import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

@FunctionalInterface
public interface AtomicIterator<T> {

    static <T> AtomicIterator<T> of(final Iterator<T> iterator) {
        return action -> {
            final var hasNext = iterator.hasNext();
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
        while (tryAdvance(action));
    }

    /**
     * Use of the asIterator() function requires the user to always call the hasNext() function before the next() function.
     * Otherwise, it will throw a NoSuchElementException.
     *
     * @return an iterator object from a iteratorX object
     */
    default Iterator<T> asIterator() {
        final var sink = new Object() {
            boolean hasNext = false;
            T next = null;
        };
        return new Iterator<>() {

            @Override
            public boolean hasNext() {
                return sink.hasNext || (sink.hasNext = tryAdvance(v -> sink.next = v));
            }

            @Override
            public T next() {
                if (hasNext()) {
                    sink.hasNext = false;
                    final var next = sink.next;
                    sink.next = null;
                    return next;
                }
                throw new NoSuchElementException();
            }
        };
    }

    default Spliterator<T> asSpliterator() {
        return Spliterators.spliteratorUnknownSize(asIterator(), 0);
    }

}
