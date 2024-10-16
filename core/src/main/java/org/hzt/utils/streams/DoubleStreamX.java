package org.hzt.utils.streams;

import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.statistics.DoubleStatistics;

import java.util.OptionalDouble;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.ObjDoubleConsumer;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.LongStream;

import static org.hzt.utils.streams.StreamXHelper.stream;

@FunctionalInterface
@SuppressWarnings("squid:S1448")
public interface DoubleStreamX extends DoubleStream, Spliterable.OfDouble {

    static DoubleStreamX generate(final DoubleSupplier operator) {
        return of(DoubleStream.generate(operator));
    }

    static DoubleStreamX generate(final double initial, final DoubleUnaryOperator operator) {
        return of(DoubleStream.iterate(initial, operator));
    }

    static DoubleStreamX of(final PrimitiveIterable.OfDouble iterable) {
        return new DoubleStreamXImpl(stream(iterable.spliterator(), false));
    }

    static DoubleStreamX parallel(final PrimitiveIterable.OfDouble iterable) {
        return new DoubleStreamXImpl(stream(iterable.spliterator(), true));
    }

    static DoubleStreamX of(final DoubleStream stream) {
        return new DoubleStreamXImpl(stream);
    }

    @Override
    Spliterator.OfDouble spliterator();

    default DoubleSequence asSequence() {
        //noinspection FunctionalExpressionCanBeFolded
        return DoubleSequence.of(this::iterator);
    }

    @Override
    default DoubleStreamX filter(final DoublePredicate predicate) {
        return DoubleStreamX.of(stream(this).filter(predicate));
    }

    @Override
    default DoubleStreamX map(final DoubleUnaryOperator mapper) {
        return DoubleStreamX.of(stream(this).map(mapper));
    }

    @Override
    default IntStreamX mapToInt(final DoubleToIntFunction mapper) {
        return new IntStreamXImpl(stream(this).mapToInt(mapper));
    }

    @Override
    default LongStream mapToLong(final DoubleToLongFunction mapper) {
        return new LongStreamXImpl(stream(this).mapToLong(mapper));
    }

    @Override
    default <U> StreamX<U> mapToObj(final DoubleFunction<? extends U> mapper) {
        return new StreamXImpl<>(stream(this).mapToObj(mapper));
    }

    @Override
    default DoubleStreamX flatMap(final DoubleFunction<? extends DoubleStream> mapper) {
        return DoubleStreamX.of(stream(this).flatMap(mapper));
    }

    @Override
    default DoubleStreamX distinct() {
        return DoubleStreamX.of(stream(this).distinct());
    }

    @Override
    default DoubleStreamX sorted() {
        return DoubleStreamX.of(stream(this).sorted());
    }

    @Override
    @SuppressWarnings("squid:S3864")
    default DoubleStreamX peek(final DoubleConsumer action) {
        return DoubleStreamX.of(stream(this).peek(action));
    }

    @Override
    default DoubleStreamX limit(final long maxSize) {
        return DoubleStreamX.of(stream(this).limit(maxSize));
    }

    @Override
    default DoubleStreamX skip(final long n) {
        return DoubleStreamX.of(stream(this).skip(n));
    }

    @Override
    default void forEach(final DoubleConsumer action) {
        stream(this).forEach(action);
    }

    @Override
    default void forEachOrdered(final DoubleConsumer action) {
        stream(this).forEachOrdered(action);
    }

    @Override
    default double[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    default double reduce(final double identity, final DoubleBinaryOperator accumulator) {
        return stream(this).reduce(identity, accumulator);
    }

    @Override
    default OptionalDouble reduce(final DoubleBinaryOperator accumulator) {
        return stream(this).reduce(accumulator);
    }

    @Override
    default <R> R collect(final Supplier<R> supplier,
                          final ObjDoubleConsumer<R> accumulator,
                          final BiConsumer<R, R> combiner) {
        return stream(this).collect(supplier, accumulator, combiner);
    }

    default OptionalDouble max() {
        return stream(this).max();
    }

    @Override
    default long count() {
        return stream(this).count();
    }

    @Override
    default double sum() {
        return reduce(0, Double::sum);
    }

    @Override
    default OptionalDouble min() {
        return stream(this).min();
    }

    @Override
    default OptionalDouble average() {
        return stream(this).average();
    }

    @Override
    default DoubleStatistics summaryStatistics() {
        return collect(DoubleStatistics::new, DoubleStatistics::accept, DoubleStatistics::combine);
    }

    @Override
    default StreamX<Double> boxed() {
        return new StreamXImpl<>(stream(this).boxed());
    }

    @Override
    default boolean anyMatch(final DoublePredicate predicate) {
        return stream(this).anyMatch(predicate);
    }

    @Override
    default boolean allMatch(final DoublePredicate predicate) {
        return stream(this).allMatch(predicate);
    }

    @Override
    default boolean noneMatch(final DoublePredicate predicate) {
        return stream(this).noneMatch(predicate);
    }

    @Override
    default OptionalDouble findFirst() {
        return stream(this).findFirst();
    }

    @Override
    default OptionalDouble findAny() {
        return stream(this).findAny();
    }

    @Override
    default PrimitiveIterator.OfDouble iterator() {
        return Spliterators.iterator(spliterator());
    }

    @Override
    default boolean isParallel() {
        throw new UnsupportedOperationException("isParallel() not supported in DoubleStreamX interface");
    }

    @Override
    default DoubleStreamX sequential() {
        return DoubleStreamX.of(stream(spliterator(), false));
    }

    @Override
    default DoubleStreamX parallel() {
        return DoubleStreamX.of(stream(spliterator(), true));
    }

    @Override
    default DoubleStreamX unordered() {
        return DoubleStreamX.of(stream(this).unordered());
    }

    @Override
    default DoubleStreamX onClose(final Runnable closeHandler) {
        return this;
    }

    @Override
    default void close() {
        throw new UnsupportedOperationException("Not supported in DoubleStreamX interface");
    }
}
