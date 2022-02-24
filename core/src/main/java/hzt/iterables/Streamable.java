package hzt.iterables;

/**
 * @param <S> the Stream type
 */
public interface Streamable<S> {

    S stream();

    S parallelStream();
}
