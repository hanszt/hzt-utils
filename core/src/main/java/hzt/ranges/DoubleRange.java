package hzt.ranges;

import hzt.collections.ArrayX;
import hzt.numbers.DoubleX;
import hzt.sequences.Sequence;
import hzt.statistics.DoubleStatistics;
import hzt.utils.It;
import hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.DoublePredicate;

public interface DoubleRange extends NumberRange<Double>, Sequence<Double>, Transformable<DoubleRange> {

    static DoubleRange empty() {
        return DoubleRange.of(Sequence.empty());
    }

    static DoubleRange of(Iterable<Double> doubleIterable) {
        return new DoubleRange() {
            @NotNull
            @Override
            public Iterator<Double> iterator() {
                return doubleIterable.iterator();
            }

            @NotNull
            @Override
            public DoubleRange get() {
                return this;
            }
        };
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

    default @NotNull Double min() {
        return min(It::noFilter);
    }

    default Double min(DoublePredicate predicate) {
        return filter(predicate::test).minOf(It::asDouble);
    }

    default @NotNull Double max() {
        return max(It::noFilter);
    }

    default double max(DoublePredicate predicate) {
        return filter(predicate::test).maxOf(It::asDouble);
    }

    default @NotNull Double average() {
        return average(It::noFilter);
    }

    default double average(DoublePredicate predicate) {
        return filter(predicate::test).averageOfDoubles(It::asDouble);
    }

    default @NotNull Double sum() {
        return sum(It::noFilter);
    }

    default double sum(DoublePredicate predicate) {
        return filter(predicate::test).sumOfDoubles(It::asDouble);
    }

    default @NotNull Double stdDev() {
        return stdDev(It::noFilter);
    }

    default double stdDev(DoublePredicate predicate) {
        return filter(predicate::test).statsOfDoubles(It::asDouble).getStandardDeviation();
    }

    default @NotNull DoubleStatistics stats() {
        return stats(It::noFilter);
    }

    @Override
    @NotNull
    default DoubleRange onEach(@NotNull Consumer<? super Double> consumer) {
        return DoubleRange.of(Sequence.super.onEach(consumer));
    }

    @Override
    default @NotNull ArrayX<Double> toArrayX() {
        return toArrayX(Double[]::new);
    }

    @Override
    @NotNull
    default Double[] toArray() {
        return toArray(Double[]::new);
    }

    default double[] toDoubleArray() {
        return stream().mapToDouble(It::asDouble).toArray();
    }

    default DoubleStatistics stats(DoublePredicate doublePredicate) {
        return filter(doublePredicate::test).statsOfDoubles(It::asDouble);
    }
}
