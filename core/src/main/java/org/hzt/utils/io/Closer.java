package org.hzt.utils.io;

import org.hzt.utils.function.ThrowingConsumer;
import org.hzt.utils.function.ThrowingFunction;

import java.util.function.Predicate;

public final class Closer<T> implements AutoCloseable {

    private final T resource;
    private final ThrowingConsumer<? super T> closeable;

    private Closer(final T resource, final ThrowingConsumer<? super T> consumingAutoClosable) {
        this.resource = resource;
        this.closeable = consumingAutoClosable;
    }

    public static <T> Closer<T> forResource(final T resource,
                                            final ThrowingConsumer<? super T> consumingAutoClosable) {
        return new Closer<>(resource, consumingAutoClosable);
    }

    @Override
    public void close() {
        try {
            closeable.accept(resource);
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public <R> R apply(final ThrowingFunction<? super T, ? extends R> function) {
        try {
            return function.apply(resource);
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public <R> R applyAndClose(final ThrowingFunction<? super T, ? extends R> function) {
        try (this) {
            return apply(function);
        }
    }

    public void execute(final ThrowingConsumer<? super T> consumer) {
        try {
            consumer.accept(resource);
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void executeAndClose(final ThrowingConsumer<? super T> consumer) {
        try (this) {
            execute(consumer);
        }
    }

    public boolean test(final Predicate<? super T> predicate) {
        return predicate.test(resource);
    }

    public boolean testAndClose(final Predicate<? super T> predicate) {
        try (this) {
            return predicate.test(resource);
        }
    }

    public T getResource() {
        return resource;
    }
}
