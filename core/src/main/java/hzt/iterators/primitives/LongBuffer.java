package hzt.iterators.primitives;

import hzt.iterables.primitives.LongIterable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.PrimitiveIterator;

final class LongBuffer implements LongIterable {

    private int size = 0;
    private long[] elementData;

    LongBuffer(int initCapacity) {
        elementData = new long[initCapacity];
    }

    LongBuffer(LongBuffer longBuffer) {
        size = longBuffer.size;
        final var otherArray = longBuffer.elementData;
        this.elementData = Arrays.copyOf(otherArray, otherArray.length);
    }

    LongBuffer() {
        elementData = new long[]{};
    }

    boolean add(long l) {
        if (size == elementData.length) {
            final var isInitEmptyArray = elementData.length == 0;
            elementData = PrimitiveArraysSupport.growArray(elementData, size, isInitEmptyArray);
        }
        elementData[size] = l;
        size++;
        return true;
    }

    void clear() {
        size = 0;
        elementData = new long[PrimitiveArraysSupport.DEFAULT_CAPACITY];
    }

    boolean isEmpty() {
        return size == 0;
    }

    int size() {
        return size;
    }

    long remove(int index) {
        Objects.checkIndex(index, size);
        long oldValue = elementData[index];
        size = PrimitiveArraysSupport.fastRemoveLong(elementData, size, index);
        return oldValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LongBuffer longs = (LongBuffer) o;
        return size == longs.size && Arrays.equals(elementData, longs.elementData);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(elementData);
        return result;
    }

    @Override
    public @NotNull PrimitiveIterator.OfLong iterator() {
        return new PrimitiveIterator.OfLong() {
            private int counter = 0;

            @Override
            public boolean hasNext() {
                return counter < size;
            }

            @Override
            public long nextLong() {
                return elementData[counter++];
            }
        };
    }
}
