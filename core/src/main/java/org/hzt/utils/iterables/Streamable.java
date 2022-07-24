package org.hzt.utils.iterables;

import java.util.stream.BaseStream;

/**
 * @param <T> the type of the stream elements
 * @param <S> the type of the stream implementing {@code BaseStream}
 */
public interface Streamable<T, S extends BaseStream<T, S>> {

    S stream();

    default S parallelStream() {
        return stream().parallel();
    }
}
