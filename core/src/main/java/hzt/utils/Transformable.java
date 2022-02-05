package hzt.utils;

import hzt.collections.ListX;
import hzt.tuples.Pair;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@FunctionalInterface
public interface Transformable<T> extends Supplier<T> {

    static <T> Transformable<T> from(T t) {
        return () -> t;
    }

    default <R> R let(Function<T, R> mapper) {
        return mapper.apply(get());
    }

    default <R> Transformable<R> run(Function<T, R> mapper) {
        return Transformable.from(let(mapper));
    }

    default T also(Consumer<T> block) {
        final T t = get();
        block.accept(t);
        return t;
    }

    default Transformable<T> apply(Consumer<T> block) {
        return Transformable.from(also(block));
    }

    default T when(Predicate<T> predicate, Consumer<T> block) {
        T t = get();
        if (predicate.test(t)) {
            block.accept(t);
        }
        return t;
    }

    default T unless(Predicate<T> predicate, Consumer<T> block) {
        return when(predicate.negate(), block);
    }

    default Transformable<T> alsoWhen(Predicate<T> predicate, Consumer<T> block) {
        return Transformable.from(when(predicate, block));
    }

    default Transformable<T> alsoUnless(Predicate<T> predicate, Consumer<T> block) {
        return Transformable.from(unless(predicate, block));
    }

    default Optional<T> takeIf(Predicate<T> predicate) {
        T t = get();
        return predicate.test(t) ? Optional.ofNullable(t) : Optional.empty();
    }

    default Optional<T> takeUnless(Predicate<T> predicate) {
        T t = get();
        return predicate.test(t) ? Optional.empty() : Optional.ofNullable(t);
    }

    default <B> Pair<T, B> to(B other) {
        return Pair.of(get(), other);
    }

    default ListX<T> asListX() {
        return ListX.of(get());
    }

}
