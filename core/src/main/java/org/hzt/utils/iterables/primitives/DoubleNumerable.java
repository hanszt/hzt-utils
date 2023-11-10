package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.statistics.DoubleStatistics;

import java.util.PrimitiveIterator;
import java.util.function.DoublePredicate;

@FunctionalInterface
public interface DoubleNumerable extends PrimitiveIterable.OfDouble, PrimitiveNumerable<DoublePredicate> {

    @Override
    default long count() {
        return count(It::noDoubleFilter);
    }

    @Override
    default long count(final DoublePredicate predicate) {
        long count = 0;
        final PrimitiveIterator.OfDouble iterator = this.iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextDouble())) {
                count++;
            }
        }
        return count;
    }

    default double min() {
        return stats().getMin();
    }

    default double min(final DoublePredicate predicate) {
        return DoubleSequence.of(this).filter(predicate).min();
    }

    default double max() {
        return stats().getMax();
    }

    default double max(final DoublePredicate predicate) {
        return DoubleSequence.of(this).filter(predicate).max();
    }

    default double average() {
        return stats().getAverage();
    }

    default double average(final DoublePredicate predicate) {
        return DoubleSequence.of(this).filter(predicate).average();
    }

    default double sum() {
        double sum = 0;
        final PrimitiveIterator.OfDouble iterator = iterator();
        while (iterator.hasNext()) {
            sum += iterator.nextDouble();
        }
        return sum;
    }

    default double sum(final DoublePredicate predicate) {
        return DoubleSequence.of(this).filter(predicate).sum();
    }

    default double stdDev() {
        return stats().getStandardDeviation();
    }

    default double stdDev(final DoublePredicate predicate) {
        return DoubleSequence.of(this).filter(predicate).stdDev();
    }

    default DoubleStatistics stats() {
        final DoubleStatistics doubleStatistics = new DoubleStatistics();
        final PrimitiveIterator.OfDouble iterator = this.iterator();
        while (iterator.hasNext()) {
            doubleStatistics.accept(iterator.nextDouble());
        }
        return doubleStatistics;
    }

    default DoubleStatistics stats(final DoublePredicate predicate) {
        return DoubleSequence.of(this).filter(predicate).stats();
    }

}
