package hzt.sequences;

import hzt.iterators.SkipWhileIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Predicate;

public class SkipWhileSequence<T> implements Sequence<T> {

    private final Sequence<T> upstream;
    private final Predicate<T> predicate;
    private final boolean inclusive;

    public SkipWhileSequence(Sequence<T> upstream, Predicate<T> predicate, boolean inclusive) {
        this.upstream = upstream;
        this.predicate = predicate;
        this.inclusive = inclusive;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return SkipWhileIterator.of(upstream.iterator(), predicate, inclusive);
    }
}
