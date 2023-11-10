package org.hzt.utils;


import java.util.Objects;
import java.util.function.Supplier;

public final class PreConditions {

    private PreConditions() {
    }

    @SafeVarargs
    public static <T> void requireAllNonNull(final Class<T> aClass, T... objects) {
        int counter = 1;
        for (T t : objects) {
            Objects.requireNonNull(t, aClass.getSimpleName() + " " + counter + " is null");
            counter++;
        }
    }

    public static void requireAllNonNull(Object... objects) {
        RuntimeException exception = new IllegalArgumentException("Some objects where null");
        int counter = 1;
        for (Object object : objects) {
            if (object == null) {
                exception.addSuppressed(new NullPointerException("object " + counter + " is null"));
            }
            counter++;
        }
        if (exception.getSuppressed().length > 0) {
            throw exception;
        }
    }

    public static void requireGreaterThanOrEqualToZero(int n) {
        require(n >= 0, () -> errorMessage(n));
    }

    public static void requireGreaterThanOrEqualToZero(long n) {
        require(n >= 0, () -> errorMessage(n));
    }

    private static String errorMessage(long n) {
        return "Requested element count " + n + " is less than zero.";
    }

    public static void require(boolean value, Supplier<String> messageSupplier) {
        if (!value) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    public static void require(boolean value) {
        if (!value) {
            throw new IllegalArgumentException();
        }
    }

    public static void rangeCheck(int size, int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex (" + fromIndex + ") is greater than toIndex (" + toIndex + ").");
        }
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException("fromIndex (" + fromIndex + ") is less than zero.");
        }
        if (toIndex > size) {
            throw new IndexOutOfBoundsException("toIndex (" + toIndex + ") is greater than size (" + size + ").");
        }
    }

    public static <E extends RuntimeException> void requireOrThrow(boolean value, Supplier<E> runtimeException) {
        if (!value) {
            throw runtimeException.get();
        }
    }
}
