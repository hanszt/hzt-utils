package org.hzt.utils.iterables;

import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MutableSetX;
import org.hzt.utils.collections.SetX;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.functional_iterator.AtomicIterator;
import org.hzt.utils.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This is a base interface to transform iterables to some other state.
 * <p>
 * It is inspired by the Kotlin standard library
 * <p>
 *
 * @param <T> The Type of the Iterable in the IterableX object
 * @author Hans Zuidervaart
 */
public interface IterableX<T> extends Mappable<T>, Filterable<T>, Skipable<T>, Takeable<T>, Zippable<T>, Windowable<T>,
        Sortable<T>, Distinctable<T>, Stringable<T>, Numerable<T>, Reducable<T>,
        Collectable<T>, Groupable<T>, Streamable<T, Stream<T>>, Indexable<T> {

    default AtomicIterator<T> atomicIterator() {
        return AtomicIterator.of(iterator());
    }

    IterableX<T> plus(@NotNull T value);

    IterableX<T> plus(@NotNull Iterable<? extends T> values);

    IterableX<T> minus(@NotNull T value);

    IterableX<T> minus(@NotNull Iterable<T> values);

    <R> IterableX<R> castIfInstanceOf(@NotNull Class<R> aClass);

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Sequence<T> asSequence() {
        return Sequence.of(this);
    }

    PrimitiveIterable.OfInt mapToInt(@NotNull ToIntFunction<? super T> mapper);

    PrimitiveIterable.OfLong mapToLong(@NotNull ToLongFunction<? super T> toLongMapper);

    PrimitiveIterable.OfDouble mapToDouble(@NotNull ToDoubleFunction<? super T> mapper);

    <K> EntryIterable<K, T> associateBy(@NotNull Function<? super T, ? extends K> keyMapper);

    <V> EntryIterable<T, V> associateWith(@NotNull Function<? super T, ? extends V> valueMapper);

    default <R> void forEach(@NotNull final Function<? super T, ? extends R> selector,
                             @NotNull final Consumer<? super R> consumer) {
        onEach(selector, consumer);
    }

    @NotNull
    default IterableX<T> onEach(@NotNull final Consumer<? super T> consumer) {
        return onEach(It::self, consumer);
    }

    @NotNull
    default <R> IterableX<T> onEach(@NotNull final Function<? super T, ? extends R> selector,
                                @NotNull final Consumer<? super R> consumer) {
        for (final var t : this) {
            consumer.accept(t != null ? selector.apply(t) : null);
        }
        return this;
    }

    default <R> Sequence<R> then(IterableExtension<T, R> extension) {
        return () -> extension.extend(this).iterator();
    }

    default SetX<T> intersect(@NotNull final Iterable<T> other) {
        final var intersection = toMutableSet();
        final var otherCollection = other instanceof final Collection<T> collection ? collection : MutableSetX.of(other);
        intersection.retainAll(otherCollection);
        return SetX.of(intersection);
    }

    default <S, I extends Iterable<S>, R> SetX<R> intersectionOf(@NotNull final Function<? super T, ? extends I> toIterableMapper,
                                                                 @NotNull final Function<? super S, ? extends R> selector) {
        return IterableReductions.intersectionOf(this, toIterableMapper, selector);
    }

    default <R, I extends Iterable<R>> SetX<R> intersectionOf(@NotNull final Function<? super T, ? extends I> toIterableMapper) {
        return intersectionOf(toIterableMapper, It::self);
    }

    default SetX<T> union(@NotNull final Iterable<T> other) {
        final var union = toMutableSet();
        return SetX.copyOf(union.plus(other));
    }

    default <R> SetX<R> union(@NotNull final Iterable<T> other, @NotNull final Function<? super T, ? extends R> mapper) {
        final MutableSetX<R> union = mapTo(MutableSetX::empty, mapper);
        final SetX<R> setX = ListX.of(other).toSetXOf(mapper);
        setX.forEach(union::add);
        return union;
    }

    default int[] toIntArray(@NotNull final ToIntFunction<? super T> mapper) {
        return asSequence().mapToInt(mapper).toArray();
    }

    default long[] toLongArray(@NotNull final ToLongFunction<? super T> mapper) {
        return asSequence().mapToLong(mapper).toArray();
    }

    default double[] toDoubleArray(@NotNull final ToDoubleFunction<? super T> mapper) {
        return asSequence().mapToDouble(mapper).toArray();
    }

    <R> IterableX<R> scan(R initial, BiFunction<? super R, ? super T, ? extends R> operation);

    default boolean[] toBooleanArray(@NotNull final Predicate<? super T> mapper) {
        final var size = (int) count();
        final var result = new boolean[size];
        var counter = 0;
        for (final var value : this) {
            result[counter] = mapper.test(value);
            counter++;
        }
        return result;
    }

    @Override
    default PrimitiveIterator.@NotNull OfInt indexIterator() {
        return Mappable.super.indexIterator();
    }
}
