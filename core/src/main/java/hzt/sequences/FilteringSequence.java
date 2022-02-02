package hzt.sequences;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Predicate;

final class FilteringSequence<T> implements Sequence<T> {

    private final Sequence<T> upStream;
    private final Predicate<T> predicate;
    private final boolean sendWhen;

    FilteringSequence(Sequence<T> upStream, Predicate<T> predicate, boolean sendWhen) {
        this.upStream = upStream;
        this.predicate = predicate;
        this.sendWhen = sendWhen;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new FilteringIterator<>(upStream.iterator(), predicate, sendWhen);
    }

    @Override
    public String toString() {
        return "FilteringSequence{" +
                "upStream=" + upStream +
                ", predicate=" + predicate +
                ", sendWhen=" + sendWhen +
                '}';
    }
}
