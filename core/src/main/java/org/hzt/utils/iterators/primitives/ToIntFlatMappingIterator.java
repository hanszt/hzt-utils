package org.hzt.utils.iterators.primitives;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.Function;

final class ToIntFlatMappingIterator<T> implements PrimitiveIterator.OfInt {

    private final Iterator<T> iterator;
    private final Function<? super T, ? extends PrimitiveIterator.OfInt> toIteratorFunction;

    private PrimitiveIterator.OfInt itemIterator = null;

    ToIntFlatMappingIterator(Iterator<T> iterator,
                             Function<? super T, ? extends PrimitiveIterator.OfInt> toIteratorFunction) {
        this.iterator = iterator;
        this.toIteratorFunction = toIteratorFunction;
    }

    @Override
    public boolean hasNext() {
        return ensureItemIterator();
    }

    @Override
    public int nextInt() {
        if (!ensureItemIterator()) {
            throw new NoSuchElementException();
        }
        return itemIterator.nextInt();
    }

    private boolean ensureItemIterator() {
        if (itemIterator != null && !itemIterator.hasNext()) {
            itemIterator = null;
        }
        while (itemIterator == null) {
            if (!iterator.hasNext()) {
                return false;
            }
            final PrimitiveIterator.OfInt nextItemIterator = toIteratorFunction.apply(iterator.next());
            if (nextItemIterator.hasNext()) {
                itemIterator = nextItemIterator;
                return true;
            }
        }
        return true;
    }
}
