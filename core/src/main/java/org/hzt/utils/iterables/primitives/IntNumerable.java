package org.hzt.utils.iterables.primitives;

import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.statistics.IntStatistics;
import org.hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.IntPredicate;

@FunctionalInterface
public interface IntNumerable extends IntIterable, PrimitiveNumerable<IntPredicate> {

    @Override
    default long count() {
        return count(It::noIntFilter);
    }

    default long count(@NotNull IntPredicate predicate) {
        long count = 0;
        PrimitiveIterator.OfInt iterator = this.iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextInt())) {
                count++;
            }
        }
        return count;
    }

    default int min() {
        return stats().getMin();
    }

    default int min(IntPredicate predicate) {
        return IntSequence.of(this).filter(predicate).min();
    }

    default int max() {
        return stats().getMax();
    }

    default int max(IntPredicate predicate) {
        return IntSequence.of(this).filter(predicate).max();
    }

    default double average() {
        return stats().getAverage();
    }

    default double average(IntPredicate predicate) {
        return IntSequence.of(this).filter(predicate).average();
    }

    default long sum() {
        long sum = 0;
        PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            sum += iterator.nextInt();
        }
        return sum;
    }

    default long sum(IntPredicate predicate) {
        return IntSequence.of(this).filter(predicate).sum();
    }

    default double stdDev() {
        return stats().getStandardDeviation();
    }

    default double stdDev(IntPredicate predicate) {
        return IntSequence.of(this).filter(predicate).stdDev();
    }

    default @NotNull IntStatistics stats() {
        IntStatistics intStatistics = new IntStatistics();
        PrimitiveIterator.OfInt iterator = this.iterator();
        while (iterator.hasNext()) {
            int i = iterator.nextInt();
            intStatistics.accept(i);
        }
        return intStatistics;
    }

    default @NotNull IntStatistics stats(@NotNull IntPredicate predicate) {
        return IntSequence.of(this).filter(predicate).stats();
    }

}
