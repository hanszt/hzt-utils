package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collections.primitives.LongList;
import org.hzt.utils.collections.primitives.LongMutableList;
import org.hzt.utils.tuples.Pair;

import java.util.function.LongFunction;
import java.util.function.LongPredicate;

@FunctionalInterface
public interface LongGroupable extends PrimitiveGroupable<Long, LongList, LongPredicate>, PrimitiveIterable.OfLong {

    @Override
    default MapX<Long, LongList> group() {
        return groupBy(It::self);
    }

    default <K> MapX<K, LongList> groupBy(final LongFunction<? extends K> classifier) {
        final var iterator = iterator();
        final MutableMapX<K, LongList> map = MutableMapX.empty();
        while (iterator.hasNext()) {
            final var next = iterator.nextLong();
            final var items = map.computeIfAbsent(classifier.apply(next), _ -> LongMutableList.empty());
            ((LongMutableList) items).add(next);
        }
        return map;
    }

    @Override
    default Pair<LongList, LongList> partition(final LongPredicate predicate) {
        final var matchingList = LongMutableList.empty();
        final var nonMatchingList = LongMutableList.empty();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var nextLong = iterator.nextLong();
            if (predicate.test(nextLong)) {
                matchingList.add(nextLong);
            } else {
                nonMatchingList.add(nextLong);
            }
        }
        return Pair.of(matchingList, nonMatchingList);
    }
}
