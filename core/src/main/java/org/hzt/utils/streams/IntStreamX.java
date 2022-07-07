package org.hzt.utils.streams;

import org.hzt.utils.It;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.statistics.IntStatistics;
import org.jetbrains.annotations.NotNull;

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

    static IntStreamX generate(IntSupplier operator) {
        return of(IntStream.generate(operator));
    }

    static IntStreamX generate(int initial, IntUnaryOperator operator) {
        return of(IntStream.iterate(initial, operator));
    }

    static IntStreamX of(int @NotNull ... values) {
        return new IntStreamXImpl(stream(Spliterators.spliterator(values, 0), false));
    }

    static IntStreamX of(@NotNull PrimitiveIterable.OfInt iterable) {
        return new IntStreamXImpl(stream(iterable.spliterator(), false));
    }

    static IntStreamX parallel(@NotNull PrimitiveIterable.OfInt iterable) {
        return new IntStreamXImpl(stream(iterable.spliterator(), true));
    }

    static IntStreamX of(IntStream stream) {
        return new IntStreamXImpl(stream);
    }

    @NotNull
    @Override
    Spliterator.OfInt spliterator();

    default IntSequence asSequence() {
        //noinspection FunctionalExpressionCanBeFolded
        return IntSequence.of(this::iterator);
    }

    @Override
    default IntStreamX filter(IntPredicate predicate) {
        return IntStreamX.of(stream(this).filter(predicate));
    }

    @Override
    default  IntStreamX map(IntUnaryOperator mapper) {
        return IntStreamX.of(stream(this).map(mapper));
    }

    @Override
    default LongStreamX mapToLong(IntToLongFunction mapper) {
        return LongStreamX.of(stream(this).mapToLong(mapper));
    }

    @Override
    default DoubleStreamX mapToDouble(IntToDoubleFunction mapper) {
        return DoubleStreamX.of(stream(this).mapToDouble(mapper));
    }

    @Override
    default <U> StreamX<U> mapToObj(IntFunction<? extends U> mapper) {
        return new StreamXImpl<>(stream(this).mapToObj(mapper));
    }

    @Override
    default IntStreamX flatMap(IntFunction<? extends IntStream> mapper) {
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
    default IntStreamX peek(IntConsumer action) {
        return IntStreamX.of(stream(this).peek(action));
    }

    @Override
    default IntStreamX limit(long maxSize) {
        return IntStreamX.of(stream(this).limit(maxSize));
    }

    @Override
    default IntStreamX skip(long n) {
        return IntStreamX.of(stream(this).skip(n));
    }

    @Override
    default void forEach(IntConsumer action) {
        stream(this).forEach(action);
    }

    @Override
    default void forEachOrdered(IntConsumer action) {
        stream(this).forEachOrdered(action);
    }

    @Override
    default int @NotNull [] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    default int reduce(int identity, IntBinaryOperator accumulator) {
        return stream(this).reduce(identity, accumulator);
    }

    @NotNull
    @Override
    default OptionalInt reduce(IntBinaryOperator accumulator) {
        return stream(this).reduce(accumulator);
    }

    @Override
    default <R> R collect(Supplier<R> supplier,
                          ObjIntConsumer<R> accumulator,
                          BiConsumer<R, R> combiner) {
        return stream(this).collect(supplier, accumulator, combiner);
    }

    @NotNull
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
    default boolean anyMatch(IntPredicate predicate) {
        return stream(this).anyMatch(predicate);
    }

    @Override
    default boolean allMatch(IntPredicate predicate) {
        return stream(this).allMatch(predicate);
    }

    @Override
    default boolean noneMatch(IntPredicate predicate) {
        return stream(this).noneMatch(predicate);
    }

    @NotNull
    @Override
    default OptionalInt findFirst() {
        return stream(this).findFirst();
    }

    @Override
    default OptionalInt findAny() {
        return stream(this).findAny();
    }

    @NotNull
    @Override
    default PrimitiveIterator.OfInt iterator() {
        return Spliterators.iterator(spliterator());
    }

    @Override
    default boolean isParallel() {
        throw new UnsupportedOperationException("isParallel() not supported in IntStreamX interface");
    }

    @NotNull
    @Override
    default IntStreamX sequential() {
        return IntStreamX.of(stream(spliterator(), false));
    }

    @NotNull
    @Override
    default IntStreamX parallel() {
        return IntStreamX.of(stream(spliterator(), true));
    }

    @NotNull
    @Override
    default IntStreamX unordered() {
        return IntStreamX.of(stream(this).unordered());
    }

    @NotNull
    @Override
    default IntStreamX onClose(Runnable closeHandler) {
        return this;
    }

    @Override
    default void close() {
        throw new UnsupportedOperationException("Not supported in IntStreamX interface");
    }

}
