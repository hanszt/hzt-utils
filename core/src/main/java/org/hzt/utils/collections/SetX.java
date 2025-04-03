package org.hzt.utils.collections;

import org.hzt.utils.Transformable;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;

public interface SetX<E> extends CollectionX<E>, Transformable<SetX<E>> {

    static <E> SetX<E> empty() {
        return new UnmodifiableSetX<>();
    }

    static <E> SetX<E> of(final Iterable<E> iterable) {
        return new UnmodifiableSetX<>(iterable);
    }

    @SafeVarargs
    static <E> SetX<E> of(final E... values) {
        return new UnmodifiableSetX<>(values);
    }

    static <E> SetX<E> build(final Consumer<? super MutableSetX<E>> mutableSetConsumer) {
        return new LinkedHashSetX<>(mutableSetConsumer);
    }

    static <E> SetX<E> build(int size, final Consumer<? super MutableSetX<E>> mutableSetConsumer) {
        return new LinkedHashSetX<>(size, mutableSetConsumer);
    }

    static <E> SetX<E> copyOf(final Iterable<E> iterable) {
        return new UnmodifiableSetX<>(iterable);
    }

    static <E> SetX<E> copyOf(final Collection<E> collection) {
        return new UnmodifiableSetX<>(collection);
    }

    default SetX<E> get() {
        return this;
    }

    @Override
    default SetX<E> onEach(final Consumer<? super E> consumer) {
        return SetX.of(CollectionX.super.onEach(consumer));
    }

    @Override
    default <R> SetX<E> onEach(final Function<? super E, ? extends R> selector, final Consumer<? super R> consumer) {
        return SetX.of(CollectionX.super.onEach(selector, consumer));
    }

    @Override
    default Set<E> toSet() {
        return switch (this) {
            case LinkedHashSetX<E> s when s.isUnmodifiable -> asSet();
            default -> CollectionX.super.toSet();
        };
    }

    private AbstractSet<E> asSet() {
        return new AbstractSet<>() {
            @Override
            public Iterator<E> iterator() {
                return SetX.this.iterator();
            }

            @Override
            public int size() {
                return SetX.this.size();
            }
        };
    }

    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(iterator(), size(), Spliterator.DISTINCT);
    }
}
