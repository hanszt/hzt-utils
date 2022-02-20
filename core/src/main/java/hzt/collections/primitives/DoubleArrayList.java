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
        elementData = new double[]{};
    }

    DoubleArrayList(Iterable<Double> iterable) {
        if (iterable instanceof DoubleIterable) {
            final var iterator = ((DoubleIterable) iterable).iterator();
            while (iterator.hasNext()) {
                add(iterator().nextDouble());
            }
            return;
        }
        for (double aDouble : iterable) {
            add(aDouble);
        }
    }

    public DoubleArrayList(double... array) {
        elementData = Arrays.copyOf(array, array.length);
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

    public int indexOf(double d) {
        return indexOfRange(d, 0, size);
    }

    int indexOfRange(double o, int start, int end) {
        for (int i = start; i < end; i++) {
            if (o == elementData[i]) {
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
            if (l1 != l2) {
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
                return false;
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
