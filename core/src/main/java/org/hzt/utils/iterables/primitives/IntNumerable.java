package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.statistics.IntStatistics;

import java.util.PrimitiveIterator;
import java.util.function.IntPredicate;

@FunctionalInterface
public interface IntNumerable extends PrimitiveIterable.OfInt, PrimitiveNumerable<IntPredicate> {

    @Override
    default long count() {
        return count(It::noIntFilter);
    }

    default long count(final IntPredicate predicate) {
        long count = 0;
        final PrimitiveIterator.OfInt iterator = this.iterator();
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

    default int min(final IntPredicate predicate) {
        return IntSequence.of(this).filter(predicate).min();
    }

    default int max() {
        return stats().getMax();
    }

    default int max(final IntPredicate predicate) {
        return IntSequence.of(this).filter(predicate).max();
    }

    default double average() {
        return stats().getAverage();
    }

    default double average(final IntPredicate predicate) {
        return IntSequence.of(this).filter(predicate).average();
    }

    default long sum() {
        long sum = 0;
        final PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            sum += iterator.nextInt();
        }
        return sum;
    }

    default long sum(final IntPredicate predicate) {
        return IntSequence.of(this).filter(predicate).sum();
    }

    default double stdDev() {
        return stats().getStandardDeviation();
    }

    default double stdDev(final IntPredicate predicate) {
        return IntSequence.of(this).filter(predicate).stdDev();
    }

    default IntStatistics stats() {
        final IntStatistics intStatistics = new IntStatistics();
        final PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            intStatistics.accept(iterator.nextInt());
        }
        return intStatistics;
    }

    default IntStatistics stats(final IntPredicate predicate) {
        return IntSequence.of(this).filter(predicate).stats();
    }

}
