package hzt.ranges;

import hzt.collections.ArrayX;
import hzt.function.TriFunction;
import hzt.numbers.IntX;
import hzt.sequences.Sequence;
import hzt.statistics.IntStatistics;
import hzt.tuples.Pair;
import hzt.tuples.Triple;
import hzt.utils.It;
import hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

@FunctionalInterface
public interface IntRange extends NumberRange<Integer>, Sequence<Integer>, Transformable<IntRange> {

    static IntRange empty() {
        return IntRange.of(Sequence.empty());
    }

    static IntRange of(Iterable<Integer> integers) {
        return integers::iterator;
    }

    static IntRange of(IntStream stream) {
        return stream::iterator;
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
        return IntRange.of(filter(IntX.multipleOf(step)));
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

    default @NotNull Integer min() {
        return min(It::noFilter);
    }

    default @NotNull Integer min(Predicate<Integer> predicate) {
        return filter(predicate).minOf(It::asInt);
    }

    default @NotNull Integer max() {
        return max(It::noFilter);
    }

    default @NotNull Integer max(Predicate<Integer> predicate) {
        return filter(predicate).maxOf(It::asInt);
    }

    default @NotNull Double average() {
        return average(It::noFilter);
    }

    default @NotNull Double average(Predicate<Integer> predicate) {
        return filter(predicate).averageOf(It::asLong);
    }

    default @NotNull Long sum() {
        return sum(It::noFilter);
    }

    default @NotNull Long sum(Predicate<Integer> predicate) {
        return filter(predicate).sumOfInts(It::asInt);
    }

    default @NotNull Double stdDev() {
        return stdDev(It::noFilter);
    }

    default @NotNull Double stdDev(Predicate<Integer> predicate) {
        return filter(predicate).statsOfLongs(It::asLong).getStandardDeviation();
    }

    default @NotNull IntStatistics stats() {
        return stats(It::noFilter);
    }

    @Override
    default @NotNull IntRange filter(@NotNull Predicate<Integer> predicate) {
        return IntRange.of(Sequence.super.filter(predicate));
    }

    @Override
    @NotNull
    default IntRange onEach(@NotNull Consumer<? super Integer> consumer) {
        return IntRange.of(Sequence.super.onEach(consumer));
    }

    default IntStream intStream() {
        return stream().mapToInt(It::asInt);
    }

    @Override
    default @NotNull ArrayX<Integer> toArrayX() {
        return toArrayX(Integer[]::new);
    }

    @Override
    @NotNull
    default Integer[] toArray() {
        return toArray(Integer[]::new);
    }

    default int[] toIntArray() {
        return stream().mapToInt(It::asInt).toArray();
    }

    @Override
    default @NotNull IntStatistics stats(Predicate<Integer> predicate) {
        return filter(predicate).statsOfInts(It::asInt);
    }

    default <R1, R2, R> R intsToTwo(@NotNull Function<? super IntRange, ? extends R1> resultMapper1,
                                @NotNull Function<? super IntRange, ? extends R2> resultMapper2,
                                @NotNull BiFunction<R1, R2, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this));
    }

    default <R1, R2> Pair<R1, R2> intsToTwo(@NotNull Function<? super IntRange, ? extends R1> resultMapper1,
                                        @NotNull Function<? super IntRange, ? extends R2> resultMapper2) {
        return intsToTwo(resultMapper1, resultMapper2, Pair::of);
    }

    default <R1, R2, R3, R> R intsToThree(@NotNull Function<? super IntRange, ? extends R1> resultMapper1,
                                      @NotNull Function<? super IntRange, ? extends R2> resultMapper2,
                                      @NotNull Function<? super IntRange, ? extends R3> resultMapper3,
                                      @NotNull TriFunction<R1, R2, R3, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this), resultMapper3.apply(this));
    }

    default <R1, R2, R3> Triple<R1, R2, R3> intsToThree(@NotNull Function<? super IntRange, ? extends R1> resultMapper1,
                                                    @NotNull Function<? super IntRange, ? extends R2> resultMapper2,
                                                    @NotNull Function<? super IntRange, ? extends R3> resultMapper3) {
        return intsToThree(resultMapper1, resultMapper2, resultMapper3, Triple::of);
    }

    @Override
    @NotNull
    default IntRange get() {
        return this;
    }
}
