package org.hzt.utils.iterables.primitives;

import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.hzt.utils.tuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;
import java.util.function.IntPredicate;

@FunctionalInterface
public interface IntGroupable extends PrimitiveGroupable<Integer, IntMutableList, IntPredicate>, PrimitiveIterable.OfInt {

    @Override
    default MapX<Integer, IntMutableList> group() {
        final var iterator = iterator();
        final MutableMapX<Integer, IntMutableList> map = MutableMapX.empty();
        while (iterator.hasNext()) {
            final var nextInt = iterator.nextInt();
            map.computeIfAbsent(nextInt, key -> IntMutableList.empty()).add(nextInt);
        }
        return map;
    }

    default <K> MapX<K, IntMutableList> groupBy(@NotNull final IntFunction<? extends K> classifier) {
        final var iterator = iterator();
        final MutableMapX<K, IntMutableList> map = MutableMapX.empty();
        while (iterator.hasNext()) {
            final var nextInt = iterator.nextInt();
            map.computeIfAbsent(classifier.apply(nextInt), key -> IntMutableList.empty()).add(nextInt);
        }
        return map;
    }

    @Override
    default Pair<IntMutableList, IntMutableList> partition(@NotNull final IntPredicate predicate) {
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
