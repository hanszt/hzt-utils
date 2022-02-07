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
    default <R> MutableListX<R> map(@NotNull Function<? super E, ? extends R> mapper) {
        return toMutableListOf(mapper);
    }

    @Override
    default MutableListX<E> filter(@NotNull Predicate<E> predicate) {
        return filterToMutableList(predicate);
    }

    @Override
    default <R> MutableListX<E> filterNotNullBy(@NotNull Function<? super E, ? extends R> function,
                                                @NotNull Predicate<? super R> predicate) {
        return filterNotNullToMutableListBy(function, predicate);
    }

    @Override
    default MutableListX<E> filterNot(@NotNull Predicate<E> predicate) {return filter(predicate.negate());
    }

    @Override
    default MutableListX<E> takeWhile(@NotNull Predicate<E> predicate) {
        return takeToMutableListWhile(predicate);
    }

    @Override
    default <R> MutableListX<R> castIfInstanceOf(@NotNull Class<R> aClass) {
        return castToMutableListIfInstanceOf(aClass);
    }

    @Override
    default <R> MutableListX<R> mapFiltering(@NotNull Function<? super E, ? extends R> mapper, @NotNull Predicate<R> resultFilter) {
        return mapFiltering(It::noFilter, mapper, resultFilter);
    }

    @Override
    default <R> MutableListX<R> filterMapping(@NotNull Predicate<? super E> predicate,
                                              @NotNull Function<? super E, ? extends R> mapper) {
        return mapFiltering(predicate, mapper, It::noFilter);
    }

    @Override
    default <R> MutableListX<R> mapFiltering(@NotNull Predicate<? super E> predicate,
                                             @NotNull Function<? super E, ? extends R> mapper,
                                             @NotNull Predicate<R> resultFilter) {
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
    default @NotNull MutableListX<E> get() {
        return this;
    }
}
