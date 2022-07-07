package org.hzt.utils.streams;

import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.statistics.DoubleStatistics;
import org.jetbrains.annotations.NotNull;

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
import java.util.stream.StreamSupport;

@FunctionalInterface
@SuppressWarnings("squid:S1448")
public interface DoubleStreamX extends DoubleStream, Spliterable.OfDouble {

    static DoubleStreamX generate(DoubleSupplier operator) {
        return of(DoubleStream.generate(operator));
    }

    static DoubleStreamX generate(double initial, DoubleUnaryOperator operator) {
        return of(DoubleStream.iterate(initial, operator));
    }

    static DoubleStreamX of(@NotNull PrimitiveIterable.OfDouble iterable) {
        return new DoubleStreamXImpl(stream(iterable.spliterator(), false));
    }

    static DoubleStreamX parallel(@NotNull PrimitiveIterable.OfDouble iterable) {
        return new DoubleStreamXImpl(stream(iterable.spliterator(), true));
    }

    static DoubleStreamX of(DoubleStream stream) {
        return new DoubleStreamXImpl(stream);
    }

    @NotNull
    @Override
    Spliterator.OfDouble spliterator();

    private DoubleStream stream() {
        final var spliterator = spliterator();
        final var parallel = this instanceof DoubleStreamXImpl && isParallel();
        return stream(spliterator, parallel);
    }

    private static DoubleStream stream(Spliterator.OfDouble spliterator, boolean parallel) {
        if (spliterator.hasCharacteristics(Spliterator.IMMUTABLE) ||
                spliterator.hasCharacteristics(Spliterator.CONCURRENT)) {
            return StreamSupport.doubleStream(spliterator, parallel);
        }
        return StreamSupport.doubleStream(() -> spliterator, spliterator.characteristics(), parallel);
    }

    default DoubleSequence asSequence() {
        //noinspection FunctionalExpressionCanBeFolded
        return DoubleSequence.of(this::iterator);
    }

    @Override
    default DoubleStreamX filter(DoublePredicate predicate) {
        return DoubleStreamX.of(stream().filter(predicate));
    }

    @Override
    default DoubleStreamX map(DoubleUnaryOperator mapper) {
        return DoubleStreamX.of(stream().map(mapper));
    }

    @Override
    default IntStreamX mapToInt(DoubleToIntFunction mapper) {
        return new IntStreamXImpl(stream().mapToInt(mapper));
    }

    @Override
    default LongStream mapToLong(DoubleToLongFunction mapper) {
        return new LongStreamXImpl(stream().mapToLong(mapper));
    }

    @Override
    default <U> StreamX<U> mapToObj(DoubleFunction<? extends U> mapper) {
        return new StreamXImpl<>(stream().mapToObj(mapper));
    }

    @Override
    default DoubleStreamX flatMap(DoubleFunction<? extends DoubleStream> mapper) {
        return DoubleStreamX.of(stream().flatMap(mapper));
    }

    @Override
    default DoubleStreamX distinct() {
        return DoubleStreamX.of(stream().distinct());
    }

    @Override
    default DoubleStreamX sorted() {
        return DoubleStreamX.of(stream().sorted());
    }

    @Override
    @SuppressWarnings("squid:S3864")
    default DoubleStreamX peek(DoubleConsumer action) {
        return DoubleStreamX.of(stream().peek(action));
    }

    @Override
    default DoubleStreamX limit(long maxSize) {
        return DoubleStreamX.of(stream().limit(maxSize));
    }

    @Override
    default DoubleStreamX skip(long n) {
        return DoubleStreamX.of(stream().skip(n));
    }

    @Override
    default void forEach(DoubleConsumer action) {
        stream().forEach(action);
    }

    @Override
    default void forEachOrdered(DoubleConsumer action) {
        stream().forEachOrdered(action);
    }

    @Override
    default double @NotNull [] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    default double reduce(double identity, DoubleBinaryOperator accumulator) {
        return stream().reduce(identity, accumulator);
    }

    @NotNull
    @Override
    default OptionalDouble reduce(DoubleBinaryOperator accumulator) {
        return stream().reduce(accumulator);
    }

    @Override
    default <R> R collect(Supplier<R> supplier,
                          ObjDoubleConsumer<R> accumulator,
                          BiConsumer<R, R> combiner) {
        return stream().collect(supplier, accumulator, combiner);
    }

    @NotNull
    default OptionalDouble max() {
        return stream().max();
    }


    @Override
    default long count() {
        return stream().count();
    }

    @Override
    default double sum() {
        return reduce(0, Double::sum);
    }

    @Override
    default OptionalDouble min() {
        return stream().min();
    }

    @Override
    default OptionalDouble average() {
        return stream().average();
    }

    @Override
    default DoubleStatistics summaryStatistics() {
        return collect(DoubleStatistics::new, DoubleStatistics::accept, DoubleStatistics::combine);
    }

    @Override
    default StreamX<Double> boxed() {
        return new StreamXImpl<>(stream().boxed());
    }

    @Override
    default boolean anyMatch(DoublePredicate predicate) {
        return stream().anyMatch(predicate);
    }

    @Override
    default boolean allMatch(DoublePredicate predicate) {
        return stream().allMatch(predicate);
    }

    @Override
    default boolean noneMatch(DoublePredicate predicate) {
        return stream().noneMatch(predicate);
    }

    @NotNull
    @Override
    default OptionalDouble findFirst() {
        return stream().findFirst();
    }

    @Override
    default OptionalDouble findAny() {
        return stream().findAny();
    }

    @NotNull
    @Override
    default PrimitiveIterator.OfDouble iterator() {
        return Spliterators.iterator(spliterator());
    }

    @Override
    default boolean isParallel() {
        throw new UnsupportedOperationException("isParallel() not supported in DoubleStreamX interface");
    }

    @NotNull
    @Override
    default DoubleStreamX sequential() {
        return DoubleStreamX.of(stream(spliterator(), false));
    }

    @NotNull
    @Override
    default DoubleStreamX parallel() {
        return DoubleStreamX.of(stream(spliterator(), true));
    }

    @NotNull
    @Override
    default DoubleStreamX unordered() {
        return DoubleStreamX.of(stream().unordered());
    }

    @NotNull
    @Override
    default DoubleStreamX onClose(Runnable closeHandler) {
        return this;
    }

    @Override
    default void close() {
        throw new UnsupportedOperationException("Not supported in DoubleStreamX interface");
    }

    @Override
    default DoubleStreamX takeWhile(DoublePredicate predicate) {
        return DoubleStreamX.of(DoubleStream.super.takeWhile(predicate));
    }

    @Override
    default DoubleStreamX dropWhile(DoublePredicate predicate) {
        return DoubleStreamX.of(DoubleStream.super.dropWhile(predicate));
    }
}
