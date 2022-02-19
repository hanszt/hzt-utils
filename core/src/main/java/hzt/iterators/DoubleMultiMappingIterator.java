package hzt.iterators;

import hzt.iterables.primitives.DoubleIterable;
import hzt.sequences.primitives.DoubleSequence;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public final class DoubleMultiMappingIterator implements PrimitiveIterator.OfDouble {

    private final OfDouble iterator;
    private final DoubleSequence.DoubleMapMultiConsumer mapper;

    private OfDouble itemIterator = null;

    private DoubleMultiMappingIterator(@NotNull OfDouble iterator, @NotNull DoubleSequence.DoubleMapMultiConsumer mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    public static DoubleMultiMappingIterator of(@NotNull OfDouble iterator, @NotNull DoubleSequence.DoubleMapMultiConsumer mapper) {
        return new DoubleMultiMappingIterator(iterator, mapper);
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
            } else {
                DoubleBuffer doubleBuffer = new DoubleBuffer();
                mapper.accept(iterator.nextDouble(), doubleBuffer::add);
                final OfDouble nextItemIterator = doubleBuffer.iterator();
                if (nextItemIterator.hasNext()) {
                    itemIterator = nextItemIterator;
                    return true;
                }
            }
        }
        return true;
    }

    private static final class DoubleBuffer implements DoubleIterable {
        private int index = 0;
        private double[] array = new double[10];

        private void add(double aDouble) {
            if (index >= array.length) {
                array = Arrays.copyOf(array, array.length + 10);
            }
            array[index] = aDouble;
            index++;
        }

        @Override
        public OfDouble iterator() {
            return new OfDouble() {
                int counter = 0;

                @Override
                public boolean hasNext() {
                    return counter < index;
                }

                @Override
                public double nextDouble() {
                    return array[counter++];
                }
            };
        }
    }
}
