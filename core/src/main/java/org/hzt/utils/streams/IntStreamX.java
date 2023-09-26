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
import java.util.stream.StreamSupport;

@FunctionalInterface
@SuppressWarnings("squid:S1448")
public interface IntStreamX extends IntStream, Spliterable.OfInt {

    static IntStreamX generate(final IntSupplier operator) {
        return of(IntStream.generate(operator));
    }

    static IntStreamX generate(final int initial, final IntUnaryOperator operator) {
        return of(IntStream.iterate(initial, operator));
    }

    static IntStreamX of(final int @NotNull ... values) {
        return new IntStreamXImpl(stream(Spliterators.spliterator(values, 0), false));
    }

    static IntStreamX of(@NotNull final PrimitiveIterable.OfInt iterable) {
        return new IntStreamXImpl(stream(iterable.spliterator(), false));
    }

    static IntStreamX parallel(@NotNull final PrimitiveIterable.OfInt iterable) {
        return new IntStreamXImpl(stream(iterable.spliterator(), true));
    }

    static IntStreamX of(final IntStream stream) {
        return new IntStreamXImpl(stream);
    }

    @NotNull
    @Override
    Spliterator.OfInt spliterator();

    private IntStream stream() {
        final var spliterator = spliterator();
        final var parallel = this instanceof IntStreamXImpl && isParallel();
        return stream(spliterator, parallel);
    }

    private static IntStream stream(final Spliterator.OfInt spliterator, final boolean parallel) {
        if (spliterator.hasCharacteristics(Spliterator.IMMUTABLE) ||
                spliterator.hasCharacteristics(Spliterator.CONCURRENT)) {
            return StreamSupport.intStream(spliterator, parallel);
        }
        return StreamSupport.intStream(() -> spliterator, spliterator.characteristics(), parallel);
    }

    default IntSequence asSequence() {
        //noinspection FunctionalExpressionCanBeFolded
        return IntSequence.of(this::iterator);
    }

    @Override
    default IntStreamX filter(final IntPredicate predicate) {
        return IntStreamX.of(stream().filter(predicate));
    }

    @Override
    default  IntStreamX map(final IntUnaryOperator mapper) {
        return IntStreamX.of(stream().map(mapper));
    }

    @Override
    default LongStreamX mapToLong(final IntToLongFunction mapper) {
        return LongStreamX.of(stream().mapToLong(mapper));
    }

    @Override
    default DoubleStreamX mapToDouble(final IntToDoubleFunction mapper) {
        return DoubleStreamX.of(stream().mapToDouble(mapper));
    }

    @Override
    default <U> StreamX<U> mapToObj(final IntFunction<? extends U> mapper) {
        return new StreamXImpl<>(stream().mapToObj(mapper));
    }

    @Override
    default IntStreamX flatMap(final IntFunction<? extends IntStream> mapper) {
        return IntStreamX.of(stream().flatMap(mapper));
    }

    @Override
    default IntStreamX distinct() {
        return IntStreamX.of(stream().distinct());
    }

    @Override
    default IntStreamX sorted() {
        return IntStreamX.of(stream().sorted());
    }

    @Override
    @SuppressWarnings("squid:S3864")
    default IntStreamX peek(final IntConsumer action) {
        return IntStreamX.of(stream().peek(action));
    }

    @Override
    default IntStreamX limit(final long maxSize) {
        return IntStreamX.of(stream().limit(maxSize));
    }

    @Override
    default IntStreamX skip(final long n) {
        return IntStreamX.of(stream().skip(n));
    }

    @Override
    default void forEach(final IntConsumer action) {
        stream().forEach(action);
    }

    @Override
    default void forEachOrdered(final IntConsumer action) {
        stream().forEachOrdered(action);
    }

    @Override
    default int @NotNull [] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    default int reduce(final int identity, final IntBinaryOperator accumulator) {
        return stream().reduce(identity, accumulator);
    }

    @NotNull
    @Override
    default OptionalInt reduce(final IntBinaryOperator accumulator) {
        return stream().reduce(accumulator);
    }

    @Override
    default <R> R collect(final Supplier<R> supplier,
                          final ObjIntConsumer<R> accumulator,
                          final BiConsumer<R, R> combiner) {
        return stream().collect(supplier, accumulator, combiner);
    }

    @NotNull
    default OptionalInt max() {
        return stream().max();
    }


    @Override
    default long count() {
        return stream().count();
    }

    @Override
    default int sum() {
        return reduce(0, Integer::sum);
    }

    @Override
    default OptionalInt min() {
        return stream().min();
    }

    @Override
    default OptionalDouble average() {
        return stream().average();
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
        return new StreamXImpl<>(stream().boxed());
    }

    @Override
    default boolean anyMatch(final IntPredicate predicate) {
        return stream().anyMatch(predicate);
    }

    @Override
    default boolean allMatch(final IntPredicate predicate) {
        return stream().allMatch(predicate);
    }

    @Override
    default boolean noneMatch(final IntPredicate predicate) {
        return stream().noneMatch(predicate);
    }

    @NotNull
    @Override
    default OptionalInt findFirst() {
        return stream().findFirst();
    }

    @Override
    default OptionalInt findAny() {
        return stream().findAny();
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
        return IntStreamX.of(stream().unordered());
    }

    @NotNull
    @Override
    default IntStreamX onClose(final Runnable closeHandler) {
        return this;
    }

    @Override
    default void close() {
        throw new UnsupportedOperationException("Not supported in IntStreamX interface");
    }

    @Override
    default IntStreamX takeWhile(final IntPredicate predicate) {
        return IntStreamX.of(IntStream.super.takeWhile(predicate));
    }

    @Override
    default IntStreamX dropWhile(final IntPredicate predicate) {
        return IntStreamX.of(IntStream.super.dropWhile(predicate));
    }
}
