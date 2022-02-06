package hzt.sequences;

import hzt.iterators.AbstractIterator;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

final class DistinctSequence<T, K> implements Sequence<T> {

    private final Sequence<T> upstream;
    private final Function<T, K> selector;

    DistinctSequence(Sequence<T> upstream, Function<T, K> selector) {
        this.upstream = upstream;
        this.selector = selector;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new AbstractIterator<T>() {
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
