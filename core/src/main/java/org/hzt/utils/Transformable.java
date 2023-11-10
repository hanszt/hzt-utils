package org.hzt.utils;

import org.hzt.utils.tuples.Pair;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@FunctionalInterface
public interface Transformable<T> extends Supplier<T> {

    @Override
    T get();

    static <T> Transformable<T> from(T t) {
        return () -> t;
    }

    default <R> R let(Function<? super T, ? extends R> mapper) {
        return mapper.apply(get());
    }

    default <R> Transformable<R> run(Function<? super T, ? extends R> mapper) {
        return Transformable.from(let(mapper));
    }

    default T also(Consumer<? super T> block) {
        final T t = get();
        block.accept(t);
        return t;
    }

    default Transformable<T> apply(Consumer<? super T> block) {
        return Transformable.from(also(block));
    }

    default T when(Predicate<? super T> predicate, Consumer<? super T> block) {
        T t = get();
        if (predicate.test(t)) {
            block.accept(t);
        }
        return t;
    }

    default T unless(Predicate<? super T> predicate, Consumer<? super T> block) {
        return when(predicate.negate(), block);
    }

    default Transformable<T> alsoWhen(Predicate<? super T> predicate, Consumer<? super T> block) {
        return Transformable.from(when(predicate, block));
    }

    default Transformable<T> alsoUnless(Predicate<? super T> predicate, Consumer<? super T> block) {
        return Transformable.from(unless(predicate, block));
    }

    default Optional<T> takeIf(Predicate<? super T> predicate) {
        T t = get();
        return predicate.test(t) ? Optional.of(t) : Optional.empty();
    }

    default Optional<T> takeUnless(Predicate<? super T> predicate) {
        T t = get();
        return predicate.test(t) ? Optional.empty() : Optional.of(t);
    }

    default <B> Pair<T, B> to(B other) {
        return Pair.of(get(), other);
    }

}
