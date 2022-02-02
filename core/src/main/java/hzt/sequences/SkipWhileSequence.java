package hzt.sequences;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Predicate;

public class SkipWhileSequence<T> implements Sequence<T> {

    private final Sequence<T> upstream;
    private final Predicate<T> predicate;

    public SkipWhileSequence(Sequence<T> upstream, Predicate<T> predicate) {
        this.upstream = upstream;
        this.predicate = predicate;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new SkipWhileIterator<>(upstream.iterator(), predicate);
    }
}
