package org.hzt.utils;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static String getEnclosingMethodName() {
        return StackWalker.getInstance()
                .walk(frames -> frames.skip(1)
                        .findFirst()
                        .map(StackWalker.StackFrame::getMethodName)
                        .orElseThrow());
    }
}
