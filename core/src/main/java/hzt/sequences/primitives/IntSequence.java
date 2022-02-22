package hzt.sequences.primitives;

import hzt.collections.primitives.IntListX;
import hzt.function.TriFunction;
import hzt.iterables.primitives.IntCollectable;
import hzt.iterables.primitives.IntIterable;
import hzt.iterables.primitives.IntNumerable;
import hzt.iterables.primitives.IntReducable;
import hzt.iterables.primitives.IntStreamable;
import hzt.iterators.primitives.IntFilteringIterator;
import hzt.iterators.primitives.IntGeneratorIterator;
import hzt.iterators.primitives.IntMultiMappingIterator;
import hzt.iterators.primitives.IntRangeIterator;
import hzt.iterators.primitives.IntSkipWhileIterator;
import hzt.iterators.primitives.IntTakeWhileIterator;
import hzt.iterators.primitives.PrimitiveIterators;
import hzt.numbers.IntX;
import hzt.sequences.Sequence;
import hzt.tuples.Pair;
import hzt.tuples.Triple;
import hzt.utils.It;
import net.mintern.primitive.Primitive;
import net.mintern.primitive.comparators.IntComparator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
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

@FunctionalInterface
public interface IntSequence extends IntReducable, IntCollectable, IntNumerable, IntStreamable,
        PrimitiveSequence<Integer, IntConsumer, IntUnaryOperator, IntPredicate, IntBinaryOperator> {

    static IntSequence empty() {
        return IntSequence.of(Sequence.empty());
    }

    static IntSequence of(Iterable<Integer> iterable) {
        if (iterable instanceof IntIterable) {
            final var intIterable = (IntIterable) iterable;
            return intIterable::iterator;
        }
        return of(iterable, It::asInt);
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

    default IntSequence plus(int @NotNull ... values) {
        return Sequence.of(this, IntSequence.of(values)).flatMap(It::self).mapToInt(It::asInt);
    }

    default IntSequence plus(@NotNull Iterable<Integer> values) {
        return Sequence.of(this, IntSequence.of(values)).flatMap(It::self).mapToInt(It::asInt);
    }

    @Override
    default IntSequence map(@NotNull IntUnaryOperator mapper) {
        return () -> PrimitiveIterators.intTransformingIterator(iterator(), mapper);
    }

    default IntSequence flatMap(IntFunction<? extends IntSequence> flatMapper) {
        return IntSequence.of(stream().flatMap(s -> flatMapper.apply(s).stream()));
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

    default <R> Sequence<R> mapToObj(@NotNull IntFunction<R> function) {
        return () -> PrimitiveIterators.intToObjIterator(iterator(), function);
    }

    default Sequence<Integer> boxed() {
        return Sequence.of(this);
    }

    @Override
    default IntSequence take(long n) {
        return IntSequence.of(stream().limit(n));
    }

    @Override
    default IntSequence takeWhile(@NotNull IntPredicate predicate) {
        return () -> IntTakeWhileIterator.of(iterator(), predicate);
    }

    @Override
    default IntSequence takeWhileInclusive(@NotNull IntPredicate predicate) {
        return () -> IntTakeWhileIterator.of(iterator(), predicate, true);
    }

    @Override
    default IntSequence skip(long n) {
        return IntSequence.of(stream().skip(n));
    }

    @Override
    default IntSequence skipWhile(@NotNull IntPredicate predicate) {
        return () -> IntSkipWhileIterator.of(iterator(), predicate, false);
    }

    @Override
    default IntSequence skipWhileInclusive(@NotNull IntPredicate predicate) {
        return () -> IntSkipWhileIterator.of(iterator(), predicate, true);
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

    default @NotNull IntSequence onEach(@NotNull IntConsumer consumer) {
        return map(i -> {
            consumer.accept(i);
            return i;
        });
    }

    default IntSequence zip(@NotNull IntBinaryOperator merger, int... array) {
        final var iterator = PrimitiveIterators.intArrayIterator(array);
        return () -> PrimitiveIterators.mergingIterator(iterator(), iterator, merger);
    }

    @Override
    default IntSequence zip(@NotNull IntBinaryOperator merger, @NotNull Iterable<Integer> other) {
        final var iterator = PrimitiveIterators.intIteratorOf(other.iterator(), It::asInt);
        return () -> PrimitiveIterators.mergingIterator(iterator(), iterator, merger);
    }

    @Override
    default IntSequence zipWithNext(@NotNull IntBinaryOperator merger) {
        return windowed(2, s -> merger.applyAsInt(s.first(), s.last()));
    }

    default Sequence<IntListX> chunked(int size) {
        return windowed(size, size, true);
    }

    default IntSequence chunked(int size, @NotNull ToIntFunction<IntListX> transform) {
        return windowed(size, size, true).mapToInt(transform);
    }

    default Sequence<IntListX> windowed(int size, int step, boolean partialWindows) {
        return new IntWindowedSequence(this, size, step, partialWindows);
    }

    default Sequence<IntListX> windowed(int size, int step) {
        return windowed(size, step, false);
    }

    default Sequence<IntListX> windowed(int size) {
        return windowed(size, 1);
    }

    default Sequence<IntListX> windowed(int size, boolean partialWindows) {
        return windowed(size, 1, partialWindows);
    }

    default IntSequence windowed(int size, int step, boolean partialWindows,
                                 @NotNull ToIntFunction<IntListX> reducer) {
        return windowed(size, step, partialWindows).mapToInt(reducer);
    }

    default IntSequence windowed(int size, int step, @NotNull ToIntFunction<IntListX> reducer) {
        return windowed(size, step, false, reducer);
    }

    default IntSequence windowed(int size, @NotNull ToIntFunction<IntListX> reducer) {
        return windowed(size, 1, reducer);
    }

    default IntSequence windowed(int size, boolean partialWindows, @NotNull ToIntFunction<IntListX> reducer) {
        return windowed(size, 1, partialWindows, reducer);
    }

    default int[] toArray() {
        return stream().toArray();
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
}
