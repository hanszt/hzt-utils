package org.hzt.utils.iterators;

import org.hzt.utils.spined_buffers.SpinedBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

final class MultiMappingIterator<T, R> implements Iterator<R> {

    private final Iterator<T> iterator;
    private final BiConsumer<? super T, ? super Consumer<R>> mapper;

    private Iterator<R> itemIterator = null;

    MultiMappingIterator(@NotNull Iterator<T> iterator,
                         @NotNull BiConsumer<? super T, ? super Consumer<R>> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return ensureItemIterator();
    }

    @Override
    public R next() {
        if (!ensureItemIterator()) {
            throw new NoSuchElementException();
        }
        return itemIterator.next();
    }

    private boolean ensureItemIterator() {
        if (itemIterator != null && !itemIterator.hasNext()) {
            itemIterator = null;
        }
        while (itemIterator == null) {
            if (!iterator.hasNext()) {
                return false;
            }
            var buffer = new SpinedBuffer<R>();
            mapper.accept(iterator.next(), buffer);
            final var nextItemIterator = buffer.iterator();
            if (nextItemIterator.hasNext()) {
                itemIterator = nextItemIterator;
                return true;
            }
        }
        return true;
    }
}
