package hzt.iterables;

import hzt.collections.ListView;
import hzt.collections.MutableList;
import hzt.collections.MutableSet;
import hzt.collections.SetView;
import hzt.ranges.DoubleRange;
import hzt.ranges.IntRange;
import hzt.ranges.LongRange;
import hzt.sequences.Sequence;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
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
public interface IterableX<T> extends Mappable<T>, Filterable<T>, Skipable<T>, Takeable<T>, Zippable<T>, Windowable<T>,
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

    default SetView<T> intersect(@NotNull Iterable<T> other) {
        final var intersection = toMutableSet();
        final var otherCollection = other instanceof Collectable<?> ? (Collection<T>) other : MutableList.of(other);
        intersection.retainAll(otherCollection);
        return intersection;
    }

    default <S, I extends Iterable<S>, R> SetView<R> intersectionOf(@NotNull Function<? super T, ? extends I> toIterableMapper,
                                                                    @NotNull Function<? super S, ? extends R> selector) {
        return IterableReductions.intersectionOf(this, toIterableMapper, selector);
    }

    default <R, I extends Iterable<R>> SetView<R> intersectionOf(@NotNull Function<? super T, ? extends I> toIterableMapper) {
        return intersectionOf(toIterableMapper, It::self);
    }

    default SetView<T> union(@NotNull Iterable<T> other) {
        MutableSet<T> union = MutableSet.empty();
        forEach(union::add);
        other.forEach(union::add);
        return union;
    }

    default <R> SetView<R> union(@NotNull Iterable<T> other, @NotNull Function<? super T, ? extends R> mapper) {
        MutableSet<R> union = mapTo(MutableSet::empty, mapper);
        final SetView<R> setView = ListView.of(other).toSetViewOf(mapper);
        setView.forEach(union::add);
        return union;
    }
}
