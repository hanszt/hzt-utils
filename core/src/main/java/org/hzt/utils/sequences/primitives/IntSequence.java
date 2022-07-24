package org.hzt.utils.sequences.primitives;

import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.hzt.utils.function.TriFunction;
import org.hzt.utils.iterables.primitives.IntCollectable;
import org.hzt.utils.iterables.primitives.IntGroupable;
import org.hzt.utils.iterables.primitives.IntNumerable;
import org.hzt.utils.iterables.primitives.IntReducable;
import org.hzt.utils.iterables.primitives.IntStreamable;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterables.primitives.PrimitiveSortable;
import org.hzt.utils.iterators.primitives.IntFilteringIterator;
import org.hzt.utils.iterators.primitives.IntGeneratorIterator;
import org.hzt.utils.iterators.primitives.IntMultiMappingIterator;
import org.hzt.utils.iterators.primitives.IntSkipWhileIterator;
import org.hzt.utils.iterators.primitives.IntTakeWhileIterator;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.tuples.Pair;
import org.hzt.utils.tuples.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Consumer;
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
        IntGroupable, PrimitiveSortable<IntComparator>,
        PrimitiveSequence<Integer, IntConsumer, IntUnaryOperator, IntPredicate, IntBinaryOperator> {

    static IntSequence empty() {
        return PrimitiveIterators::emptyIntIterator;
    }

    static IntSequence of(Iterable<Integer> iterable) {
        if (iterable instanceof PrimitiveIterable.OfInt intIterable) {
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
        return Sequence.of(this, IntSequence.of(values)).mapMultiToInt(OfInt::forEachInt);
    }

    default IntSequence plus(@NotNull Iterable<Integer> values) {
        return Sequence.of(this, IntSequence.of(values)).mapMultiToInt(OfInt::forEachInt);
    }

    @Override
    default IntSequence distinct() {
        return () -> PrimitiveIterators.distinctIterator(iterator());
    }

    @Override
    default IntSequence map(@NotNull IntUnaryOperator mapper) {
        return () -> PrimitiveIterators.intTransformingIterator(iterator(), mapper);
    }

    default IntSequence flatMap(IntFunction<? extends Iterable<Integer>> flatMapper) {
        return mapMulti((value, intConsumer) -> consumeForEach(flatMapper.apply(value), intConsumer));
    }

    private static void consumeForEach(Iterable<Integer> iterable, IntConsumer consumer) {
        if (iterable instanceof OfInt) {
            ((OfInt) iterable).forEachInt(consumer);
        } else {
            iterable.forEach(consumer::accept);
        }
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
        return mapToObj(Integer::valueOf);
    }

    @Override
    default IntSequence take(long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return PrimitiveIterators::emptyIntIterator;
        } else if (this instanceof IntSkipTakeSequence skipTakeSequence) {
            return skipTakeSequence.take(n);
        } else {
            return new IntTakeSequence(this, n);
        }
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
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return this;
        } else if (this instanceof IntSkipTakeSequence skipTakeSequence) {
            return skipTakeSequence.skip(n);
        } else {
            return new IntSkipSequence(this, n);
        }
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
        return () -> toList().sorted().iterator();
    }

    default IntSequence sorted(IntComparator intComparator) {
        return () -> toList().sorted(intComparator).iterator();
    }

    @Override
    default IntSequence sortedDescending() {
        return sorted((IntX::compareReversed));
    }

    default IntSequence shuffled() {
        return () -> toList().shuffled().iterator();
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
        return windowed(2, w -> merger.applyAsInt(w.first(), w.last()));
    }

    default int[] toArray() {
        return toList().toArray();
    }

    default <R> R transform(@NotNull Function<? super IntSequence, ? extends R> resultMapper) {
        return resultMapper.apply(this);
    }

    default IntSequence onSequence(Consumer<? super IntSequence> consumer) {
        consumer.accept(this);
        return this;
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
