package org.hzt.utils.collections.primitives;

import org.hzt.utils.PreConditions;
import org.hzt.utils.arrays.ArraysX;
import org.hzt.utils.iterables.IterableXHelper;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.hzt.utils.sequences.primitives.DoubleSequence;

import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.PrimitiveIterator;
import java.util.function.DoubleConsumer;

final class DoubleArrayList extends PrimitiveAbstractArrayList<Double, DoubleConsumer, double[], PrimitiveIterator.OfDouble>
        implements DoubleMutableList {

    DoubleArrayList() {
        super(0, new double[DEFAULT_CAPACITY]);
    }

    DoubleArrayList(int initCapacity) {
        super(0, new double[initCapacity]);
    }

    DoubleArrayList(DoubleList doubleList) {
        super(doubleList.size(), doubleList.toArray());
    }

    DoubleArrayList(double... array) {
        super(array.length, Arrays.copyOf(array, array.length));
    }

    DoubleArrayList(Iterable<Double> iterable) {
        this();
        if (iterable instanceof PrimitiveIterable.OfDouble) {
            final PrimitiveIterator.OfDouble iterator = ((PrimitiveIterable.OfDouble) iterable).iterator();
            while (iterator.hasNext()) {
                add(iterator.nextDouble());
            }
            return;
        }
        for (double value : iterable) {
            add(value);
        }
    }

    public boolean add(double value) {
        if (size == elementData.length) {
            final boolean isInitEmptyArray = elementData.length == 0;
            elementData = growArray(size, isInitEmptyArray);
        }
        elementData[size] = value;
        size++;
        return true;
    }

    @Override
    public boolean add(int index, double value) {
        PreConditions.requireOrThrow(index >= 0 && index <= size, IndexOutOfBoundsException::new);
        if (size == elementData.length) {
            elementData = growArray(size, elementData.length == 0);
        }
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = value;
        size++;
        return true;
    }

    @Override
    public boolean addAll(int index, PrimitiveIterable.OfDouble iterable) {
        rangeCheckForAdd(index);
        final double[] a = iterable instanceof DoubleCollection ? ((DoubleCollection) iterable).toArray() : DoubleSequence.of(iterable).toArray();
        int numNew = a.length;
        if (numNew == 0) {
            return false;
        }
        final int s = size;
        if (numNew > elementData.length - s) {
            elementData = growArray(s + numNew, false);
        }
        int numMoved = s - index;
        if (numMoved > 0) {
            System.arraycopy(elementData, index, elementData, index + numNew, numMoved);
        }
        System.arraycopy(a, 0, elementData, index, numNew);
        size = s + numNew;
        return true;
    }

    @Override
    public double get(int index) {
        PreConditions.requireOrThrow(index < size, IndexOutOfBoundsException::new);
        return elementData[index];
    }

    public int indexOf(double value) {
        return indexOfRange(value, size);
    }

    int indexOfRange(double value, int end) {
        for (int i = 0; i < end; i++) {
            if (Double.compare(value, elementData[i]) == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(double value) {
        return lastIndexOfRange(value, size);
    }

    @Override
    public OptionalDouble findRandom() {
        return isNotEmpty() ? OptionalDouble.of(get(IterableXHelper.RANDOM.nextInt(size()))) : OptionalDouble.empty();
    }

    @Override
    public DoubleList shuffled() {
        final DoubleMutableList mutableList = DoubleMutableList.of(this);
        PrimitiveListHelper.shuffle(mutableList);
        return mutableList;
    }

    private int lastIndexOfRange(double value, int end) {
        for (int i = end - 1; i >= 0; i--) {
            if (Double.compare(value, elementData[i]) == 0) {
                return i;
            }
        }
        return -1;
    }

    public double removeAt(int index) {
        double oldValue = elementData[PrimitiveListHelper.checkIndex(index, size)];
        size = fastRemoveDouble(elementData, size, index);
        return oldValue;
    }

    private static int fastRemoveDouble(double[] array, int size, int index) {
        final int newSize = size - 1;
        if (newSize > index) {
            System.arraycopy(array, index + 1, array, index, newSize - index);
        }
        array[newSize] = 0.0;
        return newSize;
    }

    @Override
    @SuppressWarnings("squid:S2162")
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DoubleList)) {
            return false;
        }

        PrimitiveIterator.OfDouble iterator1 = iterator();
        PrimitiveIterator.OfDouble iterator2 = ((DoubleList) o).iterator();
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
        final int result = Objects.hash(size);
        return  31 * result + Arrays.hashCode(elementData);
    }

    @Override
    public PrimitiveIterator.OfDouble iterator() {
        return listIterator();
    }

    @Override
    protected double[] newArray(int length) {
        return new double[length];
    }

    @Override
    protected double[] copyElementData(int newLength) {
        return Arrays.copyOf(elementData, newLength);
    }

    @Override
    public double set(int index, double value) {
        PreConditions.requireOrThrow(index < size, IndexOutOfBoundsException::new);
        elementData[index] = value;
        return value;
    }

    @Override
    public DoubleMutableList toMutableList() {
        return this;
    }

    @Override
    public PrimitiveListIterator.OfDouble listIterator() {
        return listIterator(0);
    }

    @Override
    @SuppressWarnings("squid:S1188")
    public PrimitiveListIterator.OfDouble listIterator(int startIndex) {
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

    @Override
    public void sort(DoubleComparator comparator) {
        ArraysX.sort(0, size, comparator, elementData);
    }

    @Override
    public void sort() {
        Arrays.sort(elementData, 0, size);
    }

    @Override
    protected void appendNextPrimitive(StringBuilder sb, PrimitiveIterator.OfDouble iterator) {
        sb.append(iterator.nextDouble());
    }
}
