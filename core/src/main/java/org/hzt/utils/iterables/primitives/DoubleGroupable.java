package org.hzt.utils.iterables.primitives;

import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collections.primitives.DoubleMutableList;
import org.hzt.utils.tuples.Pair;

import java.util.PrimitiveIterator;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;

@FunctionalInterface
public interface DoubleGroupable extends PrimitiveGroupable<Double, DoubleMutableList, DoublePredicate>, PrimitiveIterable.OfDouble {

    @Override
    default MapX<Double, DoubleMutableList> group() {
        final PrimitiveIterator.OfDouble iterator = iterator();
        final MutableMapX<Double, DoubleMutableList> map = MutableMapX.empty();
        while (iterator.hasNext()) {
            final double nextDouble = iterator.nextDouble();
            map.computeIfAbsent(nextDouble, key -> DoubleMutableList.empty()).add(nextDouble);
        }
        return map;
    }

    default <K> MapX<K, DoubleMutableList> groupBy(DoubleFunction<K> classifier) {
        final PrimitiveIterator.OfDouble iterator = iterator();
        final MutableMapX<K, DoubleMutableList> map = MutableMapX.empty();
        while (iterator.hasNext()) {
            final double nextDouble = iterator.nextDouble();
            map.computeIfAbsent(classifier.apply(nextDouble), key -> DoubleMutableList.empty()).add(nextDouble);
        }
        return map;
    }

    @Override
    default Pair<DoubleMutableList, DoubleMutableList> partition(DoublePredicate predicate) {
        final DoubleMutableList matchingList = DoubleMutableList.empty();
        final DoubleMutableList nonMatchingList = DoubleMutableList.empty();
        final PrimitiveIterator.OfDouble iterator = iterator();
        while (iterator.hasNext()) {
            final double nextDouble = iterator.nextDouble();
            if (predicate.test(nextDouble)) {
                matchingList.add(nextDouble);
            } else {
                nonMatchingList.add(nextDouble);
            }
        }
        return Pair.of(matchingList, nonMatchingList);
    }
}
