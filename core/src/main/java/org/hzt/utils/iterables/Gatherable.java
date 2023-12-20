package org.hzt.utils.iterables;

import org.hzt.utils.gatherers.Gatherer;
import org.hzt.utils.iterators.Iterators;

/**
 * An interface to unify gatherer functionality
 *
 * @param <T> The type of the elements processed by the gatherable
 */
@FunctionalInterface
public interface Gatherable<T> extends Collectable<T> {

    default <A, R> Gatherable<R> gather(final Gatherer<? super T, A, R> gatherer) {
        return () -> Iterators.gatheringIterator(iterator(), gatherer);
    }
}
