package org.hzt.utils.iterators.primitives;

import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.spined_buffers.SpinedBuffer;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public final class IntMultiMappingIterator implements PrimitiveIterator.OfInt {

    private final OfInt iterator;
    private final IntSequence.IntMapMultiConsumer mapper;

    private OfInt itemIterator = null;

    private IntMultiMappingIterator(final OfInt iterator, final IntSequence.IntMapMultiConsumer mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    public static IntMultiMappingIterator of(final OfInt iterator, final IntSequence.IntMapMultiConsumer mapper) {
        return new IntMultiMappingIterator(iterator, mapper);
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
            final SpinedBuffer.OfInt intBuffer = new SpinedBuffer.OfInt();
            mapper.accept(iterator.nextInt(), intBuffer);
            final OfInt nextItemIterator = intBuffer.iterator();
            if (nextItemIterator.hasNext()) {
                itemIterator = nextItemIterator;
                return true;
            }
        }
        return true;
    }
}
