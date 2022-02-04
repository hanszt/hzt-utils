package hzt.collections;

import hzt.utils.It;
import hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface MutableListX<E> extends List<E>, ListX<E>, Transformable<MutableListX<E>> {

    static <E> MutableListX<E> empty() {
        return new ArrayListX<>();
    }

    static <E> MutableListX<E> withInitCapacity(int capacity) {
        return new ArrayListX<>(capacity);
    }

    static <E> MutableListX<E> of(@NotNull Iterable<E> iterable) {
        return new ArrayListX<>(iterable);
    }

    static <E> MutableListX<E> of(@NotNull Collection<E> collection) {
        return new ArrayListX<>(collection);
    }

    @SafeVarargs
    static <E> MutableListX<E> of(@NotNull E... values) {
        return new ArrayListX<>(values);
    }

    static <E> MutableListX<E> of(@NotNull E value) {
        return new ArrayListX<>(value);
    }

    @Override
    default <R> MutableListX<R> map(Function<E, R> mapper) {
        return toMutableListOf(mapper);
    }

    @Override
    default MutableListX<E> filter(Predicate<E> predicate) {
        return filterToMutableList(predicate);
    }

    @Override
    default <R> MutableListX<E> filterNotNullBy(Function<? super E, ? extends R> function, Predicate<? super R> predicate) {
        return filterNotNullToMutableListBy(function, predicate);
    }

    @Override
    default MutableListX<E> filterNot(Predicate<E> predicate) {return filter(predicate.negate());
    }

    @Override
    default MutableListX<E> takeWhile(Predicate<E> predicate) {
        return takeToMutableListWhile(predicate);
    }

    @Override
    default <R> MutableListX<R> castIfInstanceOf(Class<R> aClass) {
        return castToMutableListIfInstanceOf(aClass);
    }

    @Override
    default <R> MutableListX<R> mapFiltering(Function<? super E, ? extends R> mapper, Predicate<R> resultFilter) {
        return mapFiltering(It.noFilter(), mapper, resultFilter);
    }

    @Override
    default <R> MutableListX<R> filterMapping(Predicate<E> predicate, Function<E, R> mapper) {
        return mapFiltering(predicate, mapper, It.noFilter());
    }

    @Override
    default <R> MutableListX<R> mapFiltering(Predicate<E> predicate, Function<? super E, ? extends R> mapper, Predicate<R> resultFilter) {
        return mapFilteringToCollection(MutableListX::of, predicate, mapper, resultFilter);
    }
    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return an iterator over the elements in this list in proper sequence
     */

    @Override
    default Stream<E> stream() {
        return List.super.stream();
    }

    ListX<E> toListX();

    MutableListX<E> headTo(int toIndex);

    MutableListX<E> tailFrom(int fromIndex);

    MutableListX<E> subList(int fromIndex, int toIndex);

    @Override
    default MutableListX<E> get() {
        return this;
    }
}
