package org.hzt.utils.iterables.primitives;

import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collections.primitives.DoubleMutableListX;
import org.hzt.utils.tuples.Pair;

import java.util.PrimitiveIterator;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;

@FunctionalInterface
public interface DoubleGroupable extends PrimitiveGroupable<Double, DoubleMutableListX, DoublePredicate>, DoubleIterable {

    @Override
    default MapX<Double, DoubleMutableListX> group() {
        final PrimitiveIterator.OfDouble iterator = iterator();
        final MutableMapX<Double, DoubleMutableListX> map = MutableMapX.empty();
        while (iterator.hasNext()) {
            final double nextDouble = iterator.nextDouble();
            map.computeIfAbsent(nextDouble, key -> DoubleMutableListX.empty()).add(nextDouble);
        }
        return map;
    }

    default <K> MapX<K, DoubleMutableListX> groupBy(DoubleFunction<K> classifier) {
        final PrimitiveIterator.OfDouble iterator = iterator();
        final MutableMapX<K, DoubleMutableListX> map = MutableMapX.empty();
        while (iterator.hasNext()) {
            final double nextDouble = iterator.nextDouble();
            map.computeIfAbsent(classifier.apply(nextDouble), key -> DoubleMutableListX.empty()).add(nextDouble);
        }
        return map;
    }

    @Override
    default Pair<DoubleMutableListX, DoubleMutableListX> partition(DoublePredicate predicate) {
        final DoubleMutableListX matchingList = DoubleMutableListX.empty();
        final DoubleMutableListX nonMatchingList = DoubleMutableListX.empty();
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
