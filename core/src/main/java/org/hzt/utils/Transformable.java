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

    static <T> Transformable<T> from(@NotNull T t) {
        return () -> t;
    }

    default <R> R let(@NotNull Function<? super T, ? extends R> mapper) {
        return mapper.apply(get());
    }

    default <R> Transformable<R> run(@NotNull Function<? super T, ?extends R> mapper) {
        return Transformable.from(let(mapper));
    }

    default T also(@NotNull Consumer<? super T> block) {
        final T t = get();
        block.accept(t);
        return t;
    }

    default Transformable<T> apply(@NotNull Consumer<? super T> block) {
        return Transformable.from(also(block));
    }

    default T when(@NotNull Predicate<T> predicate, @NotNull Consumer<? super T> block) {
        T t = get();
        if (predicate.test(t)) {
            block.accept(t);
        }
        return t;
    }

    default T unless(@NotNull Predicate<T> predicate, @NotNull Consumer<? super T> block) {
        return when(predicate.negate(), block);
    }

    default Transformable<T> alsoWhen(@NotNull Predicate<T> predicate, Consumer<? super T> block) {
        return Transformable.from(when(predicate, block));
    }

    default Transformable<T> alsoUnless(@NotNull Predicate<T> predicate, Consumer<? super T> block) {
        return Transformable.from(unless(predicate, block));
    }

    default Optional<T> takeIf(@NotNull Predicate<T> predicate) {
        T t = get();
        return predicate.test(t) ? Optional.of(t) : Optional.empty();
    }

    default Optional<T> takeUnless(@NotNull Predicate<T> predicate) {
        T t = get();
        return predicate.test(t) ? Optional.empty() : Optional.of(t);
    }

    default <B> Pair<T, B> to(B other) {
        return Pair.of(get(), other);
    }

}
