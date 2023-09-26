package org.hzt.utils.streams;

import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.stream.DoubleStream;

final class DoubleStreamXImpl implements DoubleStreamX {

    private final DoubleStream stream;

    DoubleStreamXImpl(final DoubleStream stream) {
        this.stream = stream;
    }


    @NotNull
    @Override
    public Spliterator.OfDouble spliterator() {
        return stream.spliterator();
    }

    @Override
    public boolean isParallel() {
        return stream.isParallel();
    }
}
