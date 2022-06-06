package org.hzt.utils.streams;

import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.stream.Stream;

final class StreamXImpl<T> implements StreamX<T> {

    private final Stream<T> stream;

    StreamXImpl(Stream<T> stream) {
        this.stream = stream;
    }


    @NotNull
    @Override
    public Spliterator<T> spliterator() {
        return stream.spliterator();
    }

    @Override
    public boolean isParallel() {
        return stream.isParallel();
    }
}
