package hzt.iterators;

import hzt.collections.MutableListX;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MultiMappingIterator<T, R> implements Iterator<R> {

    private final Iterator<T> iterator;
    private final BiConsumer<? super T, ? super Consumer<R>> mapper;

    private Iterator<R> itemIterator = null;

    public MultiMappingIterator(Iterator<T> iterator, BiConsumer<? super T, ? super Consumer<R>> mapper) {
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
            } else {
                MutableListX<R> list = MutableListX.empty();
                mapper.accept(iterator.next(), (Consumer<R>) list::add);
                final Iterator<R> nextItemIterator = list.iterator();
                if (nextItemIterator.hasNext()) {
                    itemIterator = nextItemIterator;
                    return true;
                }
            }
        }
        return true;
    }
}
