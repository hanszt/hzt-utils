package org.hzt.utils.streams;


import java.util.Spliterator;
import java.util.stream.IntStream;

final class IntStreamXImpl implements IntStreamX {

    private final IntStream stream;

    IntStreamXImpl(final IntStream stream) {
        this.stream = stream;
    }


    @Override
    public Spliterator.OfInt spliterator() {
        return stream.spliterator();
    }

    @Override
    public boolean isParallel() {
        return stream.isParallel();
    }
}
