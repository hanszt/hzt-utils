package hzt.sequences.primitives;

import hzt.collections.primitives.LongListX;
import hzt.function.TriFunction;
import hzt.iterables.primitives.LongIterable;
import hzt.iterables.primitives.LongReducable;
import hzt.iterables.Streamable;
import hzt.iterators.primitives.LongFilteringIterator;
import hzt.iterators.primitives.LongGeneratorIterator;
import hzt.iterators.primitives.LongMultiMappingIterator;
import hzt.iterators.primitives.LongRangeIterator;
import hzt.iterators.primitives.LongSkipWhileIterator;
import hzt.iterators.primitives.LongTakeWhileIterator;
import hzt.iterators.primitives.PrimitiveIterators;
import hzt.numbers.LongX;
import hzt.sequences.Sequence;
import hzt.statistics.LongStatistics;
import hzt.tuples.Pair;
import hzt.tuples.Triple;
import hzt.utils.It;
import hzt.utils.Transformable;
import net.mintern.primitive.Primitive;
import net.mintern.primitive.comparators.LongComparator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.ToLongFunction;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface LongSequence extends LongReducable,
        PrimitiveSequence<Long, LongConsumer, LongUnaryOperator, LongPredicate, LongBinaryOperator>,
        Streamable<LongStream>,
        Transformable<LongSequence> {

    static LongSequence empty() {
        return LongSequence.of(Sequence.empty());
    }

    static LongSequence of(Iterable<Long> iterable) {
        if (iterable instanceof LongIterable) {
            final var longIterable = (LongIterable) iterable;
            return longIterable::iterator;
        }
        return of(iterable, It::asLong);
    }

    static <T> LongSequence of(Iterable<T> iterable, ToLongFunction<T> mapper) {
        return () -> PrimitiveIterators.longIteratorOf(iterable.iterator(), mapper);
    }

    static LongSequence of(long... longs) {
        return () -> PrimitiveIterators.longArrayIterator(longs);
    }

    static LongSequence of(LongStream longStream) {
        return longStream::iterator;
    }

    static LongSequence of(long start, long end) {
        return of(start, end, 1);
    }

    static LongSequence of(long start, long end, long step) {
        final var endInclusive = end + (start < end ? -1 : 1);
        final LongSequence longSequence = () -> LongRangeIterator.of(start, endInclusive, step);
        return start == end ? LongSequence.empty() : longSequence;
    }

    static LongX from(long start) {
        return LongX.of(start);
    }

    static LongSequence until(long end) {
        return of(0, end);
    }

    static LongSequence until(long end, long step) {
        return of(0, end, step);
    }

    static LongSequence closed(long endInclusive) {
        return closed(0, endInclusive);
    }

    static LongSequence closed(long start, long endInclusive) {
        return closed(start, endInclusive, 1);
    }

    static LongSequence closed(long start, long endInclusive, long step) {
        return () -> LongRangeIterator.of(start, endInclusive, step);
    }

    static LongSequence generate(long seedValue, LongUnaryOperator nextFunction) {
        return generate(() -> seedValue, nextFunction);
    }

    static LongSequence generate(@NotNull LongSupplier nextFunction) {
        return generate(nextFunction, t -> nextFunction.getAsLong());
    }

    static LongSequence generate(@NotNull LongSupplier seedFunction, @NotNull LongUnaryOperator nextFunction) {
        return () -> LongGeneratorIterator.of(seedFunction, nextFunction);
    }

    default LongSequence step(long step) {
        return filter(l -> l % step == 0);
    }

    default LongSequence plus(@NotNull long... values) {
        return Sequence.of(this, LongSequence.of(values)).flatMap(It::self).mapToLong(It::asLong);
    }

    default LongSequence plus(@NotNull Iterable<Long> values) {
        return Sequence.of(this, LongSequence.of(values)).flatMap(It::self).mapToLong(It::asLong);
    }

    default LongSequence map(@NotNull LongUnaryOperator unaryOperator) {
        return () -> PrimitiveIterators.longTransformingIterator(iterator(), unaryOperator);
    }

    default LongSequence flatMap(LongFunction<? extends LongSequence> flatMapper) {
        return LongSequence.of(stream().flatMap(s -> flatMapper.apply(s).stream()));
    }

    default LongSequence mapMulti(LongMapMultiConsumer longMapMultiConsumer) {
        return () -> LongMultiMappingIterator.of(iterator(), longMapMultiConsumer);
    }

    @FunctionalInterface
    interface LongMapMultiConsumer {
        void accept(long value, LongConsumer lc);
    }

    default IntSequence mapToInt(LongToIntFunction mapper) {
        return () -> PrimitiveIterators.longToIntIterator(iterator(), mapper);
    }

    default DoubleSequence mapToDouble(LongToDoubleFunction mapper) {
        return () -> PrimitiveIterators.longToDoubleIterator(iterator(), mapper);
    }

    default <R> Sequence<R> mapToObj(@NotNull Function<Long, R> mapper) {
        return Sequence.of(this).map(mapper);
    }

    default Sequence<Long> boxed() {
        return Sequence.of(this);
    }

    default LongStream stream() {
        return StreamSupport.longStream(spliterator(), false);
    }

    default LongStream parallelStream() {
        return StreamSupport.longStream(spliterator(), true);
    }

    default LongSequence filter(@NotNull LongPredicate predicate) {
        return () -> LongFilteringIterator.of(iterator(), predicate, true);
    }

    default @NotNull LongSequence filterNot(@NotNull LongPredicate predicate) {
        return () -> LongFilteringIterator.of(iterator(), predicate, false);
    }

    default LongSequence take(long n) {
        return LongSequence.of(stream().limit(n));
    }

    default LongSequence takeWhile(@NotNull LongPredicate predicate) {
        return () -> LongTakeWhileIterator.of(iterator(), predicate);
    }

    @Override
    default LongSequence takeWhileInclusive(@NotNull LongPredicate predicate) {
        return () -> LongTakeWhileIterator.of(iterator(), predicate, true);
    }

    default LongSequence skip(long n) {
        return LongSequence.of(stream().skip(n));
    }

    default LongSequence skipWhile(@NotNull LongPredicate longPredicate) {
        return () -> LongSkipWhileIterator.of(iterator(), longPredicate, false);
    }

    @Override
    default LongSequence skipWhileInclusive(@NotNull LongPredicate longPredicate) {
        return () -> LongSkipWhileIterator.of(iterator(), longPredicate, true);
    }

    @Override
    default LongSequence sorted() {
        final var array = toArray();
        Arrays.sort(array);
        return LongSequence.of(array);
    }

    default LongSequence sorted(LongComparator longComparator) {
        final var array = toArray();
        Primitive.sort(array, longComparator);
        return LongSequence.of(array);
    }

    @Override
    default LongSequence sortedDescending() {
        return sorted(LongX::compareReversed);
    }

    default @NotNull LongSequence onEach(@NotNull LongConsumer consumer) {
        return map(l -> {
            consumer.accept(l);
            return l;
        });
    }

    default LongSequence zip(@NotNull LongBinaryOperator merger, long... array) {
        final var iterator = PrimitiveIterators.longArrayIterator(array);
        return () -> PrimitiveIterators.mergingIterator(iterator(), iterator, merger);
    }

    @Override
    default LongSequence zip(@NotNull LongBinaryOperator merger, @NotNull Iterable<Long> other) {
        final var iterator = PrimitiveIterators.longIteratorOf(other.iterator(), It::asLong);
        return () -> PrimitiveIterators.mergingIterator(iterator(), iterator, merger);
    }

    @Override
    default LongSequence zipWithNext(@NotNull LongBinaryOperator merger) {
        return windowed(2, s -> merger.applyAsLong(s.first(), s.last()));
    }

    default Sequence<LongListX> chunked(int size) {
        return windowed(size, size, true);
    }

    default LongSequence chunked(int size, @NotNull ToLongFunction<LongSequence> transform) {
        return windowed(size, size, true).mapToLong(transform);
    }

    default Sequence<LongListX> windowed(int size, int step, boolean partialWindows) {
        return new LongWindowedSequence(this, size, step, partialWindows);
    }

    default Sequence<LongListX> windowed(int size, int step) {
        return windowed(size, step, false);
    }

    default Sequence<LongListX> windowed(int size) {
        return windowed(size, 1);
    }

    default Sequence<LongListX> windowed(int size, boolean partialWindows) {
        return windowed(size, 1, partialWindows);
    }

    default LongSequence windowed(int size, int step, boolean partialWindows,
                                  @NotNull ToLongFunction<LongSequence> reducer) {
        return windowed(size, step, partialWindows).mapToLong(reducer);
    }

    default LongSequence windowed(int size, int step, @NotNull ToLongFunction<LongSequence> reducer) {
        return windowed(size, step, false, reducer);
    }

    default LongSequence windowed(int size, @NotNull ToLongFunction<LongSequence> reducer) {
        return windowed(size, 1, reducer);
    }

    default LongSequence windowed(int size, boolean partialWindows, @NotNull ToLongFunction<LongSequence> reducer) {
        return windowed(size, 1, partialWindows, reducer);
    }

    default long min() {
        return stats().getMin();
    }

    default long min(LongPredicate predicate) {
        return filter(predicate).min();
    }

    default long max() {
        return stats().getMax();
    }

    default long max(LongPredicate predicate) {
        return filter(predicate).max();
    }

    default double average() {
        return stats().getAverage();
    }

    default double average(LongPredicate predicate) {
        return filter(predicate).average();
    }

    default long sum() {
        long sum = 0;
        PrimitiveIterator.OfLong iterator = this.iterator();
        while (iterator.hasNext()) {
            long value = iterator.nextLong();
            sum += value;
        }
        return sum;
    }

    default long sum(LongPredicate predicate) {
        return filter(predicate).sum();
    }

    default double stdDev() {
        return stats().getStandardDeviation();
    }

    default double stdDev(LongPredicate predicate) {
        return filter(predicate).stats().getStandardDeviation();
    }

    default @NotNull LongStatistics stats() {
        var statistics = new LongStatistics();
        PrimitiveIterator.OfLong iterator = this.iterator();
        while (iterator.hasNext()) {
            long value = iterator.nextLong();
            statistics.accept(value);
        }
        return statistics;
    }

    default @NotNull LongStatistics stats(@NotNull LongPredicate predicate) {
        return filter(predicate).stats();
    }

    default long[] toArray() {
        return stream().toArray();
    }

    default <R1, R2, R> R longsToTwo(@NotNull Function<? super LongSequence, ? extends R1> resultMapper1,
                                    @NotNull Function<? super LongSequence, ? extends R2> resultMapper2,
                                    @NotNull BiFunction<R1, R2, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this));
    }

    default <R1, R2> Pair<R1, R2> longsToTwo(@NotNull Function<? super LongSequence, ? extends R1> resultMapper1,
                                            @NotNull Function<? super LongSequence, ? extends R2> resultMapper2) {
        return longsToTwo(resultMapper1, resultMapper2, Pair::of);
    }

    default <R1, R2, R3, R> R longsToThree(@NotNull Function<? super LongSequence, ? extends R1> resultMapper1,
                                          @NotNull Function<? super LongSequence, ? extends R2> resultMapper2,
                                          @NotNull Function<? super LongSequence, ? extends R3> resultMapper3,
                                          @NotNull TriFunction<R1, R2, R3, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this), resultMapper3.apply(this));
    }

    default <R1, R2, R3> Triple<R1, R2, R3> longsToThree(@NotNull Function<? super LongSequence, ? extends R1> resultMapper1,
                                                        @NotNull Function<? super LongSequence, ? extends R2> resultMapper2,
                                                        @NotNull Function<? super LongSequence, ? extends R3> resultMapper3) {
        return longsToThree(resultMapper1, resultMapper2, resultMapper3, Triple::of);
    }

    @Override
    @NotNull
    default LongSequence get() {
        return this;
    }
}
