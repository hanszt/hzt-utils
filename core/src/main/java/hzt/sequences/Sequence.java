package hzt.sequences;

import hzt.PreConditions;
import hzt.collections.IndexedValue;
import hzt.collections.ListX;
import hzt.collections.MutableListX;
import hzt.collections.MutableSetX;
import hzt.collections.SetX;
import hzt.iterables.IterableX;
import hzt.iterators.ArrayIterator;
import hzt.utils.It;
import hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * A sequence is a simplified stream. It evaluates its operations in a lazy way.
 * <p>
 * It does not support parallel execution.
 * <p>
 * The implementation is heavily inspired on Kotlin's sequences api. This api provides offers simpler syntax than streams
 * and is easier to understand
 *
 * @param <T> the type of the items in the Sequence
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface Sequence<T> extends IterableX<T>, Transformable<Sequence<T>> {

    static <T> Sequence<T> empty() {
        return new EmptySequence<>();
    }

    static <T> Sequence<T> of(@NotNull Iterable<T> iterable) {
        return iterable::iterator;
    }

    @SafeVarargs
    static <T> Sequence<T> of(T... values) {
        return () -> ArrayIterator.of(values);
    }

    static <T> Sequence<T> ofNullable(T value) {
        return value == null ? new EmptySequence<>() : Sequence.of(value);
    }

    static <T> Sequence<T> generate(T seedValue, UnaryOperator<T> nextFunction) {
        return seedValue == null ? new EmptySequence<>() : new GeneratorSequence<>(() -> seedValue, nextFunction);
    }

    static <T> Sequence<T> generate(Supplier<T> nextFunction) {
        return new GeneratorSequence<>(nextFunction, t -> nextFunction.get());
    }

    static <T> Sequence<T> generate(Supplier<T> seedFunction, UnaryOperator<T> nextFunction) {
        return new GeneratorSequence<>(seedFunction, nextFunction);
    }

    static Sequence<Integer> range(int start, int end) {
        return generate(start, i -> i + 1).take(end - start);
    }

    static Sequence<Integer> rangeClosed(int start, int endInclusive) {
        return generate(start, i -> i + 1).take(endInclusive + 1 - start);
    }

    @Override
    default <R> Sequence<R> map(@NotNull Function<T, R> mapper) {
        return new TransformingSequence<>(this, mapper);
    }

    @Override
    default <R> Sequence<R> mapNotNull(Function<? super T, ? extends R> mapper) {
        return new FilteringSequence<>(
                new TransformingSequence<>(
                        new FilteringSequence<>(this,
                                Objects::nonNull), mapper), Objects::nonNull);
    }

    @Override
    default <R> Sequence<R> mapIndexed(BiFunction<Integer, ? super T, ? extends R> mapper) {
        return new TransformingIndexedSequence<>(this, mapper);
    }

    @Override
    default Sequence<T> filter(@NotNull Predicate<T> predicate) {
        return new FilteringSequence<>(this, predicate);
    }

    @Override
    default Sequence<T> filterIndexed(@NotNull BiPredicate<Integer, T> predicate) {
        return new TransformingSequence<>(
                new FilteringSequence<>(withIndex(),
                        val -> predicate.test(val.index(), val.value())), IndexedValue::value);
    }

    @Override
    default Sequence<T> filterNot(@NotNull Predicate<T> predicate) {
        return new FilteringSequence<>(this, predicate, false);
    }

    @Override
    default <R, I extends Iterable<R>> Sequence<R> flatMap(@NotNull Function<T, I> transform) {
        return new FlatteningSequence<>(this, transform, Iterable::iterator);
    }

    default <R> Sequence<R> flatMapStream(@NotNull Function<T, Stream<R>> transform) {
        return new FlatteningSequence<>(this, transform, Stream::iterator);
    }

    @Override
    default Sequence<IndexedValue<T>> withIndex() {
        return this::indexedIterator;
    }

    @NotNull
    @Override
    default Sequence<T> onEach(@NotNull Consumer<? super T> consumer) {
        return onEach(It::self, consumer);
    }

    @NotNull
    @Override
    default <R> Sequence<T> onEach(@NotNull Function<? super T, ? extends R> selector, @NotNull Consumer<? super R> consumer) {
        return map(item -> {
            consumer.accept(selector.apply(item));
            return item;
        });
    }

    @NotNull
    @Override
    default Sequence<T> distinct() {
        return distinctBy(It::self);
    }

    @NotNull
    @Override
    default <R> Sequence<T> distinctBy(Function<T, R> selector) {
        return new DistinctSequence<>(this, selector);
    }

    default Sequence<ListX<T>> windowed(int size) {
        return windowed(size, 1);
    }

    default <R> Sequence<R> windowed(int size, Function<ListX<T>, R> transform) {
        return windowed(size, 1).map(transform);
    }

    default Sequence<ListX<T>> windowed(int size, int step) {
        return windowed(size, step, false);
    }

    default <R> Sequence<R> windowed(int size, int step, Function<ListX<T>, R> transform) {
        return windowed(size, step, false).map(transform);
    }

    default Sequence<ListX<T>> windowed(int size, boolean partialWindows) {
        return windowed(size, 1, partialWindows);
    }

    default Sequence<ListX<T>> windowed(int size, int step, boolean partialWindows) {
        return new WindowedSequence<>(this, size, step, partialWindows);
    }

    default <R> Sequence<R> windowed(int size, int step, boolean partialWindows, Function<ListX<T>, R> transform) {
        return new WindowedSequence<>(this, size, step, partialWindows).map(transform);
    }

    @Override
    default <R> Sequence<R> zipWithNext(BiFunction<T, T, R> function) {
        return Sequence.of(zipWithNextToMutableListOf(function));
    }

    @Override
    default <A, R> Sequence<R> zipWith(Iterable<A> iterable, BiFunction<T, A, R> function) {
        return Sequence.of(zipToMutableListWith(iterable, function));
    }

    @Override
    default Sequence<T> take(int n) {
        PreConditions.requireGreaterThanZero(n);
        if (n == 0) {
            return new EmptySequence<>();
        } else if (this instanceof SkipTakeSequence) {
            SkipTakeSequence<T> skipTakeSequence = (SkipTakeSequence<T>) this;
            return skipTakeSequence.take(n);
        } else {
            return new TakeSequence<>(this, n);
        }
    }

    @Override
    default Sequence<T> takeWhile(Predicate<T> predicate) {
        return new TakeWhileSequence<>(this, predicate, false);
    }

    @Override
    default Sequence<T> takeWhileInclusive(Predicate<T> predicate) {
        return new TakeWhileSequence<>(this, predicate, true);
    }

    @Override
    default Sequence<T> skipWhile(Predicate<T> predicate) {
        return new SkipWhileSequence<>(this, predicate);
    }

    @Override
    default Sequence<T> skip(int n) {
        PreConditions.requireGreaterThanZero(n);
        if (n == 0) {
            return this;
        } else if (this instanceof SkipTakeSequence) {
            SkipTakeSequence<T> skipTakeSequence = (SkipTakeSequence<T>) this;
            return skipTakeSequence.skip(n);
        } else {
            return new SkipSequence<>(this, n);
        }
    }

    @Override
    default <R extends Comparable<R>> Sequence<T> sortedBy(@NotNull Function<T, R> selector) {
        return Sequence.of(toMutableListSortedBy(selector));
    }

    @Override
    default Sequence<T> sorted() {
        return Sequence.of(IterableX.super.sorted());
    }

    default <C extends Collection<T>> C toCollection(Supplier<C> collectionFactory) {
        return filterToCollection(collectionFactory, It.noFilter());
    }

    default MutableListX<T> toMutableList() {
        return toMutableListOf(It::self);
    }

    default ListX<T> toListX() {
        return toMutableList();
    }

    default List<T> toList() {
        return List.copyOf(toMutableList());
    }

    default MutableSetX<T> toMutableSet() {
        return toCollectionNotNullOf(MutableSetX::empty, It::self);
    }

    default SetX<T> toSetX() {
        return toMutableSet();
    }

    default int count() {
        return count(It.noFilter());
    }

    default Sequence<T> get() {
        return this;
    }
}
