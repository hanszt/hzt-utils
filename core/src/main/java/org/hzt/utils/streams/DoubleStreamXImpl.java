package org.hzt.utils.streams;


import java.util.Spliterator;
import java.util.stream.DoubleStream;

final class DoubleStreamXImpl implements DoubleStreamX {

    private final DoubleStream stream;

    DoubleStreamXImpl(DoubleStream stream) {
        this.stream = stream;
    }


    @Override
    public Spliterator.OfDouble spliterator() {
        return stream.spliterator();
    }

    @Override
    public boolean isParallel() {
        return stream.isParallel();
    }
}
