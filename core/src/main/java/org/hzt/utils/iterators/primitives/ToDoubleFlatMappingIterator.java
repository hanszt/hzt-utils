package org.hzt.utils.iterators.primitives;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.Function;

final class ToDoubleFlatMappingIterator<T> implements PrimitiveIterator.OfDouble {

    private final Iterator<T> iterator;
    private final Function<? super T, ? extends PrimitiveIterator.OfDouble> toIteratorFunction;

    private PrimitiveIterator.OfDouble itemIterator = null;

    ToDoubleFlatMappingIterator(Iterator<T> iterator,
                                Function<? super T, ? extends PrimitiveIterator.OfDouble> toIteratorFunction) {
        this.iterator = iterator;
        this.toIteratorFunction = toIteratorFunction;
    }

    @Override
    public boolean hasNext() {
        return ensureItemIterator();
    }

    @Override
    public double nextDouble() {
        if (!ensureItemIterator()) {
            throw new NoSuchElementException();
        }
        return itemIterator.nextDouble();
    }

    private boolean ensureItemIterator() {
        if (itemIterator != null && !itemIterator.hasNext()) {
            itemIterator = null;
        }
        while (itemIterator == null) {
            if (!iterator.hasNext()) {
                return false;
            }
            final PrimitiveIterator.OfDouble nextItemIterator = toIteratorFunction.apply(iterator.next());
            if (nextItemIterator.hasNext()) {
                itemIterator = nextItemIterator;
                return true;
            }
        }
        return true;
    }
}
