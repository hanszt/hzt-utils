package hzt.ranges;

import hzt.collections.ArrayX;
import hzt.numbers.IntX;
import hzt.sequences.Sequence;
import hzt.statistics.IntStatistics;
import hzt.utils.It;
import hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

@FunctionalInterface
public interface IntRange extends NumberRange<Integer>, Sequence<Integer>, Transformable<IntRange> {

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

    default int min(IntPredicate predicate) {
        return filter(predicate::test).minOf(It::asInt);
    }

    default @NotNull Integer max() {
        return max(It::noFilter);
    }

    default int max(IntPredicate predicate) {
        return filter(predicate::test).maxOf(It::asInt);
    }

    default @NotNull Double average() {
        return average(It::noFilter);
    }

    default double average(IntPredicate predicate) {
        return filter(predicate::test).averageOf(It::asLong);
    }

    default @NotNull Long sum() {
        return sum(It::noFilter);
    }

    default long sum(IntPredicate predicate) {
        return filter(predicate::test).sumOfInts(It::asInt);
    }

    default @NotNull Double stdDev() {
        return stdDev(It::noFilter);
    }

    default double stdDev(LongPredicate predicate) {
        return filter(predicate::test).statsOfLongs(It::asLong).getStandardDeviation();
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

    default IntStatistics stats(IntPredicate predicate) {
        return filter(predicate::test).statsOfInts(It::asInt);
    }

    @Override
    @NotNull
    default IntRange get() {
        return this;
    }
}
