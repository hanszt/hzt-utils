package hzt.sequences.primitives;

import hzt.function.TriFunction;
import hzt.iterables.primitives.IntIterable;
import hzt.iterables.primitives.IntReducable;
import hzt.iterators.IntFilteringIterator;
import hzt.iterators.IntGeneratorIterator;
import hzt.iterators.IntMultiMappingIterator;
import hzt.iterators.IntRangeIterator;
import hzt.iterators.PrimitiveIterators;
import hzt.numbers.IntX;
import hzt.sequences.Sequence;
import hzt.statistics.IntStatistics;
import hzt.tuples.Pair;
import hzt.tuples.Triple;
import hzt.utils.It;
import hzt.utils.Transformable;
import net.mintern.primitive.Primitive;
import net.mintern.primitive.comparators.IntComparator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface IntSequence extends IntReducable,
        PrimitiveSequence<Integer, IntConsumer, IntUnaryOperator, IntPredicate, IntBinaryOperator>,
        Transformable<IntSequence> {

    static IntSequence empty() {
        return IntSequence.of(Sequence.empty());
    }

    static IntSequence of(Iterable<Integer> integers) {
        return of(integers, It::asInt);
    }

    static IntSequence of(IntIterable intIterable) {
        return intIterable::iterator;
    }

    static <T> IntSequence of(Iterable<T> iterable, ToIntFunction<T> mapper) {
        return () -> PrimitiveIterators.intIteratorOf(iterable.iterator(), mapper);
    }

    static IntSequence of(int... array) {
        return () -> PrimitiveIterators.intArrayIterator(array);
    }

    static IntSequence of(IntStream stream) {
        return stream::iterator;
    }

    static IntSequence of(int start, int end) {
        return of(start, end, 1);
    }

    static IntSequence of(int start, int end, int step) {
        final var endInclusive = end + (start < end ? -1 : 1);
        final IntSequence longRange = () -> IntRangeIterator.of(start, endInclusive, step);
        return start == end ? IntSequence.empty() : longRange;
    }

    static IntX from(int start) {
        return IntX.of(start);
    }

    static IntSequence until(int end) {
        return of(0, end);
    }

    static IntSequence until(int end, int step) {
        return of(0, end, step);
    }

    static IntSequence closed(int endInclusive) {
        return closed(0, endInclusive);
    }

    static IntSequence closed(int start, int endInclusive) {
        return () -> IntRangeIterator.of(start, endInclusive, 1);
    }

    static IntSequence closed(int start, int endInclusive, int step) {
        return () -> IntRangeIterator.of(start, endInclusive, step);
    }

    static IntSequence generate(int seedValue, IntUnaryOperator nextFunction) {
        return generate(() -> seedValue, nextFunction);
    }

    static IntSequence generate(@NotNull IntSupplier nextFunction) {
        return generate(nextFunction, t -> nextFunction.getAsInt());
    }

    static IntSequence generate(@NotNull IntSupplier seedFunction, @NotNull IntUnaryOperator nextFunction) {
        return () -> IntGeneratorIterator.of(seedFunction, nextFunction);
    }

    default IntSequence step(int step) {
        return filter(i -> i % step == 0);
    }

    @Override
    default IntSequence map(@NotNull IntUnaryOperator mapper) {
        return () -> PrimitiveIterators.intTransformingIterator(iterator(), mapper);
    }

    default IntSequence flatMap(IntFunction<? extends IntSequence> flatMapper) {
        return IntSequence.of(intStream().flatMap(s -> flatMapper.apply(s).intStream()));
    }

    default IntSequence mapMulti(IntMapMultiConsumer intMapMultiConsumer) {
        return () -> IntMultiMappingIterator.of(iterator(), intMapMultiConsumer);
    }

    @FunctionalInterface
    interface IntMapMultiConsumer {
        void accept(int value, IntConsumer intConsumer);
    }

    default LongSequence mapToLong(IntToLongFunction mapper) {
        return () -> PrimitiveIterators.intToLongIterator(iterator(), mapper);
    }

    default DoubleSequence mapToDouble(IntToDoubleFunction mapper) {
        return () -> PrimitiveIterators.intToDoubleIterator(iterator(), mapper);
    }

    default <R> Sequence<R> mapToObj(@NotNull Function<Integer, R> function) {
        return boxed().map(function);
    }

    default IntStream intStream() {
        return StreamSupport.intStream(spliterator(), false);
    }

    default Sequence<Integer> boxed() {
        return Sequence.of(this);
    }

    @Override
    default IntSequence take(long n) {
        return IntSequence.of(intStream().limit(n));
    }

    @Override
    default IntSequence takeWhile(@NotNull IntPredicate predicate) {
        return IntSequence.of(intStream().takeWhile(predicate));
    }

    @Override
    default IntSequence takeWhileInclusive(@NotNull IntPredicate predicate) {
        return null;
    }

    @Override
    default IntSequence skip(long n) {
        return IntSequence.of(intStream().skip(n));
    }

    @Override
    default IntSequence skipWhile(@NotNull IntPredicate predicate) {
        return  IntSequence.of(intStream().dropWhile(predicate));
    }

    @Override
    default IntSequence skipWhileInclusive(@NotNull IntPredicate predicate) {
        return null;
    }

    @Override
    default IntSequence sorted() {
        final var array = toArray();
        Arrays.sort(array);
        return IntSequence.of(array);
    }

    default IntSequence sorted(IntComparator intComparator) {
        final var array = toArray();
        Primitive.sort(array, intComparator);
        return IntSequence.of(array);
    }

    @Override
    default IntSequence sortedDescending() {
        return sorted((IntX::compareReversed));
    }

    @Override
    default @NotNull IntSequence filter(@NotNull IntPredicate predicate) {
        return () -> IntFilteringIterator.of(iterator(), predicate, true);
    }

    default @NotNull IntSequence filterNot(@NotNull IntPredicate predicate) {
        return () -> IntFilteringIterator.of(iterator(), predicate, false);
    }

    default int min() {
        return stats().getMin();
    }

    default int min(IntPredicate predicate) {
        return filter(predicate).min();
    }

    default int max() {
        return stats().getMax();
    }

    default int max(IntPredicate predicate) {
        return filter(predicate).max();
    }

    default double average() {
        return stats().getAverage();
    }

    default double average(IntPredicate predicate) {
        return filter(predicate).average();
    }

    default long sum() {
        long sum = 0;
        PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            int i = iterator.nextInt();
            sum += i;
        }
        return sum;
    }

    default long sum(IntPredicate predicate) {
        return filter(predicate).sum();
    }

    default double stdDev() {
        return stats().getStandardDeviation();
    }

    default double stdDev(IntPredicate predicate) {
        return filter(predicate).stdDev();
    }

    default @NotNull IntStatistics stats() {
        IntStatistics intStatistics = new IntStatistics();
        PrimitiveIterator.OfInt iterator = this.iterator();
        while (iterator.hasNext()) {
            int i = iterator.nextInt();
            intStatistics.accept(i);
        }
        return intStatistics;
    }

    default @NotNull IntStatistics stats(@NotNull IntPredicate predicate) {
        return filter(predicate).stats();
    }

    default @NotNull IntSequence onEach(@NotNull IntConsumer consumer) {
        return map(i -> {
            consumer.accept(i);
            return i;
        });
    }

    @Override
    default IntSequence zip(@NotNull IntBinaryOperator merger, @NotNull Iterable<Integer> other) {
        return IntSequence.of(boxed().zip(other, merger::applyAsInt));
    }

    default int[] toArray() {
        return intStream().toArray();
    }

    default <R1, R2, R> R intsToTwo(@NotNull Function<? super IntSequence, ? extends R1> resultMapper1,
                                    @NotNull Function<? super IntSequence, ? extends R2> resultMapper2,
                                    @NotNull BiFunction<R1, R2, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this));
    }

    default <R1, R2> Pair<R1, R2> intsToTwo(@NotNull Function<? super IntSequence, ? extends R1> resultMapper1,
                                            @NotNull Function<? super IntSequence, ? extends R2> resultMapper2) {
        return intsToTwo(resultMapper1, resultMapper2, Pair::of);
    }

    default <R1, R2, R3, R> R intsToThree(@NotNull Function<? super IntSequence, ? extends R1> resultMapper1,
                                          @NotNull Function<? super IntSequence, ? extends R2> resultMapper2,
                                          @NotNull Function<? super IntSequence, ? extends R3> resultMapper3,
                                          @NotNull TriFunction<R1, R2, R3, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this), resultMapper3.apply(this));
    }

    default <R1, R2, R3> Triple<R1, R2, R3> intsToThree(@NotNull Function<? super IntSequence, ? extends R1> resultMapper1,
                                                        @NotNull Function<? super IntSequence, ? extends R2> resultMapper2,
                                                        @NotNull Function<? super IntSequence, ? extends R3> resultMapper3) {
        return intsToThree(resultMapper1, resultMapper2, resultMapper3, Triple::of);
    }

    @Override
    @NotNull
    default IntSequence get() {
        return this;
    }
}
