package org.hzt.utils.io;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterables.Collectable;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.strings.StringX;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.stream.Stream;

public final class FileX extends File {

    private static final long serialVersionUID = 123L;

    private FileX(@NotNull String pathname) {
        super(pathname);
    }

    public static FileX of(String pathName) {
        return new FileX(pathName);
    }

    public ListX<StringX> readLines() {
        return readLines(StandardCharsets.UTF_8);
    }

    public ListX<StringX> readLines(Charset charset) {
        return useLines(Collectable::toListX, charset);
    }

    public String readText(Charset charset) {
        return useLines(s -> s.joinToString("\n"), charset);
    }

    public String readText() {
        return readText(StandardCharsets.UTF_8);
    }

    public StringX readTextX(Charset charset) {
        return useLines(s -> s.joinToStringX(String.format("%n")), charset);
    }

    public StringX readTextX() {
        return readTextX(StandardCharsets.UTF_8);
    }

    public <T> T useLines(Function<? super Sequence<StringX>, ? extends T> block) {
        return useLines(block, StandardCharsets.UTF_8);
    }

    public <T> T useLines(Function<? super Sequence<StringX>, ? extends T> block, Charset charset) {
        try(Stream<String> lines = Files.lines(Paths.get(getPath()), charset)) {
            return block.apply(Sequence.ofStream(lines).map(StringX::of));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
