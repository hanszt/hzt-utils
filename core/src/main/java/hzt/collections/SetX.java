package hzt.collections;

import hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

public interface SetX<E> extends CollectionX<E>, Transformable<SetX<E>> {

    static <E> SetX<E> empty() {
        return new HashSetX<>();
    }

    static <E> SetX<E> of(Iterable<E> iterable) {
        return new HashSetX<>(iterable);
    }

    @SafeVarargs
    static <E> SetX<E> of(E... values) {
        MutableSetX<E> resultSet = MutableSetX.of(values);
        resultSet.addAll(Arrays.asList(values));
        return resultSet;
    }

    static <E> SetX<E> copyOf(Iterable<E> iterable) {
        return new HashSetX<>(iterable);
    }

    @Override
    int size();

    @Override
    boolean contains(Object value);

    default @NotNull SetX<E> get() {
        return this;
    }

    @Override
    default @NotNull SetX<E> onEach(@NotNull Consumer<? super E> consumer) {
        return SetX.of(CollectionX.super.onEach(consumer));
    }

    @Override
    @NotNull
    default <R> SetX<E> onEach(@NotNull Function<? super E, ? extends R> selector, @NotNull Consumer<? super R> consumer) {
        return SetX.of(CollectionX.super.onEach(selector, consumer));
    }
}
