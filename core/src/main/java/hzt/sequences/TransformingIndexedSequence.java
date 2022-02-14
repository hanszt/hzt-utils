package hzt.sequences;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiFunction;

final class TransformingIndexedSequence<T, R> implements Sequence<R> {

    private final Sequence<T> upStream;
    private final BiFunction<Integer, ? super T, ? extends R> mapper;

    TransformingIndexedSequence(Sequence<T> upStream, BiFunction<Integer, ? super T, ? extends R> mapper) {
        this.upStream = upStream;
        this.mapper = mapper;
    }

    @NotNull
    @Override
    public Iterator<R> iterator() {
        return new Iterator<R>() {
            private final Iterator<T> iterator = upStream.iterator();
            private int index = 0;
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public R next() {
                int prevIndex = index;
                if (prevIndex < 0) {
                    throw new IllegalStateException("indexed iterator index overflow");
                }
                return mapper.apply(index++, iterator.next());
            }
        };
    }
}
