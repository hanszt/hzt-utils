package org.hzt.utils.iterators.primitives;

import org.hzt.utils.spined_buffers.SpinedBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.BiConsumer;
import java.util.function.DoubleConsumer;

public final class ToDoubleMultiMappingIterator<T> implements PrimitiveIterator.OfDouble {

    private final Iterator<T> iterator;
    private final BiConsumer<? super T, DoubleConsumer> mapper;

    private OfDouble itemIterator = null;

    private ToDoubleMultiMappingIterator(@NotNull Iterator<T> iterator,
                                         @NotNull BiConsumer<? super T, DoubleConsumer> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    public static <T> ToDoubleMultiMappingIterator<T> of(@NotNull Iterator<T> iterator,
                                                         @NotNull BiConsumer<? super T, DoubleConsumer> mapper) {
        return new ToDoubleMultiMappingIterator<>(iterator, mapper);
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
            SpinedBuffer.OfDouble doubleBuffer = new SpinedBuffer.OfDouble();
            mapper.accept(iterator.next(), doubleBuffer);
            final OfDouble nextItemIterator = doubleBuffer.iterator();
            if (nextItemIterator.hasNext()) {
                itemIterator = nextItemIterator;
                return true;
            }
        }
        return true;
    }
}
