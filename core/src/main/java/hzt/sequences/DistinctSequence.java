package hzt.sequences;

import hzt.iterators.AbstractIterator;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

record DistinctSequence<T, K>(Sequence<T> upstream, Function<T, K> selector) implements Sequence<T> {

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new AbstractIterator<>() {
            private final Iterator<T> iterator = upstream.iterator();
            private final Set<K> observed = new HashSet<>();

            @Override
            protected void computeNext() {
                while (iterator.hasNext()) {
                    T next = iterator.next();
                    K key = selector.apply(next);
                    if (observed.add(key)) {
                        setNext(next);
                        return;
                    }
                }
                done();
            }
        };
    }
}
