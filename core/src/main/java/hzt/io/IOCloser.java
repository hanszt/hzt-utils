package hzt.io;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Stream;

public class IOCloser implements Closeable {

    private final Deque<Closeable> autoCloseables = new ArrayDeque<>();

    public IOCloser(Closeable... closeFunctions) {
        Stream.of(closeFunctions).forEach(this.autoCloseables::push);
    }

    @Override
    public void close() throws IOException {
        IOException ioException = new IOException("Could not close all");
        for (Closeable closeable : autoCloseables) {
            try {
                closeable.close();
            } catch (IOException e) {
                ioException.addSuppressed(e);
            }
        }
        if (ioException.getSuppressed().length > 0) {
            throw ioException;
        }
    }

    public void addCloseFunctions(Closeable... closeFunctions) {
        Stream.of(closeFunctions).forEach(this.autoCloseables::push);
    }
}
