package hzt.ranges;

import hzt.numbers.LongX;
import hzt.sequences.Sequence;
import hzt.statistics.LongStatistics;
import hzt.utils.It;
import hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.LongPredicate;
import java.util.stream.LongStream;

public interface LongRange extends NumberRange<Long>, Sequence<Long>, Transformable<LongRange> {

    static LongRange empty() {
        return LongRange.of(Sequence.empty());
    }

    static LongRange of(Iterable<Long> longIterable) {
        return RangeHelper.toLongRange(longIterable::iterator);
    }

    static LongRange of(LongStream longStream) {
        return RangeHelper.toLongRange(longStream::iterator);
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

    default @NotNull Long min() {
        return min(It::noFilter);
    }

    default long min(LongPredicate predicate) {
        return filter(predicate::test).minOf(It::asLong);
    }

    default @NotNull Long max() {
        return max(It::noFilter);
    }

    default long max(LongPredicate predicate) {
        return filter(predicate::test).maxOf(It::asLong);
    }

    default @NotNull Double average() {
        return average(It::noFilter);
    }

    default double average(LongPredicate predicate) {
        return filter(predicate::test).averageOfLongs(It::asLong);
    }

    default @NotNull Long sum() {
        return sum(It::noFilter);
    }

    default long sum(LongPredicate predicate) {
        return filter(predicate::test).sumOfLongs(It::asLong);
    }

    default @NotNull Double stdDev() {
        return stdDev(It::noFilter);
    }

    default double stdDev(LongPredicate predicate) {
        return filter(predicate::test).statsOfLongs(It::asLong).getStandardDeviation();
    }

    default @NotNull LongStatistics stats() {
        return stats(It::noFilter);
    }

    @Override
    @NotNull
    default LongRange onEach(@NotNull Consumer<? super Long> consumer) {
        return LongRange.of(Sequence.super.onEach(consumer));
    }

    default LongStatistics stats(LongPredicate predicate) {
        return filter(predicate::test).statsOfLongs(It::asLong);
    }
}
