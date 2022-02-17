package hzt.collections;

import hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

public interface SetView<E> extends CollectionView<E>, Transformable<SetView<E>> {

    static <E> SetView<E> empty() {
        return new HashSetX<>();
    }

    static <E> SetView<E> of(Iterable<E> iterable) {
        return new HashSetX<>(iterable);
    }

    @SafeVarargs
    static <E> SetView<E> of(E... values) {
        MutableSet<E> resultSet = MutableSet.of(values);
        resultSet.addAll(Arrays.asList(values));
        return resultSet;
    }

    static <E> SetView<E> copyOf(Iterable<E> iterable) {
        return new HashSetX<>(iterable);
    }

    @Override
    int size();

    @Override
    boolean contains(Object value);

    default @NotNull SetView<E> get() {
        return this;
    }

    @Override
    default @NotNull SetView<E> onEach(@NotNull Consumer<? super E> consumer) {
        return SetView.of(CollectionView.super.onEach(consumer));
    }

    @Override
    @NotNull
    default <R> SetView<E> onEach(@NotNull Function<? super E, ? extends R> selector, @NotNull Consumer<? super R> consumer) {
        return SetView.of(CollectionView.super.onEach(selector, consumer));
    }
}
