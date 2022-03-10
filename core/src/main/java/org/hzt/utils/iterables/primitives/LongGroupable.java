package org.hzt.utils.iterables.primitives;

import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collections.primitives.LongMutableListX;
import org.hzt.utils.tuples.Pair;

import java.util.PrimitiveIterator;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;

@FunctionalInterface
public interface LongGroupable extends PrimitiveGroupable<Long, LongMutableListX, LongPredicate>, LongIterable {

    @Override
    default MapX<Long, LongMutableListX> group() {
        final PrimitiveIterator.OfLong iterator = iterator();
        final MutableMapX<Long, LongMutableListX> map = MutableMapX.empty();
        while (iterator.hasNext()) {
            final long nextLong = iterator.nextLong();
            map.computeIfAbsent(nextLong, key -> LongMutableListX.empty()).add(nextLong);
        }
        return map;
    }

    default <K> MapX<K, LongMutableListX> groupBy(LongFunction<K> classifier) {
        final PrimitiveIterator.OfLong iterator = iterator();
        final MutableMapX<K, LongMutableListX> map = MutableMapX.empty();
        while (iterator.hasNext()) {
            final long nextLong = iterator.nextLong();
            map.computeIfAbsent(classifier.apply(nextLong), key -> LongMutableListX.empty()).add(nextLong);
        }
        return map;
    }

    @Override
    default Pair<LongMutableListX, LongMutableListX> partition(LongPredicate predicate) {
        final LongMutableListX matchingList = LongMutableListX.empty();
        final LongMutableListX nonMatchingList = LongMutableListX.empty();
        final PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            final long nextLong = iterator.nextLong();
            if (predicate.test(nextLong)) {
                matchingList.add(nextLong);
            } else {
                nonMatchingList.add(nextLong);
            }
        }
        return Pair.of(matchingList, nonMatchingList);
    }
}
