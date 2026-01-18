package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.hzt.utils.tuples.Pair;

import java.util.function.IntFunction;
import java.util.function.IntPredicate;

@FunctionalInterface
public interface IntGroupable extends PrimitiveGroupable<Integer, IntList, IntPredicate>, PrimitiveIterable.OfInt {

    @Override
    default MapX<Integer, IntList> group() {
        return groupBy(It::self);
    }

    default <K> MapX<K, IntList> groupBy(final IntFunction<? extends K> classifier) {
        final var iterator = iterator();
        final MutableMapX<K, IntList> map = MutableMapX.empty();
        while (iterator.hasNext()) {
            final var next = iterator.nextInt();
            final var items = map.computeIfAbsent(classifier.apply(next), _ -> IntMutableList.empty());
            ((IntMutableList) items).add(next);
        }
        return map;
    }

    @Override
    default Pair<IntList, IntList> partition(final IntPredicate predicate) {
        final var matchingList = IntMutableList.empty();
        final var nonMatchingList = IntMutableList.empty();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var nextInt = iterator.nextInt();
            if (predicate.test(nextInt)) {
                matchingList.add(nextInt);
            } else {
                nonMatchingList.add(nextInt);
            }
        }
        return Pair.of(matchingList, nonMatchingList);
    }
}
