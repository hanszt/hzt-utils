package org.hzt.utils.sequences;

import org.hzt.utils.function.IndexedFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

final class TransformingIndexedSequence<T, R> implements Sequence<R> {

    private final Sequence<T> upStream;
    private final IndexedFunction<? super T, ? extends R> mapper;

    TransformingIndexedSequence(Sequence<T> upStream, IndexedFunction<? super T, ? extends R> mapper) {
        this.upStream = upStream;
        this.mapper = mapper;
    }

    @NotNull
    @Override
    public Iterator<R> iterator() {
        return new Iterator<>() {
            private final Iterator<T> iterator = upStream.iterator();
            private int index = 0;
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public R next() {
                var prevIndex = index;
                if (prevIndex < 0) {
                    throw new IllegalStateException("indexed iterator index overflow");
                }
                return mapper.apply(index++, iterator.next());
            }
        };
    }
}
