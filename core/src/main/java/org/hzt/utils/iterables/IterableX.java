package org.hzt.utils.iterables;

import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MutableSetX;
import org.hzt.utils.collections.SetX;
import org.hzt.utils.function.IndexedBiFunction;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.functional_iterator.AtomicIterator;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.streams.StreamX;

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
        Gatherable<T>, Sortable<T>, Distinctable<T>, Stringable<T>, Numerable<T>, Reducable<T>,
        Collectable<T>, Groupable<T>, Streamable<T, Stream<T>>, Indexable<T> {

    default AtomicIterator<T> atomicIterator() {
        return AtomicIterator.of(iterator());
    }

    IterableX<T> plus(T value);

    IterableX<T> plus(Iterable<? extends T> values);

    IterableX<T> minus(T value);

    IterableX<T> minus(Iterable<T> values);

    <R> IterableX<R> castIfInstanceOf(Class<R> aClass);

    default StreamX<T> stream() {
        return StreamX.of(StreamSupport.stream(spliterator(), false));
    }

    default Sequence<T> asSequence() {
        return Sequence.of(this);
    }

    PrimitiveIterable.OfInt mapToInt(ToIntFunction<? super T> mapper);

    PrimitiveIterable.OfLong mapToLong(ToLongFunction<? super T> toLongMapper);

    PrimitiveIterable.OfDouble mapToDouble(ToDoubleFunction<? super T> mapper);

    <K> EntryIterable<K, T> associateBy(Function<? super T, ? extends K> keyMapper);

    <V> EntryIterable<T, V> associateWith(Function<? super T, ? extends V> valueMapper);

    default IterableX<T> onEach(final Consumer<? super T> consumer) {
        return onEach(It::self, consumer);
    }

    default <R> IterableX<T> onEach(final Function<? super T, ? extends R> selector,
                                    final Consumer<? super R> consumer) {
        for (final T t : this) {
            consumer.accept(t != null ? selector.apply(t) : null);
        }
        return this;
    }

    <R> IterableX<R> then(IterableExtension<T, R> extension);

    default SetX<T> intersect(final Iterable<T> other) {
        final MutableSetX<T> intersection = toMutableSet();
        final Collection<T> otherCollection = other instanceof Collectable<?> ? (Collection<T>) other : MutableSetX.of(other);
        intersection.retainAll(otherCollection);
        return SetX.of(intersection);
    }

    default <S, I extends Iterable<S>, R> SetX<R> intersectionOf(final Function<? super T, ? extends I> toIterableMapper,
                                                                 final Function<? super S, ? extends R> selector) {
        return IterableReductions.intersectionOf(this, toIterableMapper, selector);
    }

    default <R, I extends Iterable<R>> SetX<R> intersectionOf(final Function<? super T, ? extends I> toIterableMapper) {
        return intersectionOf(toIterableMapper, It::self);
    }

    default SetX<T> union(final Iterable<T> other) {
        final MutableSetX<T> union = toMutableSet();
        return SetX.copyOf(union.plus(other));
    }

    default <R> SetX<R> union(final Iterable<T> other, final Function<? super T, ? extends R> mapper) {
        final MutableSetX<R> union = mapTo(MutableSetX::empty, mapper);
        final SetX<R> setX = ListX.of(other).toSetXOf(mapper);
        setX.forEach(union::add);
        return union;
    }

    default int[] toIntArray(final ToIntFunction<? super T> mapper) {
        return asSequence().mapToInt(mapper).toArray();
    }

    default long[] toLongArray(final ToLongFunction<? super T> mapper) {
        return asSequence().mapToLong(mapper).toArray();
    }

    default double[] toDoubleArray(final ToDoubleFunction<? super T> mapper) {
        return asSequence().mapToDouble(mapper).toArray();
    }

    <R> IterableX<R> scan(R initial, BiFunction<? super R, ? super T, ? extends R> operation);

    <R> IterableX<R> scanIndexed(R initial, IndexedBiFunction<? super R, ? super T, ? extends R> operation);

    default boolean[] toBooleanArray(final Predicate<? super T> mapper) {
        final int size = (int) count();
        final boolean[] result = new boolean[size];
        int counter = 0;
        for (final T value : this) {
            result[counter] = mapper.test(value);
            counter++;
        }
        return result;
    }

    @Override
    default PrimitiveIterator.OfInt indexIterator() {
        return Mappable.super.indexIterator();
    }
}
