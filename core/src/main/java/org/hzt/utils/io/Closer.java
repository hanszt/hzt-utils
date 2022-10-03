package org.hzt.utils.io;

import org.hzt.utils.function.ThrowingConsumer;
import org.hzt.utils.function.ThrowingFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public final class Closer<T> implements AutoCloseable {

    private final T resource;
    private final ThrowingConsumer<? super T> closeable;

    private Closer(T resource, ThrowingConsumer<? super T> consumingAutoClosable) {
        this.resource = resource;
        this.closeable = consumingAutoClosable;
    }

    public static <T> Closer<T> forResource(@NotNull T resource,
                                            @NotNull ThrowingConsumer<? super T> consumingAutoClosable) {
        return new Closer<>(resource, consumingAutoClosable);
    }

    @Override
    public void close() {
        try {
            closeable.accept(resource);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public <R> R apply(@NotNull ThrowingFunction<? super T, ? extends R> function) {
        try {
            return function.apply(resource);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public <R> R applyAndClose(@NotNull ThrowingFunction<? super T, ? extends R> function) {
        try (this) {
            return apply(function);
        }
    }

    public void execute(@NotNull ThrowingConsumer<? super T> consumer) {
        try {
            consumer.accept(resource);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void executeAndClose(@NotNull ThrowingConsumer<? super T> consumer) {
        try (this) {
            execute(consumer);
        }
    }

    public boolean test(@NotNull Predicate<? super T> predicate) {
        return predicate.test(resource);
    }

    public boolean testAndClose(@NotNull Predicate<? super T> predicate) {
        try (this) {
            return predicate.test(resource);
        }
    }

    public T getResource() {
        return resource;
    }
}
