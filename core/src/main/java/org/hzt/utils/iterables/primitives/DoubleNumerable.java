package org.hzt.utils.iterables.primitives;

import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.statistics.DoubleStatistics;
import org.hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.DoublePredicate;

@FunctionalInterface
public interface DoubleNumerable extends DoubleIterable, PrimitiveNumerable<DoublePredicate> {

    @Override
    default long count() {
        return count(It::noDoubleFilter);
    }

    @Override
    default long count(@NotNull DoublePredicate predicate) {
        long count = 0;
        PrimitiveIterator.OfDouble iterator = this.iterator();
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

    default double min(DoublePredicate predicate) {
        return DoubleSequence.of(this).filter(predicate).min();
    }

    default double max() {
        return stats().getMax();
    }

    default double max(DoublePredicate predicate) {
        return DoubleSequence.of(this).filter(predicate).max();
    }

    default double average() {
        return stats().getAverage();
    }

    default double average(DoublePredicate predicate) {
        return DoubleSequence.of(this).filter(predicate).average();
    }

    default double sum() {
        double sum = 0;
        PrimitiveIterator.OfDouble iterator = iterator();
        while (iterator.hasNext()) {
            sum += iterator.nextDouble();
        }
        return sum;
    }

    default double sum(DoublePredicate predicate) {
        return DoubleSequence.of(this).filter(predicate).sum();
    }

    default double stdDev() {
        return stats().getStandardDeviation();
    }

    default double stdDev(DoublePredicate predicate) {
        return DoubleSequence.of(this).filter(predicate).stdDev();
    }

    default @NotNull DoubleStatistics stats() {
        DoubleStatistics doubleStatistics = new DoubleStatistics();
        PrimitiveIterator.OfDouble iterator = this.iterator();
        while (iterator.hasNext()) {
            doubleStatistics.accept(iterator.nextDouble());
        }
        return doubleStatistics;
    }

    default @NotNull DoubleStatistics stats(@NotNull DoublePredicate predicate) {
        return DoubleSequence.of(this).filter(predicate).stats();
    }

}
