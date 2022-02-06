package hzt.ranges;

import hzt.numbers.DoubleX;
import hzt.sequences.Sequence;
import hzt.statistics.DoubleStatistics;
import hzt.utils.It;

import java.util.function.DoublePredicate;

public interface DoubleRange extends Sequence<Double>    {

    static DoubleRange empty() {
        return DoubleRange.of(Sequence.empty());
    }

    static DoubleRange of(Iterable<Double> doubleIterable) {
        return doubleIterable::iterator;
    }

    static DoubleRange of(double start, double end) {
        return of(start, end, 1);
    }

    static DoubleRange of(double start, double end, double step) {
        return DoubleRange.of(Sequence.generate(start, d -> d + (start < end ? step : -step))
                .takeWhile(d -> start < end ? (d < end) : (d > end)));
    }

    static DoubleX from(double start) {
        return DoubleX.of(start);
    }

    static DoubleRange closed(double endInclusive) {
        return closed(0, endInclusive);
    }

    static DoubleRange closed(double start, double endInclusive) {
        return closed(start, endInclusive, 1);
    }

    static DoubleRange closed(double start, double endInclusive, double step) {
        return DoubleRange.of(Sequence.generate(start, d -> d + (start < endInclusive ? step : -step))
                .takeWhile(d -> start < endInclusive ? (d <= endInclusive) : (d >= endInclusive)));
    }

    default double min() {
        return min(It::noFilter);
    }

    default double min(DoublePredicate predicate) {
        return filter(predicate::test).minOf(It::asDouble);
    }

    default double max() {
        return max(It::noFilter);
    }

    default double max(DoublePredicate predicate) {
        return filter(predicate::test).maxOf(It::asDouble);
    }

    default double average() {
        return average(It::noFilter);
    }

    default double average(DoublePredicate predicate) {
        return filter(predicate::test).averageOfDoubles(It::asDouble);
    }

    default double sum() {
        return sum(It::noFilter);
    }

    default double sum(DoublePredicate predicate) {
        return filter(predicate::test).sumOfDoubles(It::asDouble);
    }

    default double stdDev() {
        return stdDev(It::noFilter);
    }

    default double stdDev(DoublePredicate predicate) {
        return filter(predicate::test).statsOfDoubles(It::asDouble).getStandardDeviation();
    }

    default DoubleStatistics stats() {
        return stats(It::noFilter);
    }

    default DoubleStatistics stats(DoublePredicate doublePredicate) {
        return filter(doublePredicate::test).statsOfDoubles(It::asDouble);
    }
}
