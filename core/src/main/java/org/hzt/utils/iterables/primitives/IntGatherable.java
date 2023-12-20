package org.hzt.utils.iterables.primitives;

import org.hzt.utils.gatherers.Gatherer;
import org.hzt.utils.gatherers.primitives.IntGatherer;
import org.hzt.utils.iterables.Gatherable;
import org.hzt.utils.iterators.Iterators;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;

@FunctionalInterface
public interface IntGatherable extends IntCollectable {

    default <A, R> Gatherable<R> gather(Gatherer<Integer, A, R> gatherer) {
        if (gatherer instanceof IntGatherer<?, ?>) {
            return () -> PrimitiveIterators.intGatheringIterator(iterator(), (IntGatherer<A, R>) gatherer);
        }
        return () -> Iterators.gatheringIterator(iterator(), gatherer);
    }
}
