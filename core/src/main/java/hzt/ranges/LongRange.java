package hzt.ranges;

import hzt.numbers.LongX;
import hzt.sequences.Sequence;
import hzt.statistics.LongStatistics;
import hzt.utils.It;

import java.util.function.LongPredicate;
import java.util.stream.LongStream;

public interface LongRange extends Sequence<Long> {

    static LongRange empty() {
        return LongRange.of(Sequence.empty());
    }

    static LongRange of(Iterable<Long> longIterable) {
        return longIterable::iterator;
    }

    static LongRange of(LongStream longStream) {
        return longStream::iterator;
    }

    static LongRange of(long start, long end) {
        return of(start, end, 1);
    }

    static LongRange of(long start, long end, long step) {
        return LongRange.of(Sequence.generate(start, i -> i + (start < end ? step : -step))
                .takeWhile(l -> start < end ? (l < end) : (l > end)));
    }

    static LongX from(long start) {
        return LongX.of(start);
    }

    static LongRange until(long end) {
        return of(0, end);
    }

    static LongRange until(long end, long step) {
        return of(0, end, step);
    }

    static LongRange closed(long endInclusive) {
        return closed(0, endInclusive);
    }

    static LongRange closed(long start, long endInclusive) {
        return LongRange.of(Sequence.generate(start, i -> i + (start < endInclusive ? 1 : -1))
                .take(Math.abs(endInclusive - start) + 1));
    }

    default LongRange step(long step) {
        return LongRange.of(filter(i -> i % step == 0));
    }

    default long min() {
        return min(It::noFilter);
    }

    default long min(LongPredicate predicate) {
        return filter(predicate::test).minOf(It::asLong);
    }

    default long max() {
        return max(It::noFilter);
    }

    default long max(LongPredicate predicate) {
        return filter(predicate::test).maxOf(It::asLong);
    }

    default double average() {
        return average(It::noFilter);
    }

    default double average(LongPredicate predicate) {
        return filter(predicate::test).averageOfLongs(It::asLong);
    }

    default long sum() {
        return sum(It::noFilter);
    }

    default long sum(LongPredicate predicate) {
        return filter(predicate::test).sumOfLongs(It::asLong);
    }

    default double stdDev() {
        return stdDev(It::noFilter);
    }

    default double stdDev(LongPredicate predicate) {
        return filter(predicate::test).statsOfLongs(It::asLong).getStandardDeviation();
    }

    default LongStatistics stats() {
        return stats(It::noFilter);
    }

    default LongStatistics stats(LongPredicate predicate) {
        return filter(predicate::test).statsOfLongs(It::asLong);
    }
}
