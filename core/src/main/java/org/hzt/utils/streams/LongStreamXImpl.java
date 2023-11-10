package org.hzt.utils.streams;


import java.util.Spliterator;
import java.util.stream.LongStream;

final class LongStreamXImpl implements LongStreamX {

    private final LongStream stream;

    LongStreamXImpl(LongStream stream) {
        this.stream = stream;
    }


    @Override
    public Spliterator.OfLong spliterator() {
        return stream.spliterator();
    }

    @Override
    public boolean isParallel() {
        return stream.isParallel();
    }
}
