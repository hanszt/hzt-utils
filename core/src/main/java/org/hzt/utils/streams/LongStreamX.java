package org.hzt.utils.streams;

import org.hzt.utils.It;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.hzt.utils.statistics.LongStatistics;

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

import static org.hzt.utils.streams.StreamXHelper.stream;

@FunctionalInterface
@SuppressWarnings("squid:S1448")
public interface LongStreamX extends LongStream, Spliterable.OfLong {

    static LongStreamX generate(final LongSupplier operator) {
        return of(LongStream.generate(operator));
    }

    static LongStreamX generate(final long initial, final LongUnaryOperator operator) {
        return of(LongStream.iterate(initial, operator));
    }

    static LongStreamX of(final long... values) {
        return new LongStreamXImpl(stream(Spliterators.spliterator(values, 0), false));
    }

    static LongStreamX of(final PrimitiveIterable.OfLong iterable) {
        return new LongStreamXImpl(stream(iterable.spliterator(), false));
    }

    static LongStreamX parallel(final PrimitiveIterable.OfLong iterable) {
        return new LongStreamXImpl(stream(iterable.spliterator(), true));
    }

    static LongStreamX of(final LongStream stream) {
        return new LongStreamXImpl(stream);
    }

    @Override
    Spliterator.OfLong spliterator();

    default LongSequence asSequence() {
        //noinspection FunctionalExpressionCanBeFolded
        return LongSequence.of(this::iterator);
    }

    @Override
    default LongStreamX filter(final LongPredicate predicate) {
        return LongStreamX.of(stream(this).filter(predicate));
    }

    @Override
    default LongStreamX map(final LongUnaryOperator mapper) {
        return LongStreamX.of(stream(this).map(mapper));
    }

    @Override
    default IntStreamX mapToInt(final LongToIntFunction mapper) {
        return new IntStreamXImpl(stream(this).mapToInt(mapper));
    }

    @Override
    default DoubleStream mapToDouble(final LongToDoubleFunction mapper) {
        return stream(this).mapToDouble(mapper);
    }

    @Override
    default <U> StreamX<U> mapToObj(final LongFunction<? extends U> mapper) {
        return new StreamXImpl<>(stream(this).mapToObj(mapper));
    }

    @Override
    default LongStreamX flatMap(final LongFunction<? extends LongStream> mapper) {
        return LongStreamX.of(stream(this).flatMap(mapper));
    }

    @Override
    default LongStreamX distinct() {
        return LongStreamX.of(stream(this).distinct());
    }

    @Override
    default LongStreamX sorted() {
        return LongStreamX.of(stream(this).sorted());
    }

    @Override
    @SuppressWarnings("squid:S3864")
    default LongStreamX peek(final LongConsumer action) {
        return LongStreamX.of(stream(this).peek(action));
    }

    @Override
    default LongStreamX limit(final long maxSize) {
        return LongStreamX.of(stream(this).limit(maxSize));
    }

    @Override
    default LongStreamX skip(final long n) {
        return LongStreamX.of(stream(this).skip(n));
    }

    @Override
    default void forEach(final LongConsumer action) {
        stream(this).forEach(action);
    }

    @Override
    default void forEachOrdered(final LongConsumer action) {
        stream(this).forEachOrdered(action);
    }

    @Override
    default long[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    default long reduce(final long identity, final LongBinaryOperator accumulator) {
        return stream(this).reduce(identity, accumulator);
    }

    @Override
    default OptionalLong reduce(final LongBinaryOperator accumulator) {
        return stream(this).reduce(accumulator);
    }

    @Override
    default <R> R collect(final Supplier<R> supplier,
                          final ObjLongConsumer<R> accumulator,
                          final BiConsumer<R, R> combiner) {
        return stream(this).collect(supplier, accumulator, combiner);
    }

    default OptionalLong max() {
        return stream(this).max();
    }

    @Override
    default long count() {
        return stream(this).count();
    }

    @Override
    default long sum() {
        return reduce(0, Long::sum);
    }

    @Override
    default OptionalLong min() {
        return stream(this).min();
    }

    @Override
    default OptionalDouble average() {
        return stream(this).average();
    }

    @Override
    default LongSummaryStatistics summaryStatistics() {
        return stream(this).summaryStatistics();
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
        return new StreamXImpl<>(stream(this).boxed());
    }

    @Override
    default boolean anyMatch(final LongPredicate predicate) {
        return stream(this).anyMatch(predicate);
    }

    @Override
    default boolean allMatch(final LongPredicate predicate) {
        return stream(this).allMatch(predicate);
    }

    @Override
    default boolean noneMatch(final LongPredicate predicate) {
        return stream(this).noneMatch(predicate);
    }

    @Override
    default OptionalLong findFirst() {
        return stream(this).findFirst();
    }

    @Override
    default OptionalLong findAny() {
        return stream(this).findAny();
    }

    @Override
    default PrimitiveIterator.OfLong iterator() {
        return Spliterators.iterator(spliterator());
    }

    @Override
    default boolean isParallel() {
        throw new UnsupportedOperationException("isParallel() not supported in LongStreamX interface");
    }

    @Override
    default LongStreamX sequential() {
        return LongStreamX.of(stream(spliterator(), false));
    }

    @Override
    default LongStreamX parallel() {
        return LongStreamX.of(stream(spliterator(), true));
    }

    @Override
    default LongStreamX unordered() {
        return LongStreamX.of(stream(this).unordered());
    }

    @Override
    default LongStreamX onClose(final Runnable closeHandler) {
        return this;
    }

    @Override
    default void close() {
        throw new UnsupportedOperationException("Not supported in LongStreamX interface");
    }
}
