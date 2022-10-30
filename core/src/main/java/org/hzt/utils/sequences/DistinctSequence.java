package org.hzt.utils.sequences;

import org.hzt.utils.iterators.functional_iterator.AtomicIterator;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
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
        final var iterator = AtomicIterator.of(upstream.iterator());
        final Set<K> observed = new HashSet<>();
        final AtomicIterator<T> atomicIterator = action -> nextDistinctValue(iterator, observed, action);
        return atomicIterator.asIterator();
    }

    private boolean nextDistinctValue(AtomicIterator<T> iterator, Set<K> observed, Consumer<? super T> action) {
        var reference = new AtomicReference<T>();
        while (iterator.tryAdvance(reference::set)) {
            var next = reference.get();
            if (observed.add(selector.apply(next))) {
                action.accept(next);
                return true;
            }
        }
        return false;
    }
}
