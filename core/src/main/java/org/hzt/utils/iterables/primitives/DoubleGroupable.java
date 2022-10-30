package org.hzt.utils.iterables.primitives;

import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collections.primitives.DoubleMutableList;
import org.hzt.utils.tuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;

@FunctionalInterface
public interface DoubleGroupable extends PrimitiveGroupable<Double, DoubleMutableList, DoublePredicate>, PrimitiveIterable.OfDouble {

    @Override
    default MapX<Double, DoubleMutableList> group() {
        final var iterator = iterator();
        final MutableMapX<Double, DoubleMutableList> map = MutableMapX.empty();
        while (iterator.hasNext()) {
            final var nextDouble = iterator.nextDouble();
            map.computeIfAbsent(nextDouble, key -> DoubleMutableList.empty()).add(nextDouble);
        }
        return map;
    }

    default <K> MapX<K, DoubleMutableList> groupBy(@NotNull DoubleFunction<? extends K> classifier) {
        final var iterator = iterator();
        final MutableMapX<K, DoubleMutableList> map = MutableMapX.empty();
        while (iterator.hasNext()) {
            final var nextDouble = iterator.nextDouble();
            map.computeIfAbsent(classifier.apply(nextDouble), key -> DoubleMutableList.empty()).add(nextDouble);
        }
        return map;
    }

    @Override
    default Pair<DoubleMutableList, DoubleMutableList> partition(@NotNull DoublePredicate predicate) {
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
