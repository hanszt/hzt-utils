package org.hzt.utils.iterators.primitives;

import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.spined_buffers.SpinedBuffer;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public final class DoubleMultiMappingIterator implements PrimitiveIterator.OfDouble {

    private final OfDouble iterator;
    private final DoubleSequence.DoubleMapMultiConsumer mapper;

    private OfDouble itemIterator = null;

    private DoubleMultiMappingIterator(final OfDouble iterator, final DoubleSequence.DoubleMapMultiConsumer mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    public static DoubleMultiMappingIterator of(final OfDouble iterator, final DoubleSequence.DoubleMapMultiConsumer mapper) {
        return new DoubleMultiMappingIterator(iterator, mapper);
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
            final var doubleBuffer = new SpinedBuffer.OfDouble();
            mapper.accept(iterator.nextDouble(), doubleBuffer);
            final var nextItemIterator = doubleBuffer.iterator();
            if (nextItemIterator.hasNext()) {
                itemIterator = nextItemIterator;
                return true;
            }
        }
        return true;
    }
}
