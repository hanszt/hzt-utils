package org.hzt.utils;

public final class Objects {

    private Objects() {
    }

    public static <T> T requireNonNull(T object) {
        return requireNonNull(object, "object must not be null");
    }

    public static <T> T requireNonNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }
}
