package org.hzt.utils.iterables.primitives;

import org.hzt.utils.gatherers.primitives.IntGatherer;
import org.hzt.utils.iterables.Gatherable;
import org.hzt.utils.iterators.Iterators;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;

import java.util.stream.Gatherer;

@FunctionalInterface
public interface IntGatherable extends IntCollectable {

    default <A, R> Gatherable<R> gatherToObj(final Gatherer<Integer, A, R> gatherer) {
        if (gatherer instanceof IntGatherer<A, R> g) {
            return () -> PrimitiveIterators.intGatheringIterator(iterator(), g);
        }
        return () -> Iterators.gatheringIterator(iterator(), gatherer);
    }
}
