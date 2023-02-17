package org.hzt.utils.iterators.functional_iterator;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@FunctionalInterface
@SuppressWarnings("squid:S1711")
public interface AtomicIterator<T> {

    static <T> AtomicIterator<T> of(Iterator<T> iterator) {
        return action -> {
            var hasNext = iterator.hasNext();
            if (hasNext) {
                action.accept(iterator.next());
            }
            return hasNext;
        };
    }

    static <T> AtomicIterator<T> of(Spliterator<T> spliterator) {
        return spliterator::tryAdvance;
    }

    boolean tryAdvance(Consumer<? super T> action);

    default void forEachRemaining(Consumer<? super T> action) {
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
    default @NotNull Iterator<T> asIterator() {
        return new Iterator<>() {

            private final AtomicReference<T> sink = new AtomicReference<>();
            private final AtomicBoolean hasNext = new AtomicBoolean(false);

            @Override
            public boolean hasNext() {
                final var hasNextVal = tryAdvance(sink::set);
                this.hasNext.set(hasNextVal);
                return hasNextVal;
            }

            @Override
            public T next() {
                if (hasNext.getAndSet(false)) {
                    return sink.getAndSet(null);
                }
                throw new NoSuchElementException();
            }
        };
    }

    default Spliterator<T> asSpliterator() {
        return Spliterators.spliteratorUnknownSize(asIterator(), 0);
    }

}
