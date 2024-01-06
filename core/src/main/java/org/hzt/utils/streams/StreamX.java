package org.hzt.utils.streams;

import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.SortedMutableSetX;
import org.hzt.utils.collectors.CollectorsX;
import org.hzt.utils.gatherers.Gatherer;
import org.hzt.utils.gatherers.Gatherers;
import org.hzt.utils.iterables.Gatherable;
import org.hzt.utils.iterables.IterableXHelper;
import org.hzt.utils.iterables.Numerable;
import org.hzt.utils.iterables.Sortable;
import org.hzt.utils.iterators.Iterators;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.SequenceHelper;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@FunctionalInterface
@SuppressWarnings("squid:S1448")
public interface StreamX<T> extends Stream<T>, Gatherable<T>, Sortable<T>, Numerable<T>, Spliterable<T> {

    static <T> StreamX<T> generate(final Supplier<T> operator) {
        return StreamX.of(Stream.generate(operator));
    }

    static <T> StreamX<T> iterate(final T initial, final UnaryOperator<T> operator) {
        return StreamX.of(Stream.iterate(initial, operator));
    }

    @SafeVarargs
    static <T> StreamX<T> of(final T... values) {
        return new StreamXImpl<>(StreamXHelper.stream(Spliterators.spliterator(values, 0), false));
    }

    static <T> StreamX<T> of(final Iterable<T> iterable) {
        return new StreamXImpl<>(StreamXHelper.stream(iterable.spliterator(), false));
    }

    static <K, V> EntryStreamX<K, V> ofMap(final Map<K, V> map) {
        return () -> map.entrySet().spliterator();
    }

    static <T> StreamX<T> parallel(final Iterable<T> iterable) {
        return new StreamXImpl<>(StreamXHelper.stream(iterable.spliterator(), true));
    }

    static <T> StreamX<T> of(final Stream<T> stream) {
        return new StreamXImpl<>(stream);
    }

    @Override
    Spliterator<T> spliterator();

    default Sequence<T> asSequence() {
        return Sequence.of(this);
    }

    default Stream<T> stream() {
        return StreamXHelper.stream(spliterator(), isParallel());
    }

    @Override
    default StreamX<T> filter(final Predicate<? super T> predicate) {
        return StreamX.of(StreamXHelper.stream(this).filter(predicate));
    }

    @Override
    default <R> StreamX<R> map(final Function<? super T, ? extends R> mapper) {
        return StreamX.of(StreamXHelper.stream(this).map(mapper));
    }

    @Override
    default IntStreamX mapToInt(final ToIntFunction<? super T> mapper) {
        return IntStreamX.of(StreamXHelper.stream(this).mapToInt(mapper));
    }

    @Override
    default LongStreamX mapToLong(final ToLongFunction<? super T> mapper) {
        return LongStreamX.of(StreamXHelper.stream(this).mapToLong(mapper));
    }

    @Override
    default DoubleStreamX mapToDouble(final ToDoubleFunction<? super T> mapper) {
        return DoubleStreamX.of(StreamXHelper.stream(this).mapToDouble(mapper));
    }

    default <R> StreamX<R> then(final StreamExtension<T, R> extension) {
        return new StreamXImpl<>(extension.extend(this));
    }

    default <R> R finish(final Function<Stream<T>, ? extends R> finisher) {
        return finisher.apply(this);
    }

    @Override
    default <R> StreamX<R> flatMap(final Function<? super T, ? extends Stream<? extends R>> mapper) {
        return StreamX.of(StreamXHelper.stream(this).flatMap(mapper));
    }

    default <R> StreamX<R> flatMapIterable(final Function<? super T, ? extends Iterable<? extends R>> mapper) {
        return flatMap(e -> StreamSupport.stream(mapper.apply(e).spliterator(), isParallel()));
    }

    @Override
    default IntStreamX flatMapToInt(final Function<? super T, ? extends IntStream> mapper) {
        return IntStreamX.of(StreamXHelper.stream(this).flatMapToInt(mapper));
    }

    @Override
    default LongStreamX flatMapToLong(final Function<? super T, ? extends LongStream> mapper) {
        return LongStreamX.of(StreamXHelper.stream(this).flatMapToLong(mapper));
    }

    @Override
    default DoubleStreamX flatMapToDouble(final Function<? super T, ? extends DoubleStream> mapper) {
        return DoubleStreamX.of(StreamXHelper.stream(this).flatMapToDouble(mapper));
    }

    @Override
    default StreamX<T> distinct() {
        return StreamX.of(StreamXHelper.stream(this).distinct());
    }

    @Override
    default StreamX<T> sorted() {
        return StreamX.of(StreamXHelper.stream(this).sorted());
    }

    @Override
    default StreamX<T> sorted(final Comparator<? super T> comparator) {
        return StreamX.of(StreamXHelper.stream(this).sorted(comparator));
    }

    @Override
    default <R extends Comparable<? super R>> StreamX<T> sortedBy(final Function<? super T, ? extends R> selector) {
        return sorted(Comparator.comparing(selector));
    }

    @Override
    default StreamX<T> sortedDescending() {
        return sortedByDescending(IterableXHelper::asComparableOrThrow);
    }

    @Override
    default <R extends Comparable<? super R>> StreamX<T> sortedByDescending(final Function<? super T, ? extends R> selector) {
        return sorted(Comparator.comparing(selector).reversed());
    }

    @Override
    default <R extends Comparable<? super R>> SortedMutableSetX<T> toSortedSet(final Function<? super T, ? extends R> selector) {
        return collect(Collectors.toCollection(() -> SortedMutableSetX.comparingBy(selector)));
    }

    @Override
    default <R extends Comparable<? super R>> SortedMutableSetX<R> toSortedSetOf(final Function<? super T, ? extends R> selector) {
        return map(selector).collect(Collectors.toCollection(() -> SortedMutableSetX.comparingBy(It::self)));
    }

    @Override
    @SuppressWarnings("squid:S3864")
    default StreamX<T> peek(final Consumer<? super T> action) {
        return StreamX.of(StreamXHelper.stream(this).peek(action));
    }

    @Override
    default StreamX<T> limit(final long maxSize) {
        return StreamX.of(StreamXHelper.stream(this).limit(maxSize));
    }

    @Override
    default StreamX<T> skip(final long n) {
        return StreamX.of(StreamXHelper.stream(this).skip(n));
    }

    default StreamX<ListX<T>> windowed(final int initSize,
                                       final IntUnaryOperator nextSizeSupplier,
                                       final int initStep,
                                       final IntUnaryOperator nextStepSupplier,
                                       final boolean partialWindows) {
        SequenceHelper.checkInitWindowSizeAndStep(initSize, initStep);
        return new StreamXImpl<>(StreamXHelper.stream(Spliterators.spliteratorUnknownSize(Iterators.windowedIterator(iterator(),
                initSize, nextSizeSupplier, initStep, nextStepSupplier, partialWindows), Spliterator.ORDERED), isParallel()));
    }

    @Override
    default void forEach(final Consumer<? super T> action) {
        StreamXHelper.stream(this).forEach(action);
    }

    @Override
    default void forEachOrdered(final Consumer<? super T> action) {
        StreamXHelper.stream(this).forEachOrdered(action);
    }

    @Override
    default Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    default <A> A[] toArray(final IntFunction<A[]> generator) {
        return StreamXHelper.stream(this).toArray(generator);
    }

    @Override
    default T reduce(final T identity, final BinaryOperator<T> accumulator) {
        return StreamXHelper.stream(this).reduce(identity, accumulator);
    }

    @Override
    default Optional<T> reduce(final BinaryOperator<T> accumulator) {
        return StreamXHelper.stream(this).reduce(accumulator);
    }

    @Override
    default <U> U reduce(final U identity, final BiFunction<U, ? super T, U> accumulator, final BinaryOperator<U> combiner) {
        return StreamXHelper.stream(this).reduce(identity, accumulator, combiner);
    }

    @Override
    default <R> R collect(final Supplier<R> supplier, final BiConsumer<R, ? super T> accumulator, final BiConsumer<R, R> combiner) {
        return StreamXHelper.stream(this).collect(supplier, accumulator, combiner);
    }

    @Override
    default <R, A> R collect(final Collector<? super T, A, R> collector) {
        return StreamXHelper.stream(this).collect(collector);
    }

    @Override
    default <R extends Comparable<? super R>> Optional<T> minBy(final Function<? super T, ? extends R> selector) {
        return min(Comparator.comparing(selector));
    }

    @Override
    default <R extends Comparable<? super R>> R minOf(final Function<? super T, ? extends R> selector) {
        return map(selector).minBy(It::self).orElseThrow(NoSuchElementException::new);
    }

    @Override
    default Optional<T> min(final Comparator<? super T> comparator) {
        return StreamXHelper.stream(this).min(comparator);
    }

    @Override
    default <R extends Comparable<? super R>> Optional<T> maxBy(final Function<? super T, ? extends R> selector) {
        return max(Comparator.comparing(selector));
    }

    @Override
    default <R extends Comparable<? super R>> R maxOf(final Function<? super T, ? extends R> selector) {
        return map(selector).maxBy(It::self).orElseThrow(NoSuchElementException::new);
    }

    @Override
    default Optional<T> max(final Comparator<? super T> comparator) {
        return StreamXHelper.stream(this).max(comparator);
    }


    @Override
    default long count() {
        return StreamXHelper.stream(this).count();
    }

    @Override
    default boolean anyMatch(final Predicate<? super T> predicate) {
        return StreamXHelper.stream(this).anyMatch(predicate);
    }

    @Override
    default boolean allMatch(final Predicate<? super T> predicate) {
        return StreamXHelper.stream(this).allMatch(predicate);
    }

    @Override
    default boolean noneMatch(final Predicate<? super T> predicate) {
        return StreamXHelper.stream(this).noneMatch(predicate);
    }

    @Override
    default Optional<T> findFirst() {
        return StreamXHelper.stream(this).findFirst();
    }

    @Override
    default Optional<T> findAny() {
        return StreamXHelper.stream(this).findAny();
    }

    @Override
    default Iterator<T> iterator() {
        return Spliterators.iterator(spliterator());
    }

    @Override
    default boolean isParallel() {
        throw new UnsupportedOperationException("isParallel() not supported in StreamX interface");
    }

    default StreamX<T> isParallel(final Consumer<Boolean> resultSupplier) {
        return peek(s -> resultSupplier.accept(isParallel()));
    }

    @Override
    default StreamX<T> sequential() {
        return StreamX.of(StreamXHelper.stream(spliterator(), false));
    }

    @Override
    default StreamX<T> parallel() {
        return StreamX.of(StreamXHelper.stream(spliterator(), true));
    }

    @Override
    default StreamX<T> unordered() {
        return StreamX.of(StreamXHelper.stream(this).unordered());
    }

    @Override
    default StreamX<T> onClose(final Runnable closeHandler) {
        return this;
    }

    @Override
    default void close() {
        throw new UnsupportedOperationException("Not supported in StreamX interface");
    }

    default <R> StreamX<R> mapMulti(final BiConsumer<? super T, ? super Consumer<R>> mapper) {
        final Sequence<R> sequence = Sequence.of(this).mapMulti(mapper);
        return new StreamXImpl<>(isParallel() ? sequence.parallelStream() : sequence.stream());
    }

    /**
     * Returns a stream consisting of the results of applying the given
     * {@link Gatherer} to the elements of this stream.
     *
     * <p>This is an <a href="package-summary.html#Extensibility">extension point</a>
     * for <a href="package-summary.html#StreamOps">intermediate operations</a>.
     *
     * <p>Gatherers are highly flexible and can describe a vast array of
     * possibly stateful operations, with support for short-circuiting, and
     * parallelization.
     *
     * <p>When executed in parallel, multiple intermediate results may be
     * instantiated, populated, and merged so as to maintain isolation of
     * mutable data structures.  Therefore, even when executed in parallel
     * with non-thread-safe data structures (such as {@code ArrayList}), no
     * additional synchronization is needed for a parallel reduction.
     *
     * <p>Implementations are allowed, but not required, to detect consecutive
     * invocations and compose them into a single, fused, operation. This would
     * make the first expression below behave like the second:
     *
     * <pre>{@code
     *     var stream1 = Stream.of(...).gather(gatherer1).gather(gatherer2);
     *     var stream2 = Stream.of(...).gather(gatherer1.andThen(gatherer2));
     * }</pre>
     *
     * @param <R>      The element type of the new stream
     * @param gatherer a gatherer
     * @return the new stream
     * @implSpec The implementation in this interface returns a Stream produced as if by the following:
     * <pre>{@code
     * StreamSupport.stream(spliterator(), isParallel()).gather(gatherer)
     * }</pre>
     * <p>
     * * @implSpec
     * * The default implementation obtains the {@link #spliterator() spliterator}
     * * of this stream, wraps that spliterator to support the semantics
     * * of this operation on traversal, and returns a new stream associated with
     * * the wrapped spliterator.  The returned stream preserves the execution
     * * characteristics of this stream (namely parallel or sequential execution
     * * as per {@link #isParallel()}) but the wrapped spliterator may choose to
     * * not support splitting.  When the returned stream is closed, the close
     * * handlers for both the returned and this stream are invoked.
     * @implNote Implementations of this interface should provide their own
     * implementation of this method.
     * @see Gatherer
     * @see Gatherers
     */
    @Override
    default <A, R> StreamX<R> gather(final Gatherer<? super T, A, R> gatherer) {
        final Sequence<R> sequence = Sequence.of(this).gather(gatherer);
        return new StreamXImpl<>(isParallel() ? sequence.parallelStream() : sequence.stream());
    }

    default StreamX<T> takeWhile(final Predicate<? super T> predicate) {
        return StreamX.of(Sequence.of(this).takeWhile(predicate));
    }

    default StreamX<T> dropWhile(final Predicate<? super T> predicate) {
        return StreamX.of(Sequence.of(this).skipWhile(predicate));
    }

    default List<T> toList() {
        return collect(CollectorsX.toUnmodifiableList());
    }

    default ListX<T> toListX() {
        return collect(CollectorsX.toListX());
    }

    default <V> Map<T, V> associateWith(final Function<? super T, ? extends V> valueMapper) {
        return collect(CollectorsX.toUnmodifiableMap(It::self, valueMapper));
    }

    default <K> Map<K, T> associateBy(final Function<? super T, ? extends K> keyMapper) {
        return collect(CollectorsX.toUnmodifiableMap(keyMapper, It::self));
    }

    default <K> Map<K, List<T>> groupBy(final Function<? super T, ? extends K> selector) {
        return collect(Collectors.groupingBy(selector));
    }

    default Map<T, List<T>> group() {
        return groupBy(It::self);
    }
}
