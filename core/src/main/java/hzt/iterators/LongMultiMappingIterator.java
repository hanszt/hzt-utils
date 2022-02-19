package hzt.iterators;

import hzt.iterables.primitives.LongIterable;
import hzt.sequences.primitives.LongSequence;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public final class LongMultiMappingIterator implements PrimitiveIterator.OfLong {

    private final PrimitiveIterator.OfLong iterator;
    private final LongSequence.LongMapMultiConsumer mapper;

    private PrimitiveIterator.OfLong itemIterator = null;

    private LongMultiMappingIterator(@NotNull OfLong iterator, @NotNull LongSequence.LongMapMultiConsumer mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    public static LongMultiMappingIterator of(@NotNull OfLong iterator, @NotNull LongSequence.LongMapMultiConsumer mapper) {
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
            } else {
                LongBuffer longBuffer = new LongBuffer();
                mapper.accept(iterator.nextLong(), longBuffer::add);
                final OfLong nextItemIterator = longBuffer.iterator();
                if (nextItemIterator.hasNext()) {
                    itemIterator = nextItemIterator;
                    return true;
                }
            }
        }
        return true;
    }

    private static final class LongBuffer implements LongIterable {
        private int index = 0;
        private long[] array = new long[10];

        private void add(long l) {
            if (index >= array.length) {
                array = Arrays.copyOf(array, array.length + 10);
            }
            array[index] = l;
            index++;
        }

        @Override
        public OfLong iterator() {
            return new PrimitiveIterator.OfLong() {
                int counter = 0;

                @Override
                public boolean hasNext() {
                    return counter < index;
                }

                @Override
                public long nextLong() {
                    return array[counter++];
                }
            };
        }
    }
}
