package hzt.ranges;

import hzt.collections.ArrayX;
import hzt.function.TriFunction;
import hzt.numbers.DoubleX;
import hzt.sequences.Sequence;
import hzt.statistics.DoubleStatistics;
import hzt.tuples.Pair;
import hzt.tuples.Triple;
import hzt.utils.It;
import hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;

@FunctionalInterface
public interface DoubleRange extends NumberRange<Double>, Sequence<Double>, Transformable<DoubleRange> {

    static DoubleRange empty() {
        return DoubleRange.of(Sequence.empty());
    }

    static DoubleRange of(Iterable<Double> doubleIterable) {
        return doubleIterable::iterator;
    }

    static DoubleRange of (DoubleStream stream) {
        return stream::iterator;
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
                .takeWhile(d -> (start < endInclusive) == (d < endInclusive)));
    }

    default @NotNull Double min() {
        return min(It::noFilter);
    }

    default @NotNull Double min(Predicate<Double> predicate) {
        return filter(predicate).minOf(It::asDouble);
    }

    default @NotNull Double max() {
        return max(It::noFilter);
    }

    default @NotNull Double max(Predicate<Double> predicate) {
        return filter(predicate).maxOf(It::asDouble);
    }

    default @NotNull Double average() {
        return average(It::noFilter);
    }

    default @NotNull Double average(Predicate<Double> predicate) {
        return filter(predicate).averageOf(It::asDouble);
    }

    default @NotNull Double sum() {
        return sum(It::noFilter);
    }

    default @NotNull Double sum(Predicate<Double> predicate) {
        return filter(predicate).sumOfDoubles(It::asDouble);
    }

    default @NotNull Double stdDev() {
        return stdDev(It::noFilter);
    }

    default @NotNull Double stdDev(Predicate<Double> predicate) {
        return filter(predicate).statsOfDoubles(It::asDouble).getStandardDeviation();
    }

    default @NotNull DoubleStatistics stats() {
        return stats(It::noFilter);
    }

    @Override
    default @NotNull DoubleRange filter(@NotNull Predicate<Double> predicate) {
        return DoubleRange.of(Sequence.super.filter(predicate));
    }

    @Override
    @NotNull
    default DoubleRange onEach(@NotNull Consumer<? super Double> consumer) {
        return DoubleRange.of(Sequence.super.onEach(consumer));
    }

    default DoubleStream doubleStream() {
        return stream().mapToDouble(It::asDouble);
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

    @Override
    default @NotNull DoubleStatistics stats(Predicate<Double> doublePredicate) {
        return filter(doublePredicate).statsOfDoubles(It::asDouble);
    }

    default <R1, R2, R> R doublesToTwo(@NotNull Function<? super DoubleRange, ? extends R1> resultMapper1,
                                    @NotNull Function<? super DoubleRange, ? extends R2> resultMapper2,
                                    @NotNull BiFunction<R1, R2, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this));
    }

    default <R1, R2> Pair<R1, R2> doublesToTwo(@NotNull Function<? super DoubleRange, ? extends R1> resultMapper1,
                                            @NotNull Function<? super DoubleRange, ? extends R2> resultMapper2) {
        return doublesToTwo(resultMapper1, resultMapper2, Pair::of);
    }

    default <R1, R2, R3, R> R doublesToThree(@NotNull Function<? super DoubleRange, ? extends R1> resultMapper1,
                                          @NotNull Function<? super DoubleRange, ? extends R2> resultMapper2,
                                          @NotNull Function<? super DoubleRange, ? extends R3> resultMapper3,
                                          @NotNull TriFunction<R1, R2, R3, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this), resultMapper3.apply(this));
    }

    default <R1, R2, R3> Triple<R1, R2, R3> doublesToThree(@NotNull Function<? super DoubleRange, ? extends R1> resultMapper1,
                                                        @NotNull Function<? super DoubleRange, ? extends R2> resultMapper2,
                                                        @NotNull Function<? super DoubleRange, ? extends R3> resultMapper3) {
        return doublesToThree(resultMapper1, resultMapper2, resultMapper3, Triple::of);
    }

    @Override
    @NotNull
    default DoubleRange get() {
        return this;
    }
}
