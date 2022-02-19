package hzt.iterators.primitives;

import hzt.iterables.primitives.IntIterable;
import hzt.sequences.primitives.IntSequence;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
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
                IntBuffer intBuffer = new IntBuffer();
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

    private static final class IntBuffer implements IntIterable {
        private int index = 0;
        private int[] array = new int[10];

        private void add(int i) {
            if (index >= array.length) {
                array = Arrays.copyOf(array, array.length + 10);
            }
            array[index] = i;
            index++;
        }

        @Override
        public @NotNull OfInt iterator() {
            return new OfInt() {
                private int counter = 0;

                @Override
                public boolean hasNext() {
                    return counter < index;
                }

                @Override
                public int nextInt() {
                    return array[counter++];
                }
            };
        }
    }
}
