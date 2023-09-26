package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.hzt.utils.statistics.LongStatistics;
import org.jetbrains.annotations.NotNull;

import java.util.function.LongPredicate;

@FunctionalInterface
public interface LongNumerable extends PrimitiveIterable.OfLong, PrimitiveNumerable<LongPredicate> {

    @Override
    default long count() {
        return count(It::noLongFilter);
    }

    default long count(@NotNull final LongPredicate predicate) {
        long count = 0;
        final var iterator = iterator();
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

    default long min(final LongPredicate predicate) {
        return LongSequence.of(this).filter(predicate).min();
    }

    default long max() {
        return stats().getMax();
    }

    default long max(final LongPredicate predicate) {
        return LongSequence.of(this).filter(predicate).max();
    }

    default double average() {
        return stats().getAverage();
    }

    default double average(final LongPredicate predicate) {
        return LongSequence.of(this).filter(predicate).average();
    }

    default long sum() {
        long sum = 0;
        final var iterator = iterator();
        while (iterator.hasNext()) {
            sum += iterator.nextLong();
        }
        return sum;
    }

    default long sum(final LongPredicate predicate) {
        return LongSequence.of(this).filter(predicate).sum();
    }

    default double stdDev() {
        return stats().getStandardDeviation();
    }

    default double stdDev(final LongPredicate predicate) {
        return LongSequence.of(this).filter(predicate).stdDev();
    }

    default @NotNull LongStatistics stats() {
        final var longStatistics = new LongStatistics();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            longStatistics.accept(iterator.nextLong());
        }
        return longStatistics;
    }

    default @NotNull LongStatistics stats(@NotNull final LongPredicate predicate) {
        return LongSequence.of(this).filter(predicate).stats();
    }

}
