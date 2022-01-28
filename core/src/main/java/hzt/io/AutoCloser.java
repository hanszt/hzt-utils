package hzt.io;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Stream;

public class AutoCloser implements AutoCloseable {

    private final Deque<AutoCloseable> autoCloseables = new ArrayDeque<>();

    @Override
    public void close() {
        IllegalStateException exception = new IllegalStateException("Could not close all");
        for (AutoCloseable autoCloseable : autoCloseables) {
            try {
                autoCloseable.close();
            } catch (Exception e) {
                exception.addSuppressed(e);
            }
        }
        if (exception.getSuppressed().length > 0) {
            throw exception;
        }
    }

    public void addCloseFunctions(AutoCloseable... closeFunctions) {
        Stream.of(closeFunctions).forEach(this.autoCloseables::push);
    }
}
