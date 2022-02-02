package hzt.sequences;

import hzt.PreConditions;
import hzt.collections.ArrayIterator;
import hzt.collections.IndexedValue;
import hzt.collections.ListX;
import hzt.collections.MutableListX;
import hzt.collections.MutableSetX;
import hzt.collections.SetX;
import hzt.function.It;
import hzt.function.Transformable;
import hzt.iterables.IterableX;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * A sequence is a simplified stream. It evaluates its operations in a lazy way.
 *
 * It does not support parallel execution.
 *
 * The implementation is heavily inspired on Kotlin's sequences api. This api provides offers simpler syntax than streams
 * and is easier to understand
 *
 * @param <T> the type of the items in the Sequence
 */
public interface Sequence<T> extends IterableX<T>, Transformable<Sequence<T>> {

    static <T> Sequence<T> empty() {
        return new EmptySequence<>();
    }

    static <T> Sequence<T> of(@NotNull Iterable<T> iterable) {
        return new Sequence<T>() {
            @NotNull
            @Override
            public Iterator<T> iterator() {
                return iterable.iterator();
            }

            @Override
            public Sequence<T> get() {
                return this;
            }
        };
    }

    @SafeVarargs
    static <T> Sequence<T> of(T... values) {
        return () -> new ArrayIterator<>(values);
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

    static Sequence<Integer> range(int start, int end, IntPredicate predicate) {
        return generate(start, i -> i + 1).take(end - start).filter(predicate::test);
    }

    static Sequence<Integer> range(int start, int end) {
        return range(start, end, It.noIntFilter());
    }

    static Sequence<Integer> rangeClosed(int start, int endInclusive, IntPredicate predicate) {
        return generate(start, i -> i + 1).take(endInclusive + 1 - start).filter(predicate::test);
    }

    static Sequence<Integer> rangeClosed(int start, int endInclusive) {
        return rangeClosed(start, endInclusive, It.noIntFilter());
    }

    @Override
    default <R> Sequence<R> map(@NotNull Function<T, R> mapper) {
        return new TransformingSequence<>(this, mapper);
    }

    @Override
    default <R> Sequence<R> mapIndexed(BiFunction<Integer, ? super T, ? extends R> mapper) {
        return new TransformingIndexedSequence<>(this, mapper);
    }

    @Override
    default Sequence<T> filter(@NotNull Predicate<T> predicate) {
        return new FilteringSequence<>(this, predicate, true);
    }

    @Override
    default Sequence<T> filterNot(@NotNull Predicate<T> predicate) {
        return new FilteringSequence<>(this, predicate, false);
    }

    @Override
    default <R, I extends Iterable<R>> Sequence<R> flatMap(@NotNull Function<T, I> transform) {
        return new FlatteningSequence<>(this, transform, Iterable::iterator);
    }

    @Override
    default Sequence<IndexedValue<T>> withIndex() {
        return this::indexedIterator;
    }

    @NotNull
    @Override
    default Sequence<T> onEach(@NotNull Consumer<? super T> consumer) {
        return onEachOf(It::self, consumer);
    }

    @NotNull
    @Override
    default <R> Sequence<T> onEachOf(@NotNull Function<? super T, ? extends R> selector, @NotNull Consumer<? super R> consumer) {
        return map(item -> {
            consumer.accept(selector.apply(item));
            return item;
        });
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

    default MutableListX<T> toMutableList() {
        return toCollectionNotNullOf(MutableListX::empty, It::self);
    }

    default ListX<T> toListX() {
        return toMutableList();
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
