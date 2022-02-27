package hzt.sequences.primitives;

import hzt.function.TriFunction;
import hzt.iterables.primitives.IntCollectable;
import hzt.iterables.primitives.IntIterable;
import hzt.iterables.primitives.IntNumerable;
import hzt.iterables.primitives.IntReducable;
import hzt.iterables.primitives.IntStreamable;
import hzt.iterables.primitives.PrimitiveSortable;
import hzt.iterators.primitives.IntFilteringIterator;
import hzt.iterators.primitives.IntGeneratorIterator;
import hzt.iterators.primitives.IntMultiMappingIterator;
import hzt.iterators.primitives.IntSkipWhileIterator;
import hzt.iterators.primitives.IntTakeWhileIterator;
import hzt.iterators.primitives.PrimitiveIterators;
import hzt.numbers.IntX;
import hzt.sequences.Sequence;
import hzt.tuples.Pair;
import hzt.tuples.Triple;
import hzt.utils.It;
import hzt.utils.primitive_comparators.IntComparator;
import org.jetbrains.annotations.NotNull;

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
public interface IntSequence extends IntWindowedSequence, IntReducable, IntCollectable, IntNumerable, IntStreamable,
        PrimitiveSortable<IntComparator>,
        PrimitiveSequence<Integer, IntConsumer, IntUnaryOperator, IntPredicate, IntBinaryOperator> {

    static IntSequence empty() {
        return PrimitiveIterators::emptyIntIterator;
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
        return mapMulti((value, c) -> flatMapper.apply(value).forEachInt(c));
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
        return new IntTakeSequence(this, n);
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
        return new IntSkipSequence(this, n);
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
        return toListX().sorted().asSequence();
    }

    default IntSequence sorted(IntComparator intComparator) {
        return toListX().sorted(intComparator).asSequence();
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

    default int[] toArray() {
        return toListX().toArray();
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
