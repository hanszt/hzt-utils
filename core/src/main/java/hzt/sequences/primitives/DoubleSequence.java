package hzt.sequences.primitives;

import hzt.function.TriFunction;
import hzt.iterables.primitives.DoubleIterable;
import hzt.iterables.primitives.DoubleReducable;
import hzt.iterators.DoubleFilteringIterator;
import hzt.iterators.DoubleGeneratorIterator;
import hzt.iterators.DoubleMultiMappingIterator;
import hzt.iterators.DoubleTakeWhileIterator;
import hzt.iterators.PrimitiveIterators;
import hzt.numbers.DoubleX;
import hzt.sequences.Sequence;
import hzt.statistics.DoubleStatistics;
import hzt.tuples.Pair;
import hzt.tuples.Triple;
import hzt.utils.It;
import hzt.utils.Transformable;
import net.mintern.primitive.Primitive;
import net.mintern.primitive.comparators.DoubleComparator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.function.BiFunction;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.ToDoubleFunction;
import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface DoubleSequence extends DoubleReducable,
        PrimitiveSequence<Double, DoubleConsumer, DoubleUnaryOperator, DoublePredicate, DoubleBinaryOperator>,
        Transformable<DoubleSequence> {

    static DoubleSequence empty() {
        return DoubleSequence.of(Sequence.empty());
    }

    static DoubleSequence of(Iterable<Double> doubleIterable) {
        return of(doubleIterable, It::asDouble);
    }

    static DoubleSequence of(DoubleIterable doubleIterable) {
        return doubleIterable::iterator;
    }

    static <T> DoubleSequence of(Iterable<T> iterable, ToDoubleFunction<T> mapper) {
        return () -> PrimitiveIterators.doubleIteratorOf(iterable.iterator(), mapper);
    }

    static DoubleSequence of(double... doubles) {
        return () -> PrimitiveIterators.doubleArrayIterator(doubles);
    }

    static DoubleSequence of (DoubleStream stream) {
        return stream::iterator;
    }

    static DoubleSequence of(double start, double end) {
        return of(start, end, 1);
    }

    static DoubleSequence of(double start, double end, double step) {
        return generate(start, d -> d + (start < end ? step : -step)).takeWhile(d -> start < end ? (d < end) : (d > end));
    }

    static DoubleSequence generate(double seedValue, DoubleUnaryOperator nextFunction) {
        return generate(() -> seedValue, nextFunction);
    }

    static DoubleSequence generate(@NotNull DoubleSupplier nextFunction) {
        return generate(nextFunction, t -> nextFunction.getAsDouble());
    }

    static DoubleSequence generate(@NotNull DoubleSupplier seedFunction, @NotNull DoubleUnaryOperator nextFunction) {
        return () -> DoubleGeneratorIterator.of(seedFunction, nextFunction);
    }

    static DoubleX from(double start) {
        return DoubleX.of(start);
    }

    static DoubleSequence closed(double endInclusive) {
        return closed(0, endInclusive);
    }

    static DoubleSequence closed(double start, double endInclusive) {
        return closed(start, endInclusive, 1);
    }

    static DoubleSequence closed(double start, double endInclusive, double step) {
        return generate(start, d -> d + (start < endInclusive ? step : -step)).takeWhile(d -> (start < endInclusive) == (d < endInclusive));
    }

    @Override
    default DoubleSequence map(@NotNull DoubleUnaryOperator mapper) {
        return () -> PrimitiveIterators.doubleTransformingIterator(iterator(), mapper);
    }

    default DoubleSequence flatMap(DoubleFunction<? extends DoubleSequence> flatMapper) {
        return DoubleSequence.of(stream().flatMap(s -> flatMapper.apply(s).stream()));
    }

    default DoubleSequence mapMulti(DoubleMapMultiConsumer mapMultiConsumer) {
        return () -> DoubleMultiMappingIterator.of(iterator(), mapMultiConsumer);
    }

    @FunctionalInterface
    interface DoubleMapMultiConsumer {
        void accept(double value, DoubleConsumer intConsumer);
    }

    default IntSequence mapToInt(DoubleToIntFunction mapper) {
        return () -> PrimitiveIterators.doubleToIntIterator(iterator(), mapper);
    }

    default LongSequence mapToLong(DoubleToLongFunction mapper) {
        return () -> PrimitiveIterators.doubleToLongIterator(iterator(), mapper);
    }

    @Override
    default <R> Sequence<R> mapToObj(@NotNull Function<Double, R> function) {
        return Sequence.of(this).map(function);
    }

    @Override
    default Sequence<Double> boxed() {
        return Sequence.of(this);
    }

    @FunctionalInterface
    interface IntMapMultiConsumer {
        void accept(int value, IntConsumer intConsumer);
    }

    @Override
    default DoubleSequence take(long n) {
        return DoubleSequence.of(stream().limit(n));
    }

    @Override
    default DoubleSequence takeWhile(@NotNull DoublePredicate predicate) {
        return () -> DoubleTakeWhileIterator.of(iterator(), predicate);
    }

    @Override
    default DoubleSequence takeWhileInclusive(@NotNull DoublePredicate predicate) {
       return  () -> DoubleTakeWhileIterator.of(iterator(), predicate, true);
    }

    @Override
    default DoubleSequence skip(long n) {
        return DoubleSequence.of(stream().skip(n));
    }

    @Override
    default DoubleSequence skipWhile(@NotNull DoublePredicate predicate) {
        return DoubleSequence.of(stream().dropWhile(predicate));
    }

    @Override
    default DoubleSequence skipWhileInclusive(@NotNull DoublePredicate predicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    default DoubleSequence sorted() {
        final var array = toArray();
        Arrays.sort(array);
        return DoubleSequence.of(array);
    }

    default DoubleSequence sorted(DoubleComparator comparator) {
        final var array = toArray();
        Primitive.sort(array, comparator);
        return DoubleSequence.of(array);
    }

    @Override
    default DoubleSequence sortedDescending() {
        return sorted(DoubleX::compareReversed);
    }

    default double min() {
        return min(It::noFilter);
    }

    default double min(DoublePredicate predicate) {
        return filter(predicate).min();
    }

    default double max() {
        return stats().getMax();
    }

    default double max(DoublePredicate predicate) {
        return filter(predicate).max();
    }

    default double average() {
        return stats().getAverage();
    }

    default double average(DoublePredicate predicate) {
        return filter(predicate).average();
    }

    default double sum() {
        return stats().getSum();
    }

    default double sum(DoublePredicate predicate) {
        return filter(predicate).sum();
    }

    default double stdDev() {
        return stats().getStandardDeviation();
    }

    default double stdDev(DoublePredicate predicate) {
        return filter(predicate).stdDev();
    }

    default @NotNull DoubleStatistics stats() {
        DoubleStatistics doubleStatistics = new DoubleStatistics();
        PrimitiveIterator.OfDouble iterator = this.iterator();
        while (iterator.hasNext()) {
            double d = iterator.nextDouble();
            doubleStatistics.accept(d);
        }
        return doubleStatistics;
    }

    @Override
    default @NotNull DoubleSequence filter(@NotNull DoublePredicate predicate) {
        return () -> DoubleFilteringIterator.of(iterator(), predicate, true);
    }

    @Override
    default DoubleSequence filterNot(@NotNull DoublePredicate predicate) {
        return () -> DoubleFilteringIterator.of(iterator(), predicate, false);
    }

    @Override
    @NotNull
    default DoubleSequence onEach(@NotNull DoubleConsumer consumer) {
        return map(d -> {
            consumer.accept(d);
            return d;
        });
    }

    @Override
    default DoubleSequence zip(@NotNull DoubleBinaryOperator merger, @NotNull Iterable<Double> other) {
        return DoubleSequence.of(boxed().zip(other, merger::applyAsDouble));
    }

    @Override
    default DoubleSequence zipWithNext(@NotNull DoubleBinaryOperator merger) {
        throw new UnsupportedOperationException();
    }

    default Sequence<DoubleSequence> windowed(int size, int step) {
        throw new UnsupportedOperationException();
    }

    default DoubleStream stream() {
        return StreamSupport.doubleStream(spliterator(), false);
    }

    default DoubleStream parallelStream() {
        return StreamSupport.doubleStream(spliterator(), true);
    }

    default double[] toArray() {
        return stream().toArray();
    }

    default @NotNull DoubleStatistics stats(@NotNull DoublePredicate doublePredicate) {
        return filter(doublePredicate).stats();
    }

    default <R1, R2, R> R doublesToTwo(@NotNull Function<? super DoubleSequence, ? extends R1> resultMapper1,
                                    @NotNull Function<? super DoubleSequence, ? extends R2> resultMapper2,
                                    @NotNull BiFunction<R1, R2, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this));
    }

    default <R1, R2> Pair<R1, R2> doublesToTwo(@NotNull Function<? super DoubleSequence, ? extends R1> resultMapper1,
                                            @NotNull Function<? super DoubleSequence, ? extends R2> resultMapper2) {
        return doublesToTwo(resultMapper1, resultMapper2, Pair::of);
    }

    default <R1, R2, R3, R> R doublesToThree(@NotNull Function<? super DoubleSequence, ? extends R1> resultMapper1,
                                          @NotNull Function<? super DoubleSequence, ? extends R2> resultMapper2,
                                          @NotNull Function<? super DoubleSequence, ? extends R3> resultMapper3,
                                          @NotNull TriFunction<R1, R2, R3, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this), resultMapper3.apply(this));
    }

    default <R1, R2, R3> Triple<R1, R2, R3> doublesToThree(@NotNull Function<? super DoubleSequence, ? extends R1> resultMapper1,
                                                        @NotNull Function<? super DoubleSequence, ? extends R2> resultMapper2,
                                                        @NotNull Function<? super DoubleSequence, ? extends R3> resultMapper3) {
        return doublesToThree(resultMapper1, resultMapper2, resultMapper3, Triple::of);
    }

    @Override
    @NotNull
    default DoubleSequence get() {
        return this;
    }
}
