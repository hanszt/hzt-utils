package org.hzt.utils;

import org.hzt.utils.tuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@FunctionalInterface
public interface Transformable<T> extends Supplier<T> {

    @NotNull
    @Override
    T get();

    static <T> Transformable<T> from(@NotNull final T t) {
        return () -> t;
    }

    default <R> R let(@NotNull final Function<? super T, ? extends R> mapper) {
        return mapper.apply(get());
    }

    default <R> Transformable<R> run(@NotNull final Function<? super T, ?extends R> mapper) {
        return Transformable.from(let(mapper));
    }

    default T also(@NotNull final Consumer<? super T> block) {
        final var t = get();
        block.accept(t);
        return t;
    }

    default Transformable<T> apply(@NotNull final Consumer<? super T> block) {
        return Transformable.from(also(block));
    }

    default T when(@NotNull final Predicate<? super T> predicate, @NotNull final Consumer<? super T> block) {
        final var t = get();
        if (predicate.test(t)) {
            block.accept(t);
        }
        return t;
    }

    default T unless(@NotNull final Predicate<? super T> predicate, @NotNull final Consumer<? super T> block) {
        return when(predicate.negate(), block);
    }

    default Transformable<T> alsoWhen(@NotNull final Predicate<? super T> predicate, final Consumer<? super T> block) {
        return Transformable.from(when(predicate, block));
    }

    default Transformable<T> alsoUnless(@NotNull final Predicate<? super T> predicate, final Consumer<? super T> block) {
        return Transformable.from(unless(predicate, block));
    }

    default Optional<T> takeIf(@NotNull final Predicate<? super T> predicate) {
        final var t = get();
        return predicate.test(t) ? Optional.of(t) : Optional.empty();
    }

    default Optional<T> takeUnless(@NotNull final Predicate<? super T> predicate) {
        final var t = get();
        return predicate.test(t) ? Optional.empty() : Optional.of(t);
    }

    default <B> Pair<T, B> to(final B other) {
        return Pair.of(get(), other);
    }

}
