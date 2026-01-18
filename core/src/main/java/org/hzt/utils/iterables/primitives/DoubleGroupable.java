package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collections.primitives.DoubleList;
import org.hzt.utils.collections.primitives.DoubleMutableList;
import org.hzt.utils.tuples.Pair;

import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;

@FunctionalInterface
public interface DoubleGroupable extends PrimitiveGroupable<Double, DoubleList, DoublePredicate>, PrimitiveIterable.OfDouble {

    @Override
    default MapX<Double, DoubleList> group() {
        return groupBy(It::self);
    }

    default <K> MapX<K, DoubleList> groupBy(final DoubleFunction<? extends K> classifier) {
        final var iterator = iterator();
        final MutableMapX<K, DoubleList> map = MutableMapX.empty();
        while (iterator.hasNext()) {
            final var next = iterator.nextDouble();
            final var items = map.computeIfAbsent(classifier.apply(next), _ -> DoubleMutableList.empty());
            ((DoubleMutableList) items).add(next);
        }
        return map;
    }

    @Override
    default Pair<DoubleList, DoubleList> partition(final DoublePredicate predicate) {
        final var matchingList = DoubleMutableList.empty();
        final var nonMatchingList = DoubleMutableList.empty();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var nextDouble = iterator.nextDouble();
            if (predicate.test(nextDouble)) {
                matchingList.add(nextDouble);
            } else {
                nonMatchingList.add(nextDouble);
            }
        }
        return Pair.of(matchingList, nonMatchingList);
    }
}
