package org.hzt.utils.iterators.primitives;

import org.hzt.utils.sequences.primitives.LongSequence;
import org.hzt.utils.spined_buffers.SpinedBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public final class LongMultiMappingIterator implements PrimitiveIterator.OfLong {

    private final PrimitiveIterator.OfLong iterator;
    private final LongSequence.LongMapMultiConsumer mapper;

    private PrimitiveIterator.OfLong itemIterator = null;

    private LongMultiMappingIterator(@NotNull final OfLong iterator, @NotNull final LongSequence.LongMapMultiConsumer mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    public static LongMultiMappingIterator of(@NotNull final OfLong iterator, @NotNull final LongSequence.LongMapMultiConsumer mapper) {
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
            final var longBuffer = new SpinedBuffer.OfLong();
            mapper.accept(iterator.nextLong(), longBuffer);
            final var nextItemIterator = longBuffer.iterator();
            if (nextItemIterator.hasNext()) {
                itemIterator = nextItemIterator;
                return true;
            }
        }
        return true;
    }
}
