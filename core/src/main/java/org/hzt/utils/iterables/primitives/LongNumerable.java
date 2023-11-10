package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.hzt.utils.statistics.LongStatistics;

import java.util.PrimitiveIterator;
import java.util.function.LongPredicate;

@FunctionalInterface
public interface LongNumerable extends PrimitiveIterable.OfLong, PrimitiveNumerable<LongPredicate> {

    @Override
    default long count() {
        return count(It::noLongFilter);
    }

    default long count(LongPredicate predicate) {
        long count = 0;
        PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextLong())) {
                count++;
            }
        }
        return count;
    }

    default long min() {
        return stats().getMin();
    }

    default long min(LongPredicate predicate) {
        return LongSequence.of(this).filter(predicate).min();
    }

    default long max() {
        return stats().getMax();
    }

    default long max(LongPredicate predicate) {
        return LongSequence.of(this).filter(predicate).max();
    }

    default double average() {
        return stats().getAverage();
    }

    default double average(LongPredicate predicate) {
        return LongSequence.of(this).filter(predicate).average();
    }

    default long sum() {
        long sum = 0;
        PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            sum += iterator.nextLong();
        }
        return sum;
    }

    default long sum(LongPredicate predicate) {
        return LongSequence.of(this).filter(predicate).sum();
    }

    default double stdDev() {
        return stats().getStandardDeviation();
    }

    default double stdDev(LongPredicate predicate) {
        return LongSequence.of(this).filter(predicate).stdDev();
    }

    default LongStatistics stats() {
        LongStatistics longStatistics = new LongStatistics();
        PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            longStatistics.accept(iterator.nextLong());
        }
        return longStatistics;
    }

    default LongStatistics stats(LongPredicate predicate) {
        return LongSequence.of(this).filter(predicate).stats();
    }

}
