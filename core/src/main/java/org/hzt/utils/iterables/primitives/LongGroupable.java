package org.hzt.utils.iterables.primitives;

import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collections.primitives.LongMutableList;
import org.hzt.utils.tuples.Pair;

import java.util.PrimitiveIterator;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;

@FunctionalInterface
public interface LongGroupable extends PrimitiveGroupable<Long, LongMutableList, LongPredicate>, PrimitiveIterable.OfLong {

    @Override
    default MapX<Long, LongMutableList> group() {
        final PrimitiveIterator.OfLong iterator = iterator();
        final MutableMapX<Long, LongMutableList> map = MutableMapX.empty();
        while (iterator.hasNext()) {
            final long nextLong = iterator.nextLong();
            map.computeIfAbsent(nextLong, key -> LongMutableList.empty()).add(nextLong);
        }
        return map;
    }

    default <K> MapX<K, LongMutableList> groupBy(LongFunction<K> classifier) {
        final PrimitiveIterator.OfLong iterator = iterator();
        final MutableMapX<K, LongMutableList> map = MutableMapX.empty();
        while (iterator.hasNext()) {
            final long nextLong = iterator.nextLong();
            map.computeIfAbsent(classifier.apply(nextLong), key -> LongMutableList.empty()).add(nextLong);
        }
        return map;
    }

    @Override
    default Pair<LongMutableList, LongMutableList> partition(LongPredicate predicate) {
        final LongMutableListX matchingList = LongMutableList.empty();
        final LongMutableListX nonMatchingList = LongMutableList.empty();
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
