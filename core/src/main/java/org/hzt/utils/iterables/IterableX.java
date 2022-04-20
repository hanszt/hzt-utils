package org.hzt.utils.iterables;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.MutableSetX;
import org.hzt.utils.collections.SetX;
import org.hzt.utils.iterators.functional_iterator.IteratorX;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
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
 *
 * @param <T> The Type of the Iterable in the IterableX object
 * @author Hans Zuidervaart
 */
public interface IterableX<T> extends Mappable<T>, Filterable<T>, Skipable<T>, Takeable<T>, Zippable<T>, Windowable<T>,
        Sortable<T>, Distinctable<T>, Stringable<T>, Numerable<T>, Reducable<T>,
        Collectable<T>, Groupable<T>, Streamable<Stream<T>> {

    default IteratorX<T> iteratorX() {
        return IteratorX.of(iterator());
    }

    IterableX<T> plus(@NotNull T value);

    IterableX<T> plus(@NotNull Iterable<T> values);

    <R> IterableX<R> castIfInstanceOf(@NotNull Class<R> aClass);

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Stream<T> parallelStream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Sequence<T> asSequence() {
        return Sequence.of(this);
    }

    default IntSequence mapToInt(@NotNull ToIntFunction<? super T> keyMapper) {
        return IntSequence.of(asSequence().map(keyMapper::applyAsInt));
    }

    default LongSequence mapToLong(@NotNull ToLongFunction<? super T> keyMapper) {
        return LongSequence.of(asSequence().map(keyMapper::applyAsLong));
    }

    default DoubleSequence mapToDouble(@NotNull ToDoubleFunction<? super T> keyMapper) {
        return DoubleSequence.of(asSequence().map(keyMapper::applyAsDouble));
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

    default int[] toIntArray(@NotNull ToIntFunction<? super T> mapper) {
        return mapToInt(mapper).toArray();
    }

    default long[] toLongArray(@NotNull ToLongFunction<? super T> mapper) {
        return mapToLong(mapper).toArray();
    }

    default double[] toDoubleArray(@NotNull ToDoubleFunction<? super T> mapper) {
        return mapToDouble(mapper).toArray();
    }
}
