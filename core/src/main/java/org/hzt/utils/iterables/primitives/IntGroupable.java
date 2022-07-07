package org.hzt.utils.iterables.primitives;

import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collections.primitives.IntMutableListX;
import org.hzt.utils.tuples.Pair;

import java.util.PrimitiveIterator;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

@FunctionalInterface
public interface IntGroupable extends PrimitiveGroupable<Integer, IntMutableListX, IntPredicate>, PrimitiveIterable.OfInt {

    @Override
    default MapX<Integer, IntMutableListX> group() {
        final PrimitiveIterator.OfInt iterator = iterator();
        final MutableMapX<Integer, IntMutableListX> map = MutableMapX.empty();
        while (iterator.hasNext()) {
            final int nextInt = iterator.nextInt();
            map.computeIfAbsent(nextInt, key -> IntMutableListX.empty()).add(nextInt);
        }
        return map;
    }

    default <K> MapX<K, IntMutableListX> groupBy(IntFunction<K> classifier) {
        final PrimitiveIterator.OfInt iterator = iterator();
        final MutableMapX<K, IntMutableListX> map = MutableMapX.empty();
        while (iterator.hasNext()) {
            final int nextInt = iterator.nextInt();
            map.computeIfAbsent(classifier.apply(nextInt), key -> IntMutableListX.empty()).add(nextInt);
        }
        return map;
    }

    @Override
    default Pair<IntMutableListX, IntMutableListX> partition(IntPredicate predicate) {
        final IntMutableListX matchingList = IntMutableListX.empty();
        final IntMutableListX nonMatchingList = IntMutableListX.empty();
        final PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            final int nextInt = iterator.nextInt();
            if (predicate.test(nextInt)) {
                matchingList.add(nextInt);
            } else {
                nonMatchingList.add(nextInt);
            }
        }
        return Pair.of(matchingList, nonMatchingList);
    }
}
