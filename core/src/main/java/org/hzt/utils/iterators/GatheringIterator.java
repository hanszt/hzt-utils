package org.hzt.utils.iterators;

import org.hzt.utils.gatherers.Gatherer;
import org.hzt.utils.gatherers.Integrator;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.function.BiConsumer;

/**
 * An iterator that converts the source iterator to a gathering iterator using a gatherer
 *
 * @param <T> The type of the source iterator
 * @param <A> The type of the state container
 * @param <R> The type of this iterator
 */
final class GatheringIterator<T, A, R> implements Iterator<R> {
    private final Iterator<T> source;
    private final A state;
    private final Integrator<A, ? super T, R> integrator;
    private final BiConsumer<A, Gatherer.Downstream<? super R>> finisher;
    private final Queue<R> buffer = new ArrayDeque<>();
    private boolean finisherCalled = false;
    private boolean emitNoMoreItems = false;
    private final Gatherer.Downstream<R> downstream = new Gatherer.Downstream<>() {

        @Override
        public boolean push(final R element) {
            return buffer.add(element);
        }

        @Override
        public boolean isRejecting() {
            return emitNoMoreItems;
        }
    };


    GatheringIterator(final Iterator<T> iterator, final Gatherer<? super T, A, R> gatherer) {
        source = iterator;
        state = gatherer.initializer().get();
        integrator = gatherer.integrator();
        finisher = gatherer.finisher();
    }

    @Override
    public boolean hasNext() {
        if (!buffer.isEmpty()) {
            return true;
        }
        if (emitNoMoreItems) {
            return false;
        }
        while (buffer.isEmpty() && source.hasNext()) {
            if (!integrator.integrate(state, source.next(), downstream)) {
                emitNoMoreItems = true;
                break;
            }
        }
        if (!source.hasNext() && !finisherCalled) {
            finisher.accept(state, downstream);
            finisherCalled = true;
        }
        return !buffer.isEmpty();
    }

    @Override
    public R next() {
        if (hasNext()) {
            return buffer.remove();
        }
        throw new NoSuchElementException();
    }
}
