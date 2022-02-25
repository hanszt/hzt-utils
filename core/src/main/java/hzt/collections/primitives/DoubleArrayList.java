package hzt.collections.primitives;

import hzt.iterables.primitives.DoubleIterable;
import hzt.iterators.primitives.PrimitiveListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.DoubleConsumer;

final class DoubleArrayList extends PrimitiveAbstractCollection<Double, DoubleConsumer, PrimitiveIterator.OfDouble>
        implements DoubleMutableListX {

    private int size = 0;
    private double[] elementData;

    DoubleArrayList(int initCapacity) {
        elementData = new double[initCapacity];
    }

    DoubleArrayList(DoubleListX doubleListX) {
        size = doubleListX.size();
        this.elementData = doubleListX.toArray();
    }

    DoubleArrayList() {
        elementData = new double[PrimitiveListHelper.DEFAULT_CAPACITY];
    }

    DoubleArrayList(Iterable<Double> iterable) {
        this();
        if (iterable instanceof DoubleIterable) {
            final var iterator = ((DoubleIterable) iterable).iterator();
            while (iterator.hasNext()) {
                add(iterator.nextDouble());
            }
            return;
        }
        for (double aDouble : iterable) {
            add(aDouble);
        }
    }

    DoubleArrayList(double... array) {
        size = array.length;
        elementData = Arrays.copyOf(array, size);
    }

    @Override
    public boolean add(double value) {
        if (size == elementData.length) {
            final var isInitEmptyArray = elementData.length == 0;
            elementData = PrimitiveListHelper.growArray(elementData, size, isInitEmptyArray);
        }
        elementData[size] = value;
        size++;
        return true;
    }

    public void clear() {
        size = 0;
        elementData = new double[PrimitiveListHelper.DEFAULT_CAPACITY];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public double get(int index) {
        return elementData[index];
    }

    public int indexOf(double d) {
        return indexOfRange(d, 0, size);
    }

    int indexOfRange(double o, int start, int end) {
        for (int i = start; i < end; i++) {
            final var elementDatum = elementData[i];
            final var compare = Double.compare(o, elementDatum);
            if (compare == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public double removeAt(int index) {
        Objects.checkIndex(index, size);
        double oldValue = elementData[index];
        size = PrimitiveListHelper.fastRemoveDouble(elementData, size, index);
        return oldValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DoubleArrayList)) {
            return false;
        }

        PrimitiveIterator.OfDouble iterator1 = iterator();
        PrimitiveIterator.OfDouble iterator2 = ((DoubleListX) o).iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            double l1 = iterator1.nextDouble();
            double l2 = iterator2.nextDouble();
            if (Double.compare(l1, l2) != 0) {
                return false;
            }
        }
        return !(iterator1.hasNext() || iterator2.hasNext());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(elementData);
        return result;
    }

    @Override
    public double[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    @Override
    public @NotNull PrimitiveIterator.OfDouble iterator() {
        return listIterator();
    }

    @Override
    public long set(int index, long value) {
        elementData[index] = value;
        return value;
    }

    @Override
    public @NotNull PrimitiveListIterator.OfDouble listIterator() {
        return listIterator(0);
    }

    @Override
    public @NotNull PrimitiveListIterator.OfDouble listIterator(int startIndex) {
        return new PrimitiveListIterator.OfDouble() {
            private int index = startIndex;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public double nextDouble() {
                return elementData[index++];
            }

            @Override
            public boolean hasPrevious() {
                return index > 0;
            }

            @Override
            public double previousDouble() {
                return elementData[--index];
            }

            @Override
            public int nextIndex() {
                return index + 1;
            }

            @Override
            public int previousIndex() {
                return index - 1;
            }
        };
    }
}
