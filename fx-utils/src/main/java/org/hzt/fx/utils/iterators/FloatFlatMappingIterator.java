package org.hzt.fx.utils.iterators;

import org.hzt.fx.utils.function.FloatFunction;

import java.util.NoSuchElementException;

final class FloatFlatMappingIterator implements FloatIterator {

    private final FloatIterator iterator;
    private final FloatFunction<? extends FloatIterator> mapper;

    private FloatIterator itemIterator = null;

    FloatFlatMappingIterator(final FloatIterator iterator, final FloatFunction<? extends FloatIterator> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return ensureItemIterator();
    }

    @Override
    public float nextFloat() {
        if (!ensureItemIterator()) {
            throw new NoSuchElementException();
        }
        return itemIterator.nextFloat();
    }

    private boolean ensureItemIterator() {
        if (itemIterator != null && !itemIterator.hasNext()) {
            itemIterator = null;
        }
        while (itemIterator == null) {
            if (!iterator.hasNext()) {
                return false;
            }
            final FloatIterator nextItemIterator = mapper.apply(iterator.nextFloat());
            if (nextItemIterator.hasNext()) {
                itemIterator = nextItemIterator;
                return true;
            }
        }
        return true;
    }
}
