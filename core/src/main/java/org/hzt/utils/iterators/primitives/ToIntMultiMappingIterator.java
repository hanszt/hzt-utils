package org.hzt.utils.iterators.primitives;

import org.hzt.utils.spined_buffers.SpinedBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

public final class ToIntMultiMappingIterator<T> implements PrimitiveIterator.OfInt {

    private final Iterator<T> iterator;
    private final BiConsumer<? super T, IntConsumer> mapper;

    private OfInt itemIterator = null;

    private ToIntMultiMappingIterator(@NotNull Iterator<T> iterator,
                                      @NotNull BiConsumer<? super T, IntConsumer> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    public static <T> ToIntMultiMappingIterator<T> of(@NotNull Iterator<T> iterator,
                                                      @NotNull BiConsumer<? super T, IntConsumer> mapper) {
        return new ToIntMultiMappingIterator<>(iterator, mapper);
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
                SpinedBuffer.OfInt intBuffer = new SpinedBuffer.OfInt();
                mapper.accept(iterator.next(), intBuffer);
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
