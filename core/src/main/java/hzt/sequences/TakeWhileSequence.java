package hzt.sequences;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Predicate;

final class TakeWhileSequence<T> implements Sequence<T> {

    private final Sequence<T> upStream;
    private final Predicate<T> predicate;
    private final boolean inclusive;

    TakeWhileSequence(Sequence<T> upStream, Predicate<T> predicate, boolean inclusive) {
        this.upStream = upStream;
        this.predicate = predicate;
        this.inclusive = inclusive;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new TakeWhileIterator<>(upStream.iterator(), predicate, inclusive);
    }
}
