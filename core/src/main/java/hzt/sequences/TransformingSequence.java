package hzt.sequences;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

final class TransformingSequence<T, R> implements Sequence<R> {

    private final Sequence<T> upStream;
    private final Function<? super T, ? extends R> mapper;

    TransformingSequence(Sequence<T> upStream, Function<? super T, ? extends R> mapper) {
        this.upStream = upStream;
        this.mapper = mapper;
    }

    @NotNull
    @Override
    public Iterator<R> iterator() {
        return new Iterator<R>() {
            private final Iterator<T> iterator = upStream.iterator();
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public R next() {
                return mapper.apply(iterator.next());
            }
        };
    }
}
