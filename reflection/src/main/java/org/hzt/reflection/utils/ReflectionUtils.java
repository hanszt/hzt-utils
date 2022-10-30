package org.hzt.reflection.utils;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static String getEnclosingMethodName() {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        final int INDEX_OF_WANTED_ELEMENT = 2;
        if (stackTrace.length > INDEX_OF_WANTED_ELEMENT) {
            return stackTrace[INDEX_OF_WANTED_ELEMENT].getMethodName();
        }
        throw new IllegalStateException("Method name could not be obtained");
    }
}
