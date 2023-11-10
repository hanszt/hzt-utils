package org.hzt.utils.streams;

import org.hzt.utils.It;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.statistics.IntStatistics;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.hzt.utils.streams.StreamXHelper.stream;

@FunctionalInterface
@SuppressWarnings("squid:S1448")
public interface IntStreamX extends IntStream, Spliterable.OfInt {

    static IntStreamX generate(final IntSupplier operator) {
        return of(IntStream.generate(operator));
    }

    static IntStreamX generate(final int initial, final IntUnaryOperator operator) {
        return of(IntStream.iterate(initial, operator));
    }

    static IntStreamX of(final int... values) {
        return new IntStreamXImpl(stream(Spliterators.spliterator(values, 0), false));
    }

    static IntStreamX of(final PrimitiveIterable.OfInt iterable) {
        return new IntStreamXImpl(stream(iterable.spliterator(), false));
    }

    static IntStreamX parallel(final PrimitiveIterable.OfInt iterable) {
        return new IntStreamXImpl(stream(iterable.spliterator(), true));
    }

    static IntStreamX of(final IntStream stream) {
        return new IntStreamXImpl(stream);
    }

    @Override
    Spliterator.OfInt spliterator();

    default IntSequence asSequence() {
        //noinspection FunctionalExpressionCanBeFolded
        return IntSequence.of(this::iterator);
    }

    @Override
    default IntStreamX filter(final IntPredicate predicate) {
        return IntStreamX.of(stream(this).filter(predicate));
    }

    @Override
    default IntStreamX map(final IntUnaryOperator mapper) {
        return IntStreamX.of(stream(this).map(mapper));
    }

    @Override
    default LongStreamX mapToLong(final IntToLongFunction mapper) {
        return LongStreamX.of(stream(this).mapToLong(mapper));
    }

    @Override
    default DoubleStreamX mapToDouble(final IntToDoubleFunction mapper) {
        return DoubleStreamX.of(stream(this).mapToDouble(mapper));
    }

    @Override
    default <U> StreamX<U> mapToObj(final IntFunction<? extends U> mapper) {
        return new StreamXImpl<>(stream(this).mapToObj(mapper));
    }

    @Override
    default IntStreamX flatMap(final IntFunction<? extends IntStream> mapper) {
        return IntStreamX.of(stream(this).flatMap(mapper));
    }

    @Override
    default IntStreamX distinct() {
        return IntStreamX.of(stream(this).distinct());
    }

    @Override
    default IntStreamX sorted() {
        return IntStreamX.of(stream(this).sorted());
    }

    @Override
    @SuppressWarnings("squid:S3864")
    default IntStreamX peek(final IntConsumer action) {
        return IntStreamX.of(stream(this).peek(action));
    }

    @Override
    default IntStreamX limit(final long maxSize) {
        return IntStreamX.of(stream(this).limit(maxSize));
    }

    @Override
    default IntStreamX skip(final long n) {
        return IntStreamX.of(stream(this).skip(n));
    }

    @Override
    default void forEach(final IntConsumer action) {
        stream(this).forEach(action);
    }

    @Override
    default void forEachOrdered(final IntConsumer action) {
        stream(this).forEachOrdered(action);
    }

    @Override
    default int[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    default int reduce(final int identity, final IntBinaryOperator accumulator) {
        return stream(this).reduce(identity, accumulator);
    }

    @Override
    default OptionalInt reduce(final IntBinaryOperator accumulator) {
        return stream(this).reduce(accumulator);
    }

    @Override
    default <R> R collect(final Supplier<R> supplier,
                          final ObjIntConsumer<R> accumulator,
                          final BiConsumer<R, R> combiner) {
        return stream(this).collect(supplier, accumulator, combiner);
    }

    default OptionalInt max() {
        return stream(this).max();
    }

    @Override
    default long count() {
        return stream(this).count();
    }

    @Override
    default int sum() {
        return reduce(0, Integer::sum);
    }

    @Override
    default OptionalInt min() {
        return stream(this).min();
    }

    @Override
    default OptionalDouble average() {
        return stream(this).average();
    }

    @Override
    default IntStatistics summaryStatistics() {
        return collect(IntStatistics::new, IntStatistics::accept, IntStatistics::combine);
    }

    @Override
    default LongStream asLongStream() {
        return mapToLong(It::asLong);
    }

    @Override
    default DoubleStream asDoubleStream(){
        return mapToDouble(It::asDouble);
    }

    @Override
    default StreamX<Integer> boxed() {
        return new StreamXImpl<>(stream(this).boxed());
    }

    @Override
    default boolean anyMatch(final IntPredicate predicate) {
        return stream(this).anyMatch(predicate);
    }

    @Override
    default boolean allMatch(final IntPredicate predicate) {
        return stream(this).allMatch(predicate);
    }

    @Override
    default boolean noneMatch(final IntPredicate predicate) {
        return stream(this).noneMatch(predicate);
    }

    @Override
    default OptionalInt findFirst() {
        return stream(this).findFirst();
    }

    @Override
    default OptionalInt findAny() {
        return stream(this).findAny();
    }

    @Override
    default PrimitiveIterator.OfInt iterator() {
        return Spliterators.iterator(spliterator());
    }

    @Override
    default boolean isParallel() {
        throw new UnsupportedOperationException("isParallel() not supported in IntStreamX interface");
    }

    @Override
    default IntStreamX sequential() {
        return IntStreamX.of(stream(spliterator(), false));
    }

    @Override
    default IntStreamX parallel() {
        return IntStreamX.of(stream(spliterator(), true));
    }

    @Override
    default IntStreamX unordered() {
        return IntStreamX.of(stream(this).unordered());
    }

    @Override
    default IntStreamX onClose(final Runnable closeHandler) {
        return this;
    }

    @Override
    default void close() {
        throw new UnsupportedOperationException("Not supported in IntStreamX interface");
    }

}
