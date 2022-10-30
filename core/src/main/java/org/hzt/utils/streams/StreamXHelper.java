package org.hzt.utils.streams;

import java.util.Spliterator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

final class StreamXHelper {

    private StreamXHelper() {
    }

    static <T> Stream<T> stream(Stream<T> stream) {
        final Spliterator<T> spliterator = stream.spliterator();
        final boolean parallel = stream instanceof StreamXImpl && stream.isParallel();
        return stream(spliterator, parallel);
    }

    static <T> Stream<T> stream(final Spliterator<T> spliterator, final boolean parallel) {
        if (spliterator.hasCharacteristics(Spliterator.IMMUTABLE) ||
                spliterator.hasCharacteristics(Spliterator.CONCURRENT)) {
            return StreamSupport.stream(spliterator, parallel);
        }
        return StreamSupport.stream(() -> spliterator, spliterator.characteristics(), parallel);
    }

    static IntStream stream(IntStream stream) {
        final Spliterator.OfInt spliterator = stream.spliterator();
        final boolean parallel = stream instanceof IntStreamXImpl && stream.isParallel();
        return stream(spliterator, parallel);
    }

    static IntStream stream(Spliterator.OfInt spliterator, boolean parallel) {
        if (spliterator.hasCharacteristics(Spliterator.IMMUTABLE) ||
                spliterator.hasCharacteristics(Spliterator.CONCURRENT)) {
            return StreamSupport.intStream(spliterator, parallel);
        }
        return StreamSupport.intStream(() -> spliterator, spliterator.characteristics(), parallel);
    }

    static LongStream stream(Spliterator.OfLong spliterator, boolean parallel) {
        if (spliterator.hasCharacteristics(Spliterator.IMMUTABLE) ||
                spliterator.hasCharacteristics(Spliterator.CONCURRENT)) {
            return StreamSupport.longStream(spliterator, parallel);
        }
        return StreamSupport.longStream(() -> spliterator, spliterator.characteristics(), parallel);
    }

    static LongStream stream(LongStream stream) {
        final Spliterator.OfLong spliterator = stream.spliterator();
        final boolean parallel = stream instanceof LongStreamXImpl && stream.isParallel();
        return stream(spliterator, parallel);
    }

    static DoubleStream stream(DoubleStream stream) {
        final Spliterator.OfDouble spliterator = stream.spliterator();
        final boolean parallel = stream instanceof DoubleStreamXImpl && stream.isParallel();
        return stream(spliterator, parallel);
    }

    static DoubleStream stream(Spliterator.OfDouble spliterator, boolean parallel) {
        if (spliterator.hasCharacteristics(Spliterator.IMMUTABLE) ||
                spliterator.hasCharacteristics(Spliterator.CONCURRENT)) {
            return StreamSupport.doubleStream(spliterator, parallel);
        }
        return StreamSupport.doubleStream(() -> spliterator, spliterator.characteristics(), parallel);
    }
}
