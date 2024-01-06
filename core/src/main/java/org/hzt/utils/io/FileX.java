package org.hzt.utils.io;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterables.Collectable;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.strings.StringX;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public final class FileX extends File {

    private static final long serialVersionUID = 123L;

    private FileX(final String pathname) {
        super(pathname);
    }

    public static FileX fromResource(final String name) {
        return Optional.ofNullable(getCallerClass().getResource(name))
                .map(URL::getFile)
                .map(FileX::new)
                .orElseThrow(() -> new IllegalStateException("Could not find resource at '" + name + "'"));
    }

    private static Class<?> getCallerClass() {
        final StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stElements.length; i++) {
            final StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(FileX.class.getName()) && ste.getClassName().indexOf("java.lang.Thread") != 0) {
                return ste.getClass();
            }
        }
        throw new IllegalStateException("No caller class found...");
    }

    public static FileX of(final String pathName) {
        return new FileX(pathName);
    }

    public static FileX of(final Path path) {
        return new FileX(path.toString());
    }

    public ListX<String> readLines() {
        return readLines(StandardCharsets.UTF_8);
    }

    public ListX<String> readLines(final Charset charset) {
        return useLines(Collectable::toListX, charset);
    }

    public String readText(final Charset charset) {
        return useLines(s -> s.joinToString("\n"), charset);
    }

    public String readText() {
        return readText(StandardCharsets.UTF_8);
    }

    public StringX readTextX(final Charset charset) {
        return useLines(s -> s.joinToStringX(String.format("%n")), charset);
    }

    public StringX readTextX() {
        return readTextX(StandardCharsets.UTF_8);
    }

    public <T> T useLines(final Function<? super Sequence<String>, ? extends T> block) {
        return useLines(block, StandardCharsets.UTF_8);
    }

    public <T> T useLines(final Function<? super Sequence<String>, ? extends T> block, final Charset charset) {
        try (final Stream<String> lines = Files.lines(Paths.get(getPath()), charset)) {
            return block.apply(Sequence.of(lines::iterator));
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
