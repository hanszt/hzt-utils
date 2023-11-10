package org.hzt.utils.iterators.primitives;

import org.hzt.utils.sequences.primitives.LongSequence;
import org.hzt.utils.spined_buffers.SpinedBuffer;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public final class LongMultiMappingIterator implements PrimitiveIterator.OfLong {

    private final PrimitiveIterator.OfLong iterator;
    private final LongSequence.LongMapMultiConsumer mapper;

    private PrimitiveIterator.OfLong itemIterator = null;

    private LongMultiMappingIterator(final OfLong iterator, final LongSequence.LongMapMultiConsumer mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    public static LongMultiMappingIterator of(final OfLong iterator, final LongSequence.LongMapMultiConsumer mapper) {
        return new LongMultiMappingIterator(iterator, mapper);
    }

    @Override
    public boolean hasNext() {
        return ensureItemIterator();
    }

    @Override
    public long nextLong() {
        if (!ensureItemIterator()) {
            throw new NoSuchElementException();
        }
        return itemIterator.nextLong();
    }

    private boolean ensureItemIterator() {
        if (itemIterator != null && !itemIterator.hasNext()) {
            itemIterator = null;
        }
        while (itemIterator == null) {
            if (!iterator.hasNext()) {
                return false;
            }
            final SpinedBuffer.OfLong longBuffer = new SpinedBuffer.OfLong();
            mapper.accept(iterator.nextLong(), longBuffer);
            final OfLong nextItemIterator = longBuffer.iterator();
            if (nextItemIterator.hasNext()) {
                itemIterator = nextItemIterator;
                return true;
            }
        }
        return true;
    }
}
