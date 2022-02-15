package hzt.iterables;

import hzt.collections.ListX;
import hzt.collections.MutableListX;
import hzt.collections.MutableSetX;
import hzt.collections.SetX;
import hzt.ranges.DoubleRange;
import hzt.ranges.IntRange;
import hzt.ranges.LongRange;
import hzt.sequences.EntrySequence;
import hzt.sequences.Sequence;
import hzt.tuples.Pair;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This is an interface to transform smaller iterables including collections or arrays to some other state.
 * <p>
 * Its use is very comparable to the streams api but with shorter syntax.
 * <p>
 * It is inspired by the functional methods provided for collections in Kotlin
 * <p>
 * The functions applied to a Transformer are eagerly evaluated as apposed to in a stream.
 * For smaller collections < 100_000 elements, the performance is similar to streams
 * <p>
 * For larger collections, or when a lot of transformation are applied, streams are preferred.
 * <p>
 * For a Transformer, I suggest not to apply more than 3 subsequent transformations.
 *
 * @param <T> The Type of the Iterable in the Transform object
 * @author Hans Zuidervaart
 */
public interface IterableX<T> extends Mappable<T>, Filterable<T>,
        Sortable<T>, Distinctable<T>, Stringable<T>, Numerable<T>, Reducable<T>, Collectable<T>, Groupable<T> {

    IterableX<T> plus(@NotNull T value);

    IterableX<T> plus(@NotNull Iterable<T> values);

    default <C extends Collection<T>> C to(Supplier<C> collectionFactory) {
        return IterableXHelper.mapFilteringTo(this, collectionFactory, It::noFilter, It::self, It::noFilter);
    }

    <R> IterableX<R> castIfInstanceOf(@NotNull Class<R> aClass);

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Sequence<T> asSequence() {
        return Sequence.of(this);
    }

    default IntRange asIntRange(@NotNull ToIntFunction<T> keyMapper) {
        return IntRange.of(asSequence().map(keyMapper::applyAsInt));
    }

    default LongRange asLongRange(@NotNull ToLongFunction<T> keyMapper) {
        return LongRange.of(asSequence().map(keyMapper::applyAsLong));
    }

    default DoubleRange asDoubleRange(@NotNull ToDoubleFunction<T> keyMapper) {
        return DoubleRange.of(asSequence().map(keyMapper::applyAsDouble));
    }

    <K> EntryIterable<K, T> associateBy(@NotNull Function<? super T, ? extends K> keyMapper);

    <V> EntryIterable<T, V> associateWith(@NotNull Function<? super T, ? extends V> valueMapper);

    default <R> void forEach(@NotNull Function<? super T, ? extends R> selector,
                             @NotNull Consumer<? super R> consumer) {
        onEach(selector, consumer);
    }

    @NotNull
    default IterableX<T> onEach(@NotNull Consumer<? super T> consumer) {
        return onEach(It::self, consumer);
    }

    @NotNull
    default <R> IterableX<T> onEach(@NotNull Function<? super T, ? extends R> selector,
                                @NotNull Consumer<? super R> consumer) {
        for (T t : this) {
            consumer.accept(t != null ? selector.apply(t) : null);
        }
        return this;
    }

    default SetX<T> intersect(@NotNull Iterable<T> other) {
        final var intersection = toMutableSet();
        final var otherCollection = other instanceof Collectable<?> ? (Collection<T>) other : MutableListX.of(other);
        intersection.retainAll(otherCollection);
        return intersection;
    }

    default <S, I extends Iterable<S>, R> SetX<R> intersectionOf(@NotNull Function<? super T, ? extends I> toIterableMapper,
                                                                   @NotNull Function<? super S, ? extends R> selector) {
        return IterableReductions.intersectionOf(this, toIterableMapper, selector);
    }

    default <R, I extends Iterable<R>> SetX<R> intersectionOf(@NotNull Function<? super T, ? extends I> toIterableMapper) {
        return intersectionOf(toIterableMapper, It::self);
    }

    default <R> EntrySequence<T, R> zip(@NotNull Iterable<R> iterable) {
        return EntrySequence.ofPairs(zip(iterable, Pair::of));
    }

    <A, R> IterableX<R> zip(@NotNull Iterable<A> iterable, @NotNull BiFunction<? super T, ? super A, ? extends R> function);

    default EntrySequence<T, T> zipWithNext() {
        return EntrySequence.ofPairs(zipWithNext(Pair::of));
    }

    <R> IterableX<R> zipWithNext(BiFunction<T, T, R> function);

    IterableX<ListX<T>> chunked(int size);

    <R>IterableX<R> chunked(int size, @NotNull Function<? super ListX<T>, ? extends R> transform);

    IterableX<ListX<T>> windowed(int size);

    <R> IterableX<R> windowed(int size, @NotNull Function<? super ListX<T>, ? extends R> transform);

    IterableX<ListX<T>> windowed(int size, int step);

    <R> IterableX<R> windowed(int size, int step, @NotNull Function<? super ListX<T>, ? extends R> transform);

    IterableX<ListX<T>> windowed(int size, boolean partialWindows);

    IterableX<ListX<T>> windowed(int size, int step, boolean partialWindows);

    <R> IterableX<R> windowed(int size, int step, boolean partialWindows,
                              @NotNull Function<? super ListX<T>, R> transform);

    IterableX<T> skip(long count);

    IterableX<T> skipWhile(@NotNull Predicate<T> predicate);

    IterableX<T> skipWhileInclusive(@NotNull Predicate<T> predicate);

    IterableX<T> take(long n);

    IterableX<T> takeWhile(@NotNull Predicate<T> predicate);

    IterableX<T> takeWhileInclusive(@NotNull Predicate<T> predicate);

    default SetX<T> union(@NotNull Iterable<T> other) {
        MutableSetX<T> union = MutableSetX.empty();
        forEach(union::add);
        other.forEach(union::add);
        return union;
    }

    default <R> SetX<R> union(@NotNull Iterable<T> other, @NotNull Function<? super T, ? extends R> mapper) {
        MutableSetX<R> union = mapTo(MutableSetX::empty, mapper);
        final SetX<R> setX = ListX.of(other).toSetXOf(mapper);
        setX.forEach(union::add);
        return union;
    }
}
