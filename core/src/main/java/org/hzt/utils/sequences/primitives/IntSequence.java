package org.hzt.utils.sequences.primitives;

import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.IntMutableSet;
import org.hzt.utils.function.TriFunction;
import org.hzt.utils.gatherers.Gatherer;
import org.hzt.utils.gatherers.primitives.IntGatherer;
import org.hzt.utils.iterables.primitives.IntGatherable;
import org.hzt.utils.iterables.primitives.IntGroupable;
import org.hzt.utils.iterables.primitives.IntNumerable;
import org.hzt.utils.iterables.primitives.IntReducable;
import org.hzt.utils.iterables.primitives.IntStreamable;
import org.hzt.utils.iterables.primitives.IntStringable;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterables.primitives.PrimitiveSortable;
import org.hzt.utils.iterators.Iterators;
import org.hzt.utils.iterators.primitives.IntFilteringIterator;
import org.hzt.utils.iterators.primitives.IntMultiMappingIterator;
import org.hzt.utils.iterators.primitives.IntSkipWhileIterator;
import org.hzt.utils.iterators.primitives.IntTakeWhileIterator;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.tuples.Pair;
import org.hzt.utils.tuples.Triple;

import java.util.Random;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicBoolean;
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
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface IntSequence extends IntWindowedSequence, IntReducable, IntGatherable, IntNumerable,
        IntStreamable, IntGroupable, IntStringable, PrimitiveSortable<IntComparator>,
        PrimitiveSequence<Integer, IntConsumer, IntUnaryOperator, IntPredicate, IntBinaryOperator> {

    static IntSequence empty() {
        return PrimitiveIterators::emptyIntIterator;
    }

    static IntSequence of(final Iterable<Integer> iterable) {
        if (iterable instanceof final PrimitiveIterable.OfInt iterableOfInt) {
            return iterableOfInt::iterator;
        }
        return of(iterable, It::asInt);
    }

    static <T> IntSequence of(final Iterable<T> iterable, final ToIntFunction<T> mapper) {
        return () -> PrimitiveIterators.intIteratorOf(iterable.iterator(), mapper);
    }

    static IntSequence of(final int... array) {
        return () -> PrimitiveIterators.intArrayIterator(array);
    }

    static IntSequence of(final IntStream stream) {
        return stream::iterator;
    }

    static IntSequence reverseOf(final IntList intList) {
        return () -> PrimitiveIterators.reverseIterator(intList);
    }

    static IntSequence iterate(final int seedValue, final IntUnaryOperator nextFunction) {
        return generate(() -> seedValue, nextFunction);
    }

    static IntSequence generate(final IntSupplier nextFunction) {
        return generate(nextFunction, t -> nextFunction.getAsInt());
    }

    static IntSequence generate(final IntSupplier seedFunction, final IntUnaryOperator nextFunction) {
        return () -> PrimitiveIterators.generatorIterator(seedFunction, nextFunction);
    }

    default IntSequence step(final int step) {
        return filter(i -> i % step == 0);
    }

    default IntSequence plus(final int... values) {
        return Sequence.of(this, IntSequence.of(values)).mapMultiToInt(OfInt::forEachInt);
    }

    default IntSequence plus(final Iterable<Integer> values) {
        return Sequence.of(this, IntSequence.of(values)).mapMultiToInt(OfInt::forEachInt);
    }

    default IntSequence minus(final int... values) {
        final var others = IntSequence.of(values).toMutableSet();
        return () -> others.isEmpty() ? iterator() : filterNot(others::contains).iterator();

    }

    default IntSequence minus(final Iterable<Integer> values) {
        final var others = values instanceof IntMutableSet ? (IntMutableSet) values : IntSequence.of(values).toMutableSet();
        return () -> others.isEmpty() ? iterator() : filterNot(others::contains).iterator();
    }

    @Override
    default IntSequence distinct() {
        return () -> PrimitiveIterators.distinctIterator(iterator());
    }

    @Override
    default IntSequence map(final IntUnaryOperator mapper) {
        return () -> PrimitiveIterators.intTransformingIterator(iterator(), mapper);
    }

    default IntSequence mapIndexed(final IntBinaryOperator indexedFunction) {
        return () -> PrimitiveIterators.intIndexedTransformingIterator(iterator(), indexedFunction);
    }

    default IntSequence scan(final int initial, final IntBinaryOperator operation) {
        return () -> PrimitiveIterators.intScanningIterator(iterator(), initial, operation);
    }

    default IntSequence flatMap(final IntFunction<? extends Iterable<Integer>> flatMapper) {
        return mapMulti((value, intConsumer) -> consumeForEach(flatMapper.apply(value), intConsumer));
    }

    private static void consumeForEach(final Iterable<Integer> iterable, final IntConsumer consumer) {
        if (iterable instanceof final OfInt iterableOfInt) {
            iterableOfInt.forEachInt(consumer);
        } else {
            iterable.forEach(consumer::accept);
        }
    }

    default IntSequence mapMulti(final IntMapMultiConsumer intMapMultiConsumer) {
        return () -> IntMultiMappingIterator.of(iterator(), intMapMultiConsumer);
    }

    @Override
    default <A, R> Sequence<R> gather(final Gatherer<Integer, A, R> gatherer) {
        return gatherer instanceof IntGatherer<A, R> intGatherer ?
                (() -> PrimitiveIterators.intGatheringIterator(iterator(), intGatherer)) :
                (() -> Iterators.gatheringIterator(iterator(), gatherer));
    }

    @FunctionalInterface
    interface IntMapMultiConsumer {
        void accept(int value, IntConsumer intConsumer);
    }

    default LongSequence mapToLong(final IntToLongFunction mapper) {
        return () -> PrimitiveIterators.intToLongIterator(iterator(), mapper);
    }

    default LongSequence asLongSequence() {
        return mapToLong(i -> i);
    }

    default DoubleSequence mapToDouble(final IntToDoubleFunction mapper) {
        return () -> PrimitiveIterators.intToDoubleIterator(iterator(), mapper);
    }

    default DoubleSequence asDoubleSequence() {
        return mapToDouble(i -> i);
    }

    default <R> Sequence<R> mapToObj(final IntFunction<R> function) {
        return () -> PrimitiveIterators.intToObjIterator(iterator(), function);
    }

    default Sequence<Integer> boxed() {
        return mapToObj(Integer::valueOf);
    }

    @Override
    default IntSequence take(final long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return PrimitiveIterators::emptyIntIterator;
        } else if (this instanceof final IntSkipTakeSequence skipTakeSequence) {
            return skipTakeSequence.take(n);
        } else {
            return new IntTakeSequence(this, n);
        }
    }

    @Override
    default IntSequence takeWhile(final IntPredicate predicate) {
        return () -> IntTakeWhileIterator.of(iterator(), predicate);
    }

    @Override
    default IntSequence takeWhileInclusive(final IntPredicate predicate) {
        return () -> IntTakeWhileIterator.of(iterator(), predicate, true);
    }

    @Override
    default IntSequence skip(final long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return this;
        } else if (this instanceof final IntSkipTakeSequence skipTakeSequence) {
            return skipTakeSequence.skip(n);
        } else {
            return new IntSkipSequence(this, n);
        }
    }

    @Override
    default IntSequence skipWhile(final IntPredicate predicate) {
        return () -> IntSkipWhileIterator.of(iterator(), predicate, false);
    }

    @Override
    default IntSequence skipWhileInclusive(final IntPredicate predicate) {
        return () -> IntSkipWhileIterator.of(iterator(), predicate, true);
    }

    @Override
    default IntSequence sorted() {
        return () -> toList().sorted().iterator();
    }

    default IntSequence sorted(final IntComparator intComparator) {
        return () -> toList().sorted(intComparator).iterator();
    }

    @Override
    default IntSequence sortedDescending() {
        return sorted((IntX::compareReversed));
    }

    default IntSequence shuffled(final Random random) {
        return () -> toList().shuffled(random).iterator();
    }

    @Override
    default IntSequence filter(final IntPredicate predicate) {
        return () -> IntFilteringIterator.of(iterator(), predicate, true);
    }

    default IntSequence filterNot(final IntPredicate predicate) {
        return () -> IntFilteringIterator.of(iterator(), predicate, false);
    }

    default IntSequence onEach(final IntConsumer consumer) {
        return map(i -> {
            consumer.accept(i);
            return i;
        });
    }

    default IntSequence zip(final IntBinaryOperator merger, final int... array) {
        final var iterator = PrimitiveIterators.intArrayIterator(array);
        return () -> PrimitiveIterators.mergingIterator(iterator(), iterator, merger);
    }

    @Override
    default IntSequence zip(final IntBinaryOperator merger, final Iterable<Integer> other) {
        final var iterator = PrimitiveIterators.intIteratorOf(other.iterator(), It::asInt);
        return () -> PrimitiveIterators.mergingIterator(iterator(), iterator, merger);
    }

    @Override
    default IntSequence zipWithNext(final IntBinaryOperator merger) {
        return windowed(2, w -> merger.applyAsInt(w.first(), w.last()));
    }

    default int[] toArray() {
        return toList().toArray();
    }

    default <R> R transform(final Function<? super IntSequence, ? extends R> resultMapper) {
        return resultMapper.apply(this);
    }

    default IntSequence constrainOnce() {
        final var consumed = new AtomicBoolean();
        return () -> Iterators.constrainOnceIterator(iterator(), consumed);
    }

    @Override
    default IntStream stream() {
        final var ordered = Spliterator.ORDERED;
        return StreamSupport.intStream(() -> Spliterators.spliteratorUnknownSize(iterator(), ordered), ordered, false);
    }

    default IntSequence onSequence(final Consumer<? super IntSequence> consumer) {
        consumer.accept(this);
        return this;
    }

    default <R1, R2, R> R intsToTwo(final Function<? super IntSequence, ? extends R1> resultMapper1,
                                    final Function<? super IntSequence, ? extends R2> resultMapper2,
                                    final BiFunction<R1, R2, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this));
    }

    default <R1, R2> Pair<R1, R2> intsToTwo(final Function<? super IntSequence, ? extends R1> resultMapper1,
                                            final Function<? super IntSequence, ? extends R2> resultMapper2) {
        return intsToTwo(resultMapper1, resultMapper2, Pair::of);
    }

    default <R1, R2, R3, R> R intsToThree(final Function<? super IntSequence, ? extends R1> resultMapper1,
                                          final Function<? super IntSequence, ? extends R2> resultMapper2,
                                          final Function<? super IntSequence, ? extends R3> resultMapper3,
                                          final TriFunction<R1, R2, R3, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this), resultMapper3.apply(this));
    }

    default <R1, R2, R3> Triple<R1, R2, R3> intsToThree(final Function<? super IntSequence, ? extends R1> resultMapper1,
                                                        final Function<? super IntSequence, ? extends R2> resultMapper2,
                                                        final Function<? super IntSequence, ? extends R3> resultMapper3) {
        return intsToThree(resultMapper1, resultMapper2, resultMapper3, Triple::of);
    }
}
