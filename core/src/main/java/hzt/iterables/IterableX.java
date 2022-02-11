package hzt.iterables;

import hzt.collections.ListX;
import hzt.collections.MapX;
import hzt.collections.MutableListX;
import hzt.collections.MutableSetX;
import hzt.collections.SetX;
import hzt.ranges.DoubleRange;
import hzt.ranges.IntRange;
import hzt.ranges.LongRange;
import hzt.sequences.Sequence;
import hzt.tuples.Pair;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
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
        Sortable<T>, Distinctable<T>, Stringable<T>, Numerable<T>, Reducable<T>, Collectable<T> {

    IterableX<T> plus(@NotNull T... values);

    IterableX<T> plus(@NotNull Iterable<T> values);

    @Override
     <R> IterableX<R> map(@NotNull Function<? super T, ? extends R> mapper);

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

    default MapX<T, MutableListX<T>> group() {
        return groupBy(It::self);
    }

    default <K> MapX<K, MutableListX<T>> groupBy(@NotNull Function<? super T, ? extends K> classifier) {
        return groupMapping(classifier, It::self);
    }

    default <K, R> MapX<K, MutableListX<R>> groupMapping(@NotNull Function<? super T, ? extends K> classifier,
                                                         @NotNull Function<? super T, ? extends R> valueMapper) {
        return IterableReductions.groupMapping(this, classifier, valueMapper);
    }

    default Pair<ListX<T>, ListX<T>> partition(@NotNull Predicate<T> predicate) {
        return partitionMapping(predicate, It::self);
    }

    default <R> Pair<ListX<R>, ListX<R>> partitionMapping(@NotNull Predicate<T> predicate,
                                                          @NotNull Function<? super T, ? extends R> resultMapper) {
        return IterableReductions.partitionMapping(this, predicate, resultMapper);
    }

    default <S, C extends Collection<S>, R> SetX<R> intersectionOf(@NotNull Function<? super T, ? extends C> toCollectionMapper,
                                                                   @NotNull Function<? super S, ? extends R> selector) {
        return IterableReductions.intersectionOf(this, toCollectionMapper, selector);
    }

    default <R, C extends Collection<R>> SetX<R> intersectionOf(@NotNull Function<? super T, ? extends C> toCollectionMapper) {
        return intersectionOf(toCollectionMapper, It::self);
    }

    <A, R> IterableX<R> zipWith(@NotNull Iterable<A> iterable, @NotNull BiFunction<? super T, ? super A, ? extends R> function);

    <R> IterableX<R> zipWithNext(BiFunction<T, T, R> function);

    default <A, R> List<R> zipToListWith(@NotNull Iterable<A> otherIterable,
                                          @NotNull BiFunction<? super T, ? super A, ? extends R> function) {
        final Iterator<A> otherIterator = otherIterable.iterator();
        final Iterator<T> iterator = iterator();
        final int resultListSize = Math.min(IterableXHelper.collectionSizeOrElse(this, 10),
                IterableXHelper.collectionSizeOrElse(otherIterable, 10));

        final MutableListX<R> list = MutableListX.withInitCapacity(resultListSize);
        while (iterator.hasNext() && otherIterator.hasNext()) {
            final T next = iterator.next();
            final A otherNext = otherIterator.next();
            list.add(function.apply(next, otherNext));
        }
        return list;
    }

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

    IterableX<T> skipWhileInclusive(@NotNull Predicate<T> predicate);

    IterableX<T> skipWhile(@NotNull Predicate<T> predicate);

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
