package org.hzt.utils;


import java.util.Objects;
import java.util.function.Supplier;

public final class PreConditions {

    private PreConditions() {
    }

    @SafeVarargs
    public static <T> void requireAllNonNull(final Class<T> aClass, final T... objects) {
        var counter = 1;
        for (final var t : objects) {
            Objects.requireNonNull(t, aClass.getSimpleName() + " " + counter + " is null");
            counter++;
        }
    }

    public static void requireAllNonNull(final Object... objects) {
        final RuntimeException exception = new IllegalArgumentException("Some objects where null");
        var counter = 1;
        for (final var object : objects) {
            if (object == null) {
                exception.addSuppressed(new NullPointerException("object " + counter + " is null"));
            }
            counter++;
        }
        if (exception.getSuppressed().length > 0) {
            throw exception;
        }
    }

    public static void requireGreaterThanOrEqualToZero(final int n) {
        require(n >= 0, () -> errorMessage(n));
    }

    public static void requireGreaterThanOrEqualToZero(final long n) {
        require(n >= 0, () -> errorMessage(n));
    }

    private static String errorMessage(final long n) {
        return "Requested element count " + n + " is less than zero.";
    }

    public static void require(final boolean value, final Supplier<String> messageSupplier) {
        if (!value) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    public static void require(final boolean value) {
        if (!value) {
            throw new IllegalArgumentException();
        }
    }

    public static void rangeCheck(final int size, final int fromIndex, final int toIndex) {
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
}
