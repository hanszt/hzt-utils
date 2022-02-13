package hzt.ranges;

import hzt.collections.ArrayX;
import hzt.function.TriFunction;
import hzt.numbers.LongX;
import hzt.sequences.Sequence;
import hzt.statistics.LongStatistics;
import hzt.tuples.Pair;
import hzt.tuples.Triple;
import hzt.utils.It;
import hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.LongStream;

@FunctionalInterface
public interface LongRange extends NumberRange<Long>, Sequence<Long>, Transformable<LongRange> {

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

    default @NotNull Long min() {
        return min(It::noFilter);
    }

    default @NotNull Long min(Predicate<Long> predicate) {
        return filter(predicate).minOf(It::asLong);
    }

    default @NotNull Long max() {
        return max(It::noFilter);
    }

    default @NotNull Long max(Predicate<Long> predicate) {
        return filter(predicate).maxOf(It::asLong);
    }

    default @NotNull Double average() {
        return average(It::noFilter);
    }

    default @NotNull Double average(Predicate<Long> predicate) {
        return filter(predicate).averageOf(It::asLong);
    }

    default @NotNull Long sum() {
        return sum(It::noFilter);
    }

    default @NotNull Long sum(Predicate<Long> predicate) {
        return filter(predicate).sumOfLongs(It::asLong);
    }

    default @NotNull Double stdDev() {
        return stdDev(It::noFilter);
    }

    default @NotNull Double stdDev(Predicate<Long> predicate) {
        return filter(predicate).statsOfLongs(It::asLong).getStandardDeviation();
    }

    default @NotNull LongStatistics stats() {
        return stats(It::noFilter);
    }

    @Override
    default @NotNull LongRange filter(@NotNull Predicate<Long> predicate) {
        return LongRange.of(Sequence.super.filter(predicate));
    }

    @Override
    @NotNull
    default LongRange onEach(@NotNull Consumer<? super Long> consumer) {
        return LongRange.of(Sequence.super.onEach(consumer));
    }

    default LongStream longStream() {
        return stream().mapToLong(It::asLong);
    }

    @Override
    default @NotNull ArrayX<Long> toArrayX() {
        return toArrayX(Long[]::new);
    }

    @Override
    @NotNull
    default Long[] toArray() {
        return toArray(Long[]::new);
    }

    default long[] toLongArray() {
        return stream().mapToLong(It::asLong).toArray();
    }

    @Override
    default @NotNull LongStatistics stats(Predicate<Long> predicate) {
        return filter(predicate).statsOfLongs(It::asLong);
    }

    default <R1, R2, R> R longsToTwo(@NotNull Function<? super LongRange, ? extends R1> resultMapper1,
                                    @NotNull Function<? super LongRange, ? extends R2> resultMapper2,
                                    @NotNull BiFunction<R1, R2, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this));
    }

    default <R1, R2> Pair<R1, R2> longsToTwo(@NotNull Function<? super LongRange, ? extends R1> resultMapper1,
                                            @NotNull Function<? super LongRange, ? extends R2> resultMapper2) {
        return longsToTwo(resultMapper1, resultMapper2, Pair::of);
    }

    default <R1, R2, R3, R> R longsToThree(@NotNull Function<? super LongRange, ? extends R1> resultMapper1,
                                          @NotNull Function<? super LongRange, ? extends R2> resultMapper2,
                                          @NotNull Function<? super LongRange, ? extends R3> resultMapper3,
                                          @NotNull TriFunction<R1, R2, R3, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this), resultMapper3.apply(this));
    }

    default <R1, R2, R3> Triple<R1, R2, R3> longsToThree(@NotNull Function<? super LongRange, ? extends R1> resultMapper1,
                                                        @NotNull Function<? super LongRange, ? extends R2> resultMapper2,
                                                        @NotNull Function<? super LongRange, ? extends R3> resultMapper3) {
        return longsToThree(resultMapper1, resultMapper2, resultMapper3, Triple::of);
    }

    @Override
    @NotNull
    default LongRange get() {
        return this;
    }
}
