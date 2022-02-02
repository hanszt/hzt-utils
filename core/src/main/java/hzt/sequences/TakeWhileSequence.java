package hzt.sequences;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Predicate;

final class TakeWhileSequence<T> implements Sequence<T> {

    private final Sequence<T> upstream;
    private final Predicate<T> predicate;
    private final boolean inclusive;

    TakeWhileSequence(Sequence<T> upstream, Predicate<T> predicate, boolean inclusive) {
        this.upstream = upstream;
        this.predicate = predicate;
        this.inclusive = inclusive;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new TakeWhileIterator<>(upstream.iterator(), predicate, inclusive);
    }
}
