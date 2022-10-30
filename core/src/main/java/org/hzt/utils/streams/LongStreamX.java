package org.hzt.utils.streams;

import org.hzt.utils.It;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.hzt.utils.statistics.LongStatistics;
import org.jetbrains.annotations.NotNull;

import java.util.LongSummaryStatistics;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

@FunctionalInterface
@SuppressWarnings("squid:S1448")
public interface LongStreamX extends LongStream, Spliterable.OfLong {

    static LongStreamX generate(LongSupplier operator) {
        return of(LongStream.generate(operator));
    }

    static LongStreamX generate(long initial, LongUnaryOperator operator) {
        return of(LongStream.iterate(initial, operator));
    }

    static LongStreamX of(long @NotNull ... values) {
        return new LongStreamXImpl(stream(Spliterators.spliterator(values, 0), false));
    }

    static LongStreamX of(@NotNull PrimitiveIterable.OfLong iterable) {
        return new LongStreamXImpl(stream(iterable.spliterator(), false));
    }

    static LongStreamX parallel(@NotNull PrimitiveIterable.OfLong iterable) {
        return new LongStreamXImpl(stream(iterable.spliterator(), true));
    }

    static LongStreamX of(LongStream stream) {
        return new LongStreamXImpl(stream);
    }

    @NotNull
    @Override
    Spliterator.OfLong spliterator();

    private LongStream stream() {
        final var spliterator = spliterator();
        final var parallel = this instanceof LongStreamXImpl && isParallel();
        return stream(spliterator, parallel);
    }

    private static LongStream stream(Spliterator.OfLong spliterator, boolean parallel) {
        if (spliterator.hasCharacteristics(Spliterator.IMMUTABLE) ||
                spliterator.hasCharacteristics(Spliterator.CONCURRENT)) {
            return StreamSupport.longStream(spliterator, parallel);
        }
        return StreamSupport.longStream(() -> spliterator, spliterator.characteristics(), parallel);
    }

    default LongSequence asSequence() {
        //noinspection FunctionalExpressionCanBeFolded
        return LongSequence.of(this::iterator);
    }

    @Override
    default LongStreamX filter(LongPredicate predicate) {
        return LongStreamX.of(stream().filter(predicate));
    }

    @Override
    default LongStreamX map(LongUnaryOperator mapper) {
        return LongStreamX.of(stream().map(mapper));
    }

    @Override
    default IntStreamX mapToInt(LongToIntFunction mapper) {
        return new IntStreamXImpl(stream().mapToInt(mapper));
    }

    @Override
    default DoubleStream mapToDouble(LongToDoubleFunction mapper) {
        return stream().mapToDouble(mapper);
    }

    @Override
    default <U> StreamX<U> mapToObj(LongFunction<? extends U> mapper) {
        return new StreamXImpl<>(stream().mapToObj(mapper));
    }

    @Override
    default LongStreamX flatMap(LongFunction<? extends LongStream> mapper) {
        return LongStreamX.of(stream().flatMap(mapper));
    }

    @Override
    default LongStreamX distinct() {
        return LongStreamX.of(stream().distinct());
    }

    @Override
    default LongStreamX sorted() {
        return LongStreamX.of(stream().sorted());
    }

    @Override
    @SuppressWarnings("squid:S3864")
    default LongStreamX peek(LongConsumer action) {
        return LongStreamX.of(stream().peek(action));
    }

    @Override
    default LongStreamX limit(long maxSize) {
        return LongStreamX.of(stream().limit(maxSize));
    }

    @Override
    default LongStreamX skip(long n) {
        return LongStreamX.of(stream().skip(n));
    }

    @Override
    default void forEach(LongConsumer action) {
        stream().forEach(action);
    }

    @Override
    default void forEachOrdered(LongConsumer action) {
        stream().forEachOrdered(action);
    }

    @Override
    default long @NotNull [] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    default long reduce(long identity, LongBinaryOperator accumulator) {
        return stream().reduce(identity, accumulator);
    }

    @NotNull
    @Override
    default OptionalLong reduce(LongBinaryOperator accumulator) {
        return stream().reduce(accumulator);
    }

    @Override
    default <R> R collect(Supplier<R> supplier,
                          ObjLongConsumer<R> accumulator,
                          BiConsumer<R, R> combiner) {
        return stream().collect(supplier, accumulator, combiner);
    }

    @NotNull
    default OptionalLong max() {
        return stream().max();
    }


    @Override
    default long count() {
        return stream().count();
    }

    @Override
    default long sum() {
        return reduce(0, Long::sum);
    }

    @Override
    default OptionalLong min() {
        return stream().min();
    }

    @Override
    default OptionalDouble average() {
        return stream().average();
    }

    @Override
    default LongSummaryStatistics summaryStatistics() {
        return stream().summaryStatistics();
    }

    default LongStatistics statistics() {
        return collect(LongStatistics::new, LongStatistics::accept, LongStatistics::combine);
    }

    @Override
    default DoubleStreamX asDoubleStream(){
        return DoubleStreamX.of(mapToDouble(It::asDouble));
    }

    @Override
    default StreamX<Long> boxed() {
        return new StreamXImpl<>(stream().boxed());
    }

    @Override
    default boolean anyMatch(LongPredicate predicate) {
        return stream().anyMatch(predicate);
    }

    @Override
    default boolean allMatch(LongPredicate predicate) {
        return stream().allMatch(predicate);
    }

    @Override
    default boolean noneMatch(LongPredicate predicate) {
        return stream().noneMatch(predicate);
    }

    @NotNull
    @Override
    default OptionalLong findFirst() {
        return stream().findFirst();
    }

    @Override
    default OptionalLong findAny() {
        return stream().findAny();
    }

    @NotNull
    @Override
    default PrimitiveIterator.OfLong iterator() {
        return Spliterators.iterator(spliterator());
    }

    @Override
    default boolean isParallel() {
        throw new UnsupportedOperationException("isParallel() not supported in LongStreamX interface");
    }

    @NotNull
    @Override
    default LongStreamX sequential() {
        return LongStreamX.of(stream(spliterator(), false));
    }

    @NotNull
    @Override
    default LongStreamX parallel() {
        return LongStreamX.of(stream(spliterator(), true));
    }

    @NotNull
    @Override
    default LongStreamX unordered() {
        return LongStreamX.of(stream().unordered());
    }

    @NotNull
    @Override
    default LongStreamX onClose(Runnable closeHandler) {
        return this;
    }

    @Override
    default void close() {
        throw new UnsupportedOperationException("Not supported in LongStreamX interface");
    }

    @Override
    default LongStreamX takeWhile(LongPredicate predicate) {
        return LongStreamX.of(LongStream.super.takeWhile(predicate));
    }

    @Override
    default LongStreamX dropWhile(LongPredicate predicate) {
        return LongStreamX.of(LongStream.super.dropWhile(predicate));
    }
}
