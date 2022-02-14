package hzt.sequences;

import hzt.iterators.FilteringIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Predicate;

final class FilteringSequence<T> implements Sequence<T> {

    private final Sequence<T> upstream;
    private final Predicate<T> predicate;

    FilteringSequence(Sequence<T> upstream, Predicate<T> predicate) {
        this.upstream = upstream;
        this.predicate = predicate;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return FilteringIterator.of(upstream.iterator(), predicate, true);
    }

    @Override
    public String toString() {
        return "FilteringSequence{" +
                "upStream=" + upstream +
                ", predicate=" + predicate +
                '}';
    }
}
