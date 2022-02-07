package hzt.sequences;

import hzt.iterators.FlatteningIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

final class FlatteningSequence<T, R> implements Sequence<R> {

    private final Sequence<T> upstream;
    private final Function<T, Iterator<R>> toIteratorFunction;

    FlatteningSequence(Sequence<T> upstream,
                       Function<T, Iterator<R>> toIteratorFunction) {
        this.upstream = upstream;
        this.toIteratorFunction = toIteratorFunction;
    }

    @NotNull
    @Override
    public Iterator<R> iterator() {
        return FlatteningIterator.of(upstream.iterator(), toIteratorFunction);
    }

}
