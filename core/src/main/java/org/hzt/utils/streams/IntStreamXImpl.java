package org.hzt.utils.streams;

import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.stream.IntStream;

final class IntStreamXImpl implements IntStreamX {

    private final IntStream stream;

    IntStreamXImpl(IntStream stream) {
        this.stream = stream;
    }


    @NotNull
    @Override
    public Spliterator.OfInt spliterator() {
        return stream.spliterator();
    }

    @Override
    public boolean isParallel() {
        return stream.isParallel();
    }
}
