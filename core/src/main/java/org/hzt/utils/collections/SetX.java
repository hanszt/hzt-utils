package org.hzt.utils.collections;

import org.hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;

public interface SetX<E> extends CollectionX<E>, Transformable<SetX<E>> {

    static <E> SetX<E> empty() {
        return new ImmutableSetX<>();
    }

    static <E> SetX<E> of(final Iterable<E> iterable) {
        return new ImmutableSetX<>(iterable);
    }

    @SafeVarargs
    static <E> SetX<E> of(final E... values) {
        return new ImmutableSetX<>(values);
    }

    static <E> SetX<E> copyOf(final Iterable<E> iterable) {
        return new ImmutableSetX<>(iterable);
    }

    static <E> SetX<E> copyOf(final Collection<E> collection) {
        return new ImmutableSetX<>(collection);
    }

    @Override
    int size();

    @Override
    boolean contains(Object value);

    default @NotNull SetX<E> get() {
        return this;
    }

    @Override
    default @NotNull SetX<E> onEach(@NotNull final Consumer<? super E> consumer) {
        return SetX.of(CollectionX.super.onEach(consumer));
    }

    @Override
    @NotNull
    default <R> SetX<E> onEach(@NotNull final Function<? super E, ? extends R> selector, @NotNull final Consumer<? super R> consumer) {
        return SetX.of(CollectionX.super.onEach(selector, consumer));
    }

    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(iterator(), size(), Spliterator.DISTINCT);
    }
}
