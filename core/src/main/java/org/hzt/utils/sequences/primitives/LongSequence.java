package org.hzt.utils.sequences.primitives;

import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.primitives.LongList;
import org.hzt.utils.collections.primitives.LongMutableSet;
import org.hzt.utils.function.TriFunction;
import org.hzt.utils.function.primitives.LongIndexedFunction;
import org.hzt.utils.iterables.primitives.LongCollectable;
import org.hzt.utils.iterables.primitives.LongGroupable;
import org.hzt.utils.iterables.primitives.LongNumerable;
import org.hzt.utils.iterables.primitives.LongReducable;
import org.hzt.utils.iterables.primitives.LongStreamable;
import org.hzt.utils.iterables.primitives.LongStringable;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterables.primitives.PrimitiveSortable;
import org.hzt.utils.iterators.Iterators;
import org.hzt.utils.iterators.primitives.LongFilteringIterator;
import org.hzt.utils.iterators.primitives.LongGeneratorIterator;
import org.hzt.utils.iterators.primitives.LongMultiMappingIterator;
import org.hzt.utils.iterators.primitives.LongSkipWhileIterator;
import org.hzt.utils.iterators.primitives.LongTakeWhileIterator;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.hzt.utils.numbers.LongX;
import org.hzt.utils.primitive_comparators.LongComparator;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.tuples.Pair;
import org.hzt.utils.tuples.Triple;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Consumer;
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
public interface LongSequence extends LongWindowedSequence, LongReducable, LongCollectable, LongNumerable, LongStreamable,
        LongGroupable, LongStringable, PrimitiveSortable<LongComparator>,
        PrimitiveSequence<Long, LongConsumer, LongUnaryOperator, LongPredicate, LongBinaryOperator> {

    static LongSequence empty() {
        return PrimitiveIterators::emptyLongIterator;
    }

    static LongSequence of(final Iterable<Long> iterable) {
        if (iterable instanceof OfLong) {
            final PrimitiveIterable.OfLong longIterable = (OfLong) iterable;
            return longIterable::iterator;
        }
        return of(iterable, It::asLong);
    }

    static <T> LongSequence of(final Iterable<T> iterable, final ToLongFunction<T> mapper) {
        return () -> PrimitiveIterators.longIteratorOf(iterable.iterator(), mapper);
    }

    static LongSequence of(final long... longs) {
        return () -> PrimitiveIterators.longArrayIterator(longs);
    }

    static LongSequence of(final LongStream longStream) {
        return longStream::iterator;
    }

    static LongSequence reverseOf(final LongList longList) {
        return () -> PrimitiveIterators.reverseIterator(longList);
    }

    static LongSequence iterate(final long seedValue, final LongUnaryOperator nextFunction) {
        return generate(() -> seedValue, nextFunction);
    }

    static LongSequence generate(final LongSupplier nextFunction) {
        return generate(nextFunction, t -> nextFunction.getAsLong());
    }

    static LongSequence generate(final LongSupplier seedFunction, final LongUnaryOperator nextFunction) {
        return () -> LongGeneratorIterator.of(seedFunction, nextFunction);
    }

    default LongSequence step(final long step) {
        return filter(l -> l % step == 0);
    }

    default LongSequence plus(final long... values) {
        return Sequence.of(this, LongSequence.of(values)).mapMultiToLong(OfLong::forEachLong);
    }

    default LongSequence plus(final Iterable<Long> values) {
        return Sequence.of(this, LongSequence.of(values)).mapMultiToLong(OfLong::forEachLong);
    }

    default LongSequence minus(final long... values) {
        final LongMutableSet others = LongSequence.of(values).toMutableSet();
        return () -> others.isEmpty() ? iterator() : filterNot(others::contains).iterator();

    }

    default LongSequence minus(final Iterable<Long> values) {
        final LongMutableSet others = values instanceof LongMutableSet ? (LongMutableSet) values : LongSequence.of(values).toMutableSet();
        return () -> others.isEmpty() ? iterator() : filterNot(others::contains).iterator();
    }
    @Override
    default LongSequence distinct() {
        return () -> PrimitiveIterators.distinctIterator(iterator());
    }

    @Override
    default LongSequence map(final LongUnaryOperator unaryOperator) {
        return () -> PrimitiveIterators.longTransformingIterator(iterator(), unaryOperator);
    }

    default LongSequence mapIndexed(final LongIndexedFunction longIndexedFunction) {
        return () -> PrimitiveIterators.longIndexedTransformingIterator(iterator(), longIndexedFunction);
    }

    default LongSequence scan(final long initial, final LongBinaryOperator operation) {
        return () -> PrimitiveIterators.longScanningIterator(iterator(), initial, operation);
    }


    default LongSequence flatMap(final LongFunction<? extends Iterable<Long>> flatMapper) {
        return mapMulti((value, consumer) -> {
            final Iterable<Long> iterable = flatMapper.apply(value);
            if (iterable instanceof OfLong) {
                ((OfLong) iterable).forEachLong(consumer);
            } else {
                iterable.forEach(consumer::accept);
            }
        });
    }

    default LongSequence mapMulti(final LongMapMultiConsumer longMapMultiConsumer) {
        return () -> LongMultiMappingIterator.of(iterator(), longMapMultiConsumer);
    }

    @FunctionalInterface
    interface LongMapMultiConsumer {
        void accept(long value, LongConsumer lc);
    }

    default IntSequence mapToInt(final LongToIntFunction mapper) {
        return () -> PrimitiveIterators.longToIntIterator(iterator(), mapper);
    }

    default DoubleSequence mapToDouble(final LongToDoubleFunction mapper) {
        return () -> PrimitiveIterators.longToDoubleIterator(iterator(), mapper);
    }

    default DoubleSequence aDoubleSequence() {
        return mapToDouble(l -> l);
    }

    default <R> Sequence<R> mapToObj(final LongFunction<R> mapper) {
        return () -> PrimitiveIterators.longToObjIterator(iterator(), mapper);
    }

    default Sequence<Long> boxed() {
        return mapToObj(Long::valueOf);
    }

    default LongSequence take(final long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return PrimitiveIterators::emptyLongIterator;
        } else if (this instanceof LongSkipTakeSequence) {
            final LongSkipTakeSequence skipTakeSequence = (LongSkipTakeSequence) this;
            return skipTakeSequence.take(n);
        } else {
            return new LongTakeSequence(this, n);
        }
    }

    default LongSequence takeWhile(final LongPredicate predicate) {
        return () -> LongTakeWhileIterator.of(iterator(), predicate);
    }

    @Override
    default LongSequence takeWhileInclusive(final LongPredicate predicate) {
        return () -> LongTakeWhileIterator.of(iterator(), predicate, true);
    }

    default LongSequence skip(final long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return this;
        } else if (this instanceof LongSkipTakeSequence) {
            final LongSkipTakeSequence skipTakeSequence = (LongSkipTakeSequence) this;
            return skipTakeSequence.skip(n);
        } else {
            return new LongSkipSequence(this, n);
        }
    }

    @Override
    default LongSequence skipWhile(final LongPredicate longPredicate) {
        return () -> LongSkipWhileIterator.of(iterator(), longPredicate, false);
    }

    @Override
    default LongSequence skipWhileInclusive(final LongPredicate longPredicate) {
        return () -> LongSkipWhileIterator.of(iterator(), longPredicate, true);
    }

    @Override
    default LongSequence sorted() {
        return () -> toList().sorted().iterator();
    }

    default LongSequence sorted(final LongComparator longComparator) {
        return () -> toList().sorted(longComparator).iterator();
    }

    @Override
    default LongSequence sortedDescending() {
        return sorted(LongX::compareReversed);
    }

    default LongSequence filter(final LongPredicate predicate) {
        return () -> LongFilteringIterator.of(iterator(), predicate, true);
    }

    default LongSequence filterNot(final LongPredicate predicate) {
        return () -> LongFilteringIterator.of(iterator(), predicate, false);
    }

    default LongSequence onEach(final LongConsumer consumer) {
        return map(l -> {
            consumer.accept(l);
            return l;
        });
    }

    default LongSequence zip(final LongBinaryOperator merger, final long... array) {
        final PrimitiveIterator.OfLong iterator = PrimitiveIterators.longArrayIterator(array);
        return () -> PrimitiveIterators.mergingIterator(iterator(), iterator, merger);
    }

    @Override
    default LongSequence zip(final LongBinaryOperator merger, final Iterable<Long> other) {
        final PrimitiveIterator.OfLong iterator = PrimitiveIterators.longIteratorOf(other.iterator(), It::asLong);
        return () -> PrimitiveIterators.mergingIterator(iterator(), iterator, merger);
    }

    @Override
    default LongSequence zipWithNext(final LongBinaryOperator merger) {
        return windowed(2, w -> merger.applyAsLong(w.first(), w.last()));
    }

    default long[] toArray() {
        return toList().toArray();
    }

    default <R> R transform(final Function<? super LongSequence, ? extends R> resultMapper) {
        return resultMapper.apply(this);
    }

    default LongSequence constrainOnce() {
        final AtomicBoolean consumed = new AtomicBoolean();
        return () -> Iterators.constrainOnceIterator(iterator(), consumed);
    }

    @Override
    default LongStream stream() {
        final int ordered = Spliterator.ORDERED;
        return StreamSupport.longStream(() -> Spliterators.spliteratorUnknownSize(iterator(), ordered), ordered, false);
    }

    default LongSequence onSequence(final Consumer<? super LongSequence> consumer) {
        consumer.accept(this);
        return this;
    }

    default <R1, R2, R> R longsToTwo(final Function<? super LongSequence, ? extends R1> resultMapper1,
                                     final Function<? super LongSequence, ? extends R2> resultMapper2,
                                     final BiFunction<R1, R2, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this));
    }

    default <R1, R2> Pair<R1, R2> longsToTwo(final Function<? super LongSequence, ? extends R1> resultMapper1,
                                             final Function<? super LongSequence, ? extends R2> resultMapper2) {
        return longsToTwo(resultMapper1, resultMapper2, Pair::of);
    }

    default <R1, R2, R3, R> R longsToThree(final Function<? super LongSequence, ? extends R1> resultMapper1,
                                           final Function<? super LongSequence, ? extends R2> resultMapper2,
                                           final Function<? super LongSequence, ? extends R3> resultMapper3,
                                           final TriFunction<R1, R2, R3, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this), resultMapper3.apply(this));
    }

    default <R1, R2, R3> Triple<R1, R2, R3> longsToThree(final Function<? super LongSequence, ? extends R1> resultMapper1,
                                                         final Function<? super LongSequence, ? extends R2> resultMapper2,
                                                         final Function<? super LongSequence, ? extends R3> resultMapper3) {
        return longsToThree(resultMapper1, resultMapper2, resultMapper3, Triple::of);
    }
}
