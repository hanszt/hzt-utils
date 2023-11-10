package org.hzt.utils.sequences.primitives;

import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.primitives.DoubleList;
import org.hzt.utils.collections.primitives.DoubleMutableSet;
import org.hzt.utils.function.TriFunction;
import org.hzt.utils.function.primitives.DoubleIndexedFunction;
import org.hzt.utils.iterables.primitives.DoubleCollectable;
import org.hzt.utils.iterables.primitives.DoubleGroupable;
import org.hzt.utils.iterables.primitives.DoubleNumerable;
import org.hzt.utils.iterables.primitives.DoubleReducable;
import org.hzt.utils.iterables.primitives.DoubleStreamable;
import org.hzt.utils.iterables.primitives.DoubleStringable;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterables.primitives.PrimitiveSortable;
import org.hzt.utils.iterators.Iterators;
import org.hzt.utils.iterators.primitives.DoubleFilteringIterator;
import org.hzt.utils.iterators.primitives.DoubleGeneratorIterator;
import org.hzt.utils.iterators.primitives.DoubleMultiMappingIterator;
import org.hzt.utils.iterators.primitives.DoubleSkipWhileIterator;
import org.hzt.utils.iterators.primitives.DoubleTakeWhileIterator;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.hzt.utils.numbers.DoubleX;
import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.tuples.Pair;
import org.hzt.utils.tuples.Triple;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface DoubleSequence extends DoubleWindowedSequence, DoubleReducable, DoubleCollectable, DoubleNumerable, DoubleStreamable,
        DoubleGroupable, DoubleStringable, PrimitiveSortable<DoubleComparator>,
        PrimitiveSequence<Double, DoubleConsumer, DoubleUnaryOperator, DoublePredicate, DoubleBinaryOperator> {

    static DoubleSequence empty() {
        return PrimitiveIterators::emptyDoubleIterator;
    }

    static DoubleSequence of(final Iterable<Double> iterable) {
        if (iterable instanceof OfDouble) {
            final PrimitiveIterable.OfDouble doubleIterable = (OfDouble) iterable;
            return doubleIterable::iterator;
        }
        return of(iterable, It::asDouble);
    }

    static <T> DoubleSequence of(final Iterable<T> iterable,
                                 final ToDoubleFunction<? super T> mapper) {
        return () -> PrimitiveIterators.doubleIteratorOf(iterable.iterator(), mapper);
    }

    static DoubleSequence of(final double... doubles) {
        return () -> PrimitiveIterators.doubleArrayIterator(doubles);
    }

    static DoubleSequence of(final DoubleStream stream) {
        return stream::iterator;
    }

    static DoubleSequence reverseOf(final DoubleList doubleList) {
        return () -> PrimitiveIterators.reverseIterator(doubleList);
    }

    static DoubleSequence iterate(final double seedValue, final DoubleUnaryOperator nextFunction) {
        return generate(() -> seedValue, nextFunction);
    }

    static DoubleSequence generate(final DoubleSupplier nextFunction) {
        return generate(nextFunction, t -> nextFunction.getAsDouble());
    }

    static DoubleSequence generate(final DoubleSupplier seedFunction, final DoubleUnaryOperator nextFunction) {
        return () -> DoubleGeneratorIterator.of(seedFunction, nextFunction);
    }

    default DoubleSequence plus(final double... values) {
        return Sequence.of(this, DoubleSequence.of(values)).mapMultiToDouble(OfDouble::forEachDouble);
    }

    default DoubleSequence plus(final Iterable<Double> values) {
        return Sequence.of(this, DoubleSequence.of(values)).mapMultiToDouble(OfDouble::forEachDouble);
    }

    default DoubleSequence minus(final double... values) {
        final DoubleMutableSet others = DoubleSequence.of(values).toMutableSet();
        return () -> others.isEmpty() ? iterator() : filterNot(others::contains).iterator();

    }

    default DoubleSequence minus(final Iterable<Double> values) {
        final DoubleMutableSet others = values instanceof DoubleMutableSet ?
                (DoubleMutableSet) values : DoubleSequence.of(values).toMutableSet();
        return () -> others.isEmpty() ? iterator() : filterNot(others::contains).iterator();
    }

    @Override
    default DoubleSequence distinct() {
        return () -> PrimitiveIterators.distinctIterator(iterator());
    }

    @Override
    default DoubleSequence map(final DoubleUnaryOperator mapper) {
        return () -> PrimitiveIterators.doubleTransformingIterator(iterator(), mapper);
    }

    default DoubleSequence mapIndexed(final DoubleIndexedFunction indexedFunction) {
        return () -> PrimitiveIterators.doubleIndexedTransformingIterator(iterator(), indexedFunction);
    }

    default DoubleSequence scan(final long initial, final DoubleBinaryOperator operation) {
        return () -> PrimitiveIterators.doubleScanningIterator(iterator(), initial, operation);
    }


    default DoubleSequence flatMap(final DoubleFunction<? extends Iterable<Double>> flatMapper) {
        return mapMulti((value, consumer) -> {
            final Iterable<Double> iterable = flatMapper.apply(value);
            if (iterable instanceof OfDouble) {
                ((OfDouble) iterable).forEachDouble(consumer);
            } else {
                iterable.forEach(consumer::accept);
            }
        });
    }

    default DoubleSequence mapMulti(final DoubleMapMultiConsumer mapMultiConsumer) {
        return () -> DoubleMultiMappingIterator.of(iterator(), mapMultiConsumer);
    }

    @FunctionalInterface
    interface DoubleMapMultiConsumer {
        void accept(double value, DoubleConsumer intConsumer);
    }

    default IntSequence mapToInt(final DoubleToIntFunction mapper) {
        return () -> PrimitiveIterators.doubleToIntIterator(iterator(), mapper);
    }

    default LongSequence mapToLong(final DoubleToLongFunction mapper) {
        return () -> PrimitiveIterators.doubleToLongIterator(iterator(), mapper);
    }

    default <R> Sequence<R> mapToObj(final DoubleFunction<? extends R> mapper) {
        return () -> PrimitiveIterators.doubleToObjIterator(iterator(), mapper);
    }

    @Override
    default Sequence<Double> boxed() {
        return mapToObj(Double::valueOf);
    }

    @Override
    default DoubleSequence take(final long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return PrimitiveIterators::emptyDoubleIterator;
        } else if (this instanceof DoubleSkipTakeSequence) {
            final DoubleSkipTakeSequence skipTakeSequence = (DoubleSkipTakeSequence) this;
            return skipTakeSequence.take(n);
        } else {
            return new DoubleTakeSequence(this, n);
        }
    }

    @Override
    default DoubleSequence takeWhile(final DoublePredicate predicate) {
        return () -> DoubleTakeWhileIterator.of(iterator(), predicate);
    }

    @Override
    default DoubleSequence takeWhileInclusive(final DoublePredicate predicate) {
        return () -> DoubleTakeWhileIterator.of(iterator(), predicate, true);
    }

    @Override
    default DoubleSequence skip(final long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return this;
        } else if (this instanceof DoubleSkipTakeSequence) {
            final DoubleSkipTakeSequence skipTakeSequence = (DoubleSkipTakeSequence) this;
            return skipTakeSequence.skip(n);
        } else {
            return new DoubleSkipSequence(this, n);
        }
    }

    @Override
    default DoubleSequence skipWhile(final DoublePredicate predicate) {
        return () -> DoubleSkipWhileIterator.of(iterator(), predicate, false);
    }

    @Override
    default DoubleSequence skipWhileInclusive(final DoublePredicate predicate) {
        return () -> DoubleSkipWhileIterator.of(iterator(), predicate, true);
    }

    @Override
    default DoubleSequence sorted() {
        return () -> toList().sorted().iterator();
    }

    default DoubleSequence sorted(final DoubleComparator comparator) {
        return () -> toList().sorted(comparator).iterator();
    }

    @Override
    default DoubleSequence sortedDescending() {
        return sorted(DoubleX::compareReversed);
    }

    @Override
    default DoubleSequence filter(final DoublePredicate predicate) {
        return () -> DoubleFilteringIterator.of(iterator(), predicate, true);
    }

    @Override
    default DoubleSequence filterNot(final DoublePredicate predicate) {
        return () -> DoubleFilteringIterator.of(iterator(), predicate, false);
    }

    @Override
    default DoubleSequence onEach(final DoubleConsumer consumer) {
        return map(d -> {
            consumer.accept(d);
            return d;
        });
    }

    default DoubleSequence zip(final DoubleBinaryOperator merger, final double... array) {
        final PrimitiveIterator.OfDouble iterator = PrimitiveIterators.doubleArrayIterator(array);
        return () -> PrimitiveIterators.mergingIterator(iterator(), iterator, merger);
    }

    @Override
    default DoubleSequence zip(final DoubleBinaryOperator merger, final Iterable<Double> other) {
        final PrimitiveIterator.OfDouble iterator = PrimitiveIterators.doubleIteratorOf(other.iterator(), It::asDouble);
        return () -> PrimitiveIterators.mergingIterator(iterator(), iterator, merger);
    }

    @Override
    default DoubleSequence zipWithNext(final DoubleBinaryOperator merger) {
        return windowed(2, w -> merger.applyAsDouble(w.first(), w.last()));
    }

    default double[] toArray() {
        return toList().toArray();
    }

    default <R> R transform(final Function<? super DoubleSequence, ? extends R> resultMapper) {
        return resultMapper.apply(this);
    }

    default DoubleSequence constrainOnce() {
        final AtomicBoolean consumed = new AtomicBoolean();
        return () -> Iterators.constrainOnceIterator(iterator(), consumed);
    }

    @Override
    default DoubleStream stream() {
        final int ordered = Spliterator.ORDERED;
        return StreamSupport.doubleStream(() -> Spliterators.spliteratorUnknownSize(iterator(), ordered), ordered, false);
    }

    default DoubleSequence onSequence(final Consumer<? super DoubleSequence> consumer) {
        consumer.accept(this);
        return this;
    }

    default <R1, R2, R> R doublesToTwo(final Function<? super DoubleSequence, ? extends R1> resultMapper1,
                                       final Function<? super DoubleSequence, ? extends R2> resultMapper2,
                                       final BiFunction<? super R1, ? super R2, ? extends R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this));
    }

    default <R1, R2> Pair<R1, R2> doublesToTwo(final Function<? super DoubleSequence, ? extends R1> resultMapper1,
                                               final Function<? super DoubleSequence, ? extends R2> resultMapper2) {
        return doublesToTwo(resultMapper1, resultMapper2, Pair::of);
    }

    default <R1, R2, R3, R> R doublesToThree(final Function<? super DoubleSequence, ? extends R1> resultMapper1,
                                             final Function<? super DoubleSequence, ? extends R2> resultMapper2,
                                             final Function<? super DoubleSequence, ? extends R3> resultMapper3,
                                             final TriFunction<? super R1, ? super R2, ? super R3, ? extends R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this), resultMapper3.apply(this));
    }

    default <R1, R2, R3> Triple<R1, R2, R3> doublesToThree(final Function<? super DoubleSequence, ? extends R1> resultMapper1,
                                                           final Function<? super DoubleSequence, ? extends R2> resultMapper2,
                                                           final Function<? super DoubleSequence, ? extends R3> resultMapper3) {
        return doublesToThree(resultMapper1, resultMapper2, resultMapper3, Triple::of);
    }
}
