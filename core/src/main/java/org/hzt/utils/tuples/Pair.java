package org.hzt.utils.tuples;

import org.hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class Pair<A, B> implements Transformable<Pair<A, B>> {

    private final A first;
    private final B second;

    private Pair(final A first, final B second) {
        this.first = first;
        this.second = second;
    }

    public A first() {
        return first;
    }

    public B second() {
        return second;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        //noinspection unchecked
        final var that = (Pair<A ,B>) obj;
        return Objects.equals(this.first, that.first) &&
                Objects.equals(this.second, that.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    public static <A, B> Pair<A, B> of(final A first, final B second) {
        return new Pair<>(first, second);
    }

    public static <A, B> Pair<A, B> ofEntry(final Map.Entry<A, B> entry) {
        return new Pair<>(entry.getKey(), entry.getValue());
    }

    public <R> R to(@NotNull final BiFunction<? super A, ? super B, ? extends R> mapper) {
        return mapper.apply(first, second);
    }

    public <C> Triple<A, B, C> plus(final C third) {
        return Triple.of(first, second, third);
    }

    public <A1, B1> Pair<A1, B1> mapBoth(@NotNull final Function<? super A, ? extends A1> firstValueMapper,
                                         @NotNull final Function<? super B, ? extends B1> secondValueMapper) {
        return Pair.of(firstValueMapper.apply(first), secondValueMapper.apply(second));
    }

    public <A1> Pair<A1, B> mapFirst(@NotNull final Function<? super A, ? extends A1> firstValueMapper) {
        return Pair.of(firstValueMapper.apply(first), second);
    }

    public <B1> Pair<A, B1> mapSecond(@NotNull final Function<? super B, ? extends B1> secondValueMapper) {
        return Pair.of(first, secondValueMapper.apply(second));
    }

    @Override
    public String toString() {
        return "Pair[" +
                "first=" + first + ", " +
                "second=" + second + ']';
    }

    @Override
    public @NotNull Pair<A, B> get() {
        return this;
    }
}
