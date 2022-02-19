package hzt.iterables.primitives;

/**
 * @param <S> the PrimitiveStream
 */
public interface PrimitiveStreamable<S> {

    S stream();

    S parallelStream();
}
