package org.hzt.utils.streams;

import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.SortedMutableSetX;
import org.hzt.utils.collectors.CollectorsX;
import org.hzt.utils.iterables.IterableXHelper;
import org.hzt.utils.iterables.Numerable;
import org.hzt.utils.iterables.Sortable;
import org.hzt.utils.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
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
public interface StreamX<T> extends Stream<T>, Sortable<T>, Numerable<T>, Spliterable<T> {

    static <T> StreamX<T> generate(Supplier<? extends T> operator) {
        return StreamX.of(Stream.generate(operator));
    }

    static <T> StreamX<T> generate(T initial, UnaryOperator<T> operator) {
        return StreamX.of(Stream.iterate(initial, operator));
    }

    @SafeVarargs
    static <T> StreamX<T> of(@NotNull T... values) {
        return new StreamXImpl<>(stream(Spliterators.spliterator(values, 0), false));
    }
    static <T> StreamX<T> of(@NotNull Iterable<T> iterable) {
        return new StreamXImpl<>(stream(iterable.spliterator(), false));
    }

    static <K, V> EntryStreamX<K, V> ofMap(Map<K, V> map) {
        return () -> map.entrySet().spliterator();
    }

    static <T> StreamX<T> parallel(@NotNull Iterable<T> iterable) {
        return new StreamXImpl<>(stream(iterable.spliterator(), true));
    }

    static <T> StreamX<T> of(Stream<T> stream) {
        return new StreamXImpl<>(stream);
    }

    @NotNull
    @Override
    Spliterator<T> spliterator();

    private Stream<T> stream() {
        final var spliterator = spliterator();
        final var parallel = this instanceof StreamXImpl && isParallel();
        return stream(spliterator, parallel);
    }

    private static <T> Stream<T> stream(final Spliterator<T> spliterator, final boolean parallel) {
        if (spliterator.hasCharacteristics(Spliterator.IMMUTABLE) ||
                spliterator.hasCharacteristics(Spliterator.CONCURRENT)) {
            return StreamSupport.stream(spliterator, parallel);
        }
        return StreamSupport.stream(() -> spliterator, spliterator.characteristics(), parallel);
    }

    default Sequence<T> asSequence() {
        //noinspection FunctionalExpressionCanBeFolded
        return Sequence.of(this::iterator);
    }

    @Override
    default StreamX<T> filter(Predicate<? super T> predicate) {
        return StreamX.of(stream().filter(predicate));
    }

    @Override
    default <R> StreamX<R> map(Function<? super T, ? extends R> mapper) {
        return StreamX.of(stream().map(mapper));
    }

    @Override
    default IntStreamX mapToInt(ToIntFunction<? super T> mapper) {
        return IntStreamX.of(stream().mapToInt(mapper));
    }

    @Override
    default LongStreamX mapToLong(ToLongFunction<? super T> mapper) {
        return LongStreamX.of(stream().mapToLong(mapper));
    }

    @Override
    default DoubleStreamX mapToDouble(ToDoubleFunction<? super T> mapper) {
        return DoubleStreamX.of(stream().mapToDouble(mapper));
    }

    @Override
    default <R> StreamX<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return StreamX.of(stream().flatMap(mapper));
    }

    default <R> StreamX<R> flatMapIterable(Function<? super T, ? extends Iterable<? extends R>> mapper) {
        return flatMap(e -> StreamSupport.stream(mapper.apply(e).spliterator(), isParallel()));
    }

    @Override
    default IntStreamX flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
        return IntStreamX.of(stream().flatMapToInt(mapper));
    }

    @Override
    default LongStreamX flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
        return LongStreamX.of(stream().flatMapToLong(mapper));
    }

    @Override
    default DoubleStreamX flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
        return DoubleStreamX.of(stream().flatMapToDouble(mapper));
    }

    @Override
    default StreamX<T> distinct() {
        return StreamX.of(stream().distinct());
    }

    @Override
    default StreamX<T> sorted() {
        return StreamX.of(stream().sorted());
    }

    @Override
    default StreamX<T> sorted(Comparator<? super T> comparator) {
        return StreamX.of(stream().sorted(comparator));
    }

    @Override
    default <R extends Comparable<? super R>> StreamX<T> sortedBy(@NotNull Function<? super T, ? extends R> selector) {
        return sorted(Comparator.comparing(selector));
    }

    @Override
    default StreamX<T> sortedDescending() {
        return sortedByDescending(IterableXHelper::asComparableOrThrow);
    }

    @Override
    default <R extends Comparable<? super R>> StreamX<T> sortedByDescending(@NotNull Function<? super T, ? extends R> selector) {
        return sorted(Comparator.comparing(selector).reversed());
    }

    @Override
    default <R extends Comparable<? super R>> SortedMutableSetX<T> toSortedSet(@NotNull Function<? super T, ? extends R> selector) {
        return collect(Collectors.toCollection(() -> SortedMutableSetX.comparingBy(selector)));
    }

    @Override
    default <R extends Comparable<? super R>> SortedMutableSetX<R> toSortedSetOf(@NotNull Function<? super T, ? extends R> selector) {
        return map(selector).collect(Collectors.toCollection(() -> SortedMutableSetX.comparingBy(It::self)));
    }

    @Override
    @SuppressWarnings("squid:S3864")
    default StreamX<T> peek(Consumer<? super T> action) {
        return StreamX.of(stream().peek(action));
    }

    @Override
    default StreamX<T> limit(long maxSize) {
        return StreamX.of(stream().limit(maxSize));
    }

    @Override
    default StreamX<T> skip(long n) {
        return StreamX.of(stream().skip(n));
    }

    @Override
    default void forEach(Consumer<? super T> action) {
        stream().forEach(action);
    }

    @Override
    default void forEachOrdered(Consumer<? super T> action) {
        stream().forEachOrdered(action);
    }

    @NotNull
    @Override
    default Object @NotNull [] toArray() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    default <A> A @NotNull [] toArray(IntFunction<A[]> generator) {
        //noinspection SuspiciousToArrayCall
        return stream().toArray(generator);
    }

    @Override
    default T reduce(T identity, BinaryOperator<T> accumulator) {
        return stream().reduce(identity, accumulator);
    }

    @NotNull
    @Override
    default Optional<T> reduce(BinaryOperator<T> accumulator) {
        return stream().reduce(accumulator);
    }

    @Override
    default <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        return stream().reduce(identity, accumulator, combiner);
    }

    @Override
    default <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        return stream().collect(supplier, accumulator, combiner);
    }

    @Override
    default <R, A> R collect(@NotNull Collector<? super T, A, R> collector) {
        return stream().collect(collector);
    }

    @Override
    @NotNull
    default <R extends Comparable<? super R>> Optional<T> minBy(@NotNull Function<? super T, ? extends R> selector) {
        return min(Comparator.comparing(selector));
    }

    @Override
    default <R extends Comparable<? super R>> @NotNull R minOf(@NotNull Function<? super T, ? extends R> selector) {
        return map(selector).minBy(It::self).orElseThrow();
    }

    @NotNull
    @Override
    default Optional<T> min(Comparator<? super T> comparator) {
        return stream().min(comparator);
    }

    @Override
    @NotNull
    default <R extends Comparable<? super R>> Optional<T> maxBy(@NotNull Function<? super T, ? extends R> selector) {
        return max(Comparator.comparing(selector));
    }

    @Override
    default <R extends Comparable<? super R>> @NotNull R maxOf(@NotNull Function<? super T, ? extends R> selector) {
        return map(selector).maxBy(It::self).orElseThrow();
    }

    @NotNull
    @Override
    default Optional<T> max(Comparator<? super T> comparator) {
        return stream().max(comparator);
    }


    @Override
    default long count() {
        return stream().count();
    }

    @Override
    default boolean anyMatch(Predicate<? super T> predicate) {
        return stream().anyMatch(predicate);
    }

    @Override
    default boolean allMatch(Predicate<? super T> predicate) {
        return stream().allMatch(predicate);
    }

    @Override
    default boolean noneMatch(Predicate<? super T> predicate) {
        return stream().noneMatch(predicate);
    }

    @NotNull
    @Override
    default Optional<T> findFirst() {
        return stream().findFirst();
    }

    @NotNull
    @Override
    default Optional<T> findAny() {
        return stream().findAny();
    }

    @NotNull
    @Override
    default Iterator<T> iterator() {
        return Spliterators.iterator(spliterator());
    }

    @Override
    default boolean isParallel() {
        throw new UnsupportedOperationException("isParallel() not supported in StreamX interface");
    }

    default StreamX<T> isParallel(Consumer<Boolean> resultSupplier) {
        return peek(s -> resultSupplier.accept(isParallel()));
    }

    @NotNull
    @Override
    default StreamX<T> sequential() {
        return StreamX.of(stream(spliterator(), false));
    }

    @NotNull
    @Override
    default StreamX<T> parallel() {
        return StreamX.of(stream(spliterator(), true));
    }

    @NotNull
    @Override
    default StreamX<T> unordered() {
        return StreamX.of(stream().unordered());
    }

    @NotNull
    @Override
    default StreamX<T> onClose(Runnable closeHandler) {
        return this;
    }

    @Override
    default void close() {
        throw new UnsupportedOperationException("Not supported in StreamX interface");
    }

    @Override
    default <R> StreamX<R> mapMulti(@NotNull BiConsumer<? super T, ? super Consumer<R>> mapper) {
        final var sequence = Sequence.of(stream()::iterator).mapMulti(mapper);
        final var parallel = isParallel();
        return new StreamXImpl<>(parallel ? sequence.parallelStream() : sequence.stream());
    }

    @Override
    default StreamX<T> takeWhile(Predicate<? super T> predicate) {
        return StreamX.of(Stream.super.takeWhile(predicate));
    }

    @Override
    default StreamX<T> dropWhile(Predicate<? super T> predicate) {
        return StreamX.of(Stream.super.dropWhile(predicate));
    }

    @Override
    default List<T> toList() {
        return stream().toList();
    }

    default ListX<T> toListX() {
        return collect(CollectorsX.toListX());
    }

    default <V> Map<T, V> associateWith(@NotNull Function<? super T, ? extends V> valueMapper) {
        return collect(Collectors.toUnmodifiableMap(It::self, valueMapper));
    }

    default <K> Map<K, T> associateBy(@NotNull Function<? super T, ? extends K> keyMapper) {
        return collect(Collectors.toUnmodifiableMap(keyMapper, It::self));
    }

    default <K> Map<K, List<T>> groupBy(@NotNull Function<? super T, ? extends K> selector) {
        return collect(Collectors.groupingBy(selector));
    }

    default Map<T, List<T>> group() {
        return groupBy(It::self);
    }
}
