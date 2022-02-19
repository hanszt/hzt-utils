package hzt.iterators.primitives;

import hzt.iterables.primitives.DoubleIterable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.PrimitiveIterator;

final class DoubleBuffer implements DoubleIterable {

    private int size = 0;
    private double[] elementData;

    DoubleBuffer(int initCapacity) {
        elementData = new double[initCapacity];
    }

    DoubleBuffer(DoubleBuffer longBuffer) {
        size = longBuffer.size;
        final var otherArray = longBuffer.elementData;
        this.elementData = Arrays.copyOf(otherArray, otherArray.length);
    }

    DoubleBuffer() {
        elementData = new double[]{};
    }

    boolean add(double value) {
        if (size == elementData.length) {
            final var isInitEmptyArray = elementData.length == 0;
            elementData = PrimitiveArraysSupport.growArray(elementData, size, isInitEmptyArray);
        }
        elementData[size] = value;
        size++;
        return true;
    }

    void clear() {
        size = 0;
        elementData = new double[PrimitiveArraysSupport.DEFAULT_CAPACITY];
    }

    boolean isEmpty() {
        return size == 0;
    }

    int size() {
        return size;
    }

    double remove(int index) {
        Objects.checkIndex(index, size);
        double oldValue = elementData[index];
        size = PrimitiveArraysSupport.fastRemoveDouble(elementData, size, index);
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
        DoubleBuffer longs = (DoubleBuffer) o;
        return size == longs.size && Arrays.equals(elementData, longs.elementData);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(elementData);
        return result;
    }

    @Override
    public @NotNull PrimitiveIterator.OfDouble iterator() {
        return new PrimitiveIterator.OfDouble() {
            private int counter = 0;

            @Override
            public boolean hasNext() {
                return counter < size;
            }

            @Override
            public double nextDouble() {
                return elementData[counter++];
            }
        };
    }
}
