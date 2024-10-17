package org.hzt.utils.iterables;

import org.hzt.utils.iterators.Iterators;

import java.util.stream.Gatherer;

/**
 * An interface to unify gatherer functionality
 *
 * @param <T> The type of the elements processed by the gatherable
 */
@FunctionalInterface
public interface Gatherable<T> extends Collectable<T> {

    default <R> Gatherable<R> gather(final Gatherer<? super T, ?, R> gatherer) {
        return () -> Iterators.gatheringIterator(iterator(), gatherer);
    }
}
