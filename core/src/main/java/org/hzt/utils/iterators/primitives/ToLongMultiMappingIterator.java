package org.hzt.utils.iterators.primitives;

import org.hzt.utils.spined_buffers.SpinedBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.BiConsumer;
import java.util.function.LongConsumer;

public final class ToLongMultiMappingIterator<T> implements PrimitiveIterator.OfLong {

    private final Iterator<T> iterator;
    private final BiConsumer<? super T, LongConsumer> mapper;

    private OfLong itemIterator = null;

    private ToLongMultiMappingIterator(@NotNull Iterator<T> iterator,
                                       @NotNull BiConsumer<? super T, LongConsumer> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    public static <T> ToLongMultiMappingIterator<T> of(@NotNull Iterator<T> iterator,
                                                       @NotNull BiConsumer<? super T, LongConsumer> mapper) {
        return new ToLongMultiMappingIterator<>(iterator, mapper);
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
            var longBuffer = new SpinedBuffer.OfLong();
            mapper.accept(iterator.next(), longBuffer);
            final var nextItemIterator = longBuffer.iterator();
            if (nextItemIterator.hasNext()) {
                itemIterator = nextItemIterator;
                return true;
            }
        }
        return true;
    }
}
