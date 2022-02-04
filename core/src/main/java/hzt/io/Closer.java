package hzt.io;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public final class Closer<T> implements AutoCloseable {

    private final T resource;
    private final ThrowingConsumer<T> closeable;

    private Closer(T resource, ThrowingConsumer<T> consumingAutoClosable) {
        this.resource = resource;
        this.closeable = consumingAutoClosable;
    }

    public static <T> Closer<T> forResource(@NotNull T resource, @NotNull ThrowingConsumer<T> consumingAutoClosable) {
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

    public <R> R apply(@NotNull ThrowingFunction<T, R> function) {
        try {
            return function.apply(resource);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void execute(@NotNull ThrowingConsumer<T> function) {
        try {
            function.accept(resource);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean test(@NotNull Predicate<T> predicate) {
        return predicate.test(resource);
    }

    public T getResource() {
        return resource;
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T> {

        @SuppressWarnings("all")
        void accept(T t) throws Exception;
    }

    @FunctionalInterface
    public interface ThrowingFunction<T, R> {

        @SuppressWarnings("all")
        R apply(T t) throws Exception;
    }
}
