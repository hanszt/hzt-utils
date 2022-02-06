package hzt.ranges;

import hzt.numbers.IntX;
import hzt.sequences.Sequence;
import hzt.statistics.IntStatistics;
import hzt.utils.It;

import java.util.Optional;
import java.util.Random;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

public interface IntRange extends Sequence<Integer> {

    static IntRange empty() {
        return IntRange.of(Sequence.empty());
    }

    static IntRange of(Iterable<Integer> integers) {
        return integers::iterator;
    }

    static IntRange of (int start, int end) {
        return of(start, end, 1);
    }

    static IntRange of(int start, int end, int step) {
        return IntRange.of(Sequence.generate(start, i -> i + (start < end ? step : -step)).take(Math.abs(end - start)));
    }

    static IntX from(int start) {
        return IntX.of(start);
    }

    static IntRange until(int end) {
        return of(0, end);
    }

    static IntRange closed(int endInclusive) {
        return closed(0, endInclusive);
    }

    static IntRange closed(int start, int endInclusive) {
        return IntRange.of(Sequence.generate(start, i -> i + (start < endInclusive ? 1 : -1))
                .take(Math.abs(endInclusive - start) + 1L));
    }

    default IntRange step(int step) {
        return IntRange.of(filter(i -> i % step == 0));
    }

    default int random() {
        return toListX().random();
    }

    default Optional<Integer> findRandom() {
        return toListX().findRandom();
    }

    default int random(Random random) {
        return toListX().random(random);
    }

    default Optional<Integer> findRandom(Random random) {
        return toListX().findRandom(random);
    }

    default int min() {
        return min(It::noFilter);
    }

    default int min(IntPredicate predicate) {
        return filter(predicate::test).minOf(It::asInt);
    }

    default int max() {
        return max(It::noFilter);
    }

    default int max(IntPredicate predicate) {
        return filter(predicate::test).maxOf(It::asInt);
    }

    default double average() {
        return average(It::noFilter);
    }

    default double average(IntPredicate predicate) {
        return filter(predicate::test).averageOfLongs(It::asLong);
    }

    default long sum() {
        return sum(It::noFilter);
    }

    default long sum(IntPredicate predicate) {
        return filter(predicate::test).sumOfInts(It::asInt);
    }

    default double stdDev() {
        return stdDev(It::noFilter);
    }

    default double stdDev(LongPredicate predicate) {
        return filter(predicate::test).statsOfLongs(It::asLong).getStandardDeviation();
    }

    default IntStatistics stats() {
        return stats(It::noFilter);
    }

    default IntStatistics stats(IntPredicate predicate) {
        return filter(predicate::test).statsOfInts(It::asInt);
    }
}
