package org.hzt.utils.streams;


import java.util.Spliterator;
import java.util.stream.Stream;

class StreamXImpl<T> implements StreamX<T> {

    private final Stream<T> stream;

    StreamXImpl(final Stream<T> stream) {
        this.stream = stream;
    }


    @Override
    public Spliterator<T> spliterator() {
        return stream.spliterator();
    }

    @Override
    public boolean isParallel() {
        return stream.isParallel();
    }
}
