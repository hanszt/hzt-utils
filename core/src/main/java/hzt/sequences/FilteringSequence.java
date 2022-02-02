package hzt.sequences;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Predicate;

final class FilteringSequence<T> implements Sequence<T> {

    private final Sequence<T> upstream;
    private final Predicate<T> predicate;
    private final boolean sendWhen;

    FilteringSequence(Sequence<T> upstream, Predicate<T> predicate, boolean sendWhen) {
        this.upstream = upstream;
        this.predicate = predicate;
        this.sendWhen = sendWhen;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new FilteringIterator<>(upstream.iterator(), predicate, sendWhen);
    }

    @Override
    public String toString() {
        return "FilteringSequence{" +
                "upStream=" + upstream +
                ", predicate=" + predicate +
                ", sendWhen=" + sendWhen +
                '}';
    }
}
