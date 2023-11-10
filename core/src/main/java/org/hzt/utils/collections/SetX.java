package org.hzt.utils.collections;

import org.hzt.utils.Transformable;

import java.util.Collection;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;

public interface SetX<E> extends CollectionX<E>, Transformable<SetX<E>> {

    static <E> SetX<E> empty() {
        return new ImmutableSetX<>();
    }

    static <E> SetX<E> of(Iterable<E> iterable) {
        return new ImmutableSetX<>(iterable);
    }

    @SafeVarargs
    static <E> SetX<E> of(E... values) {
        return new ImmutableSetX<>(values);
    }

    static <E> SetX<E> copyOf(Iterable<E> iterable) {
        return new ImmutableSetX<>(iterable);
    }

    static <E> SetX<E> copyOf(Collection<E> collection) {
        return new ImmutableSetX<>(collection);
    }

    @Override
    int size();

    @Override
    boolean contains(Object value);

    default SetX<E> get() {
        return this;
    }

    @Override
    default SetX<E> onEach(Consumer<? super E> consumer) {
        return SetX.of(CollectionX.super.onEach(consumer));
    }

    @Override
    default <R> SetX<E> onEach(Function<? super E, ? extends R> selector, Consumer<? super R> consumer) {
        return SetX.of(CollectionX.super.onEach(selector, consumer));
    }

    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(iterator(), size(), Spliterator.DISTINCT);
    }
}
