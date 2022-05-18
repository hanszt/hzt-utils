package org.hzt.utils.sequences;

import org.hzt.utils.iterators.functional_iterator.IteratorX;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
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
        final Iterator<T> iterator = upstream.iterator();
        final Set<K> observed = new HashSet<>();
        return ((IteratorX<T>) action -> nextDistinctValue(iterator, observed, action)).asIterator();
    }

    private boolean nextDistinctValue(Iterator<T> iterator, Set<K> observed, Consumer<? super T> action) {
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (observed.add(selector.apply(next))) {
                action.accept(next);
                return true;
            }
        }
        return false;
    }
}
