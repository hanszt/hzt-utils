package hzt.sequences;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

final class FlatteningSequence<T, R, E> implements Sequence<E> {

    private final Sequence<T> upstream;
    private final Function<T, R> transformer;
    private final Function<R, Iterator<E>> toIteratorFunction;

    FlatteningSequence(Sequence<T> upstream,
                       Function<T, R> transformer,
                       Function<R, Iterator<E>> toIteratorFunction) {
        this.upstream = upstream;
        this.transformer = transformer;
        this.toIteratorFunction = toIteratorFunction;
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new FlatteningIterator<>(upstream.iterator(), transformer, toIteratorFunction);
    }

}
