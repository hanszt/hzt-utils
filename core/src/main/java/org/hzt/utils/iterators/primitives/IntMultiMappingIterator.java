package org.hzt.utils.iterators.primitives;

import org.hzt.utils.collections.primitives.IntMutableListX;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public final class IntMultiMappingIterator implements PrimitiveIterator.OfInt {

    private final OfInt iterator;
    private final IntSequence.IntMapMultiConsumer mapper;

    private OfInt itemIterator = null;

    private IntMultiMappingIterator(@NotNull OfInt iterator, @NotNull IntSequence.IntMapMultiConsumer mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    public static IntMultiMappingIterator of(@NotNull OfInt iterator, @NotNull IntSequence.IntMapMultiConsumer mapper) {
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
            } else {
                IntMutableListX intBuffer = IntMutableListX.empty();
                mapper.accept(iterator.nextInt(), intBuffer::add);
                final OfInt nextItemIterator = intBuffer.iterator();
                if (nextItemIterator.hasNext()) {
                    itemIterator = nextItemIterator;
                    return true;
                }
            }
        }
        return true;
    }
}
