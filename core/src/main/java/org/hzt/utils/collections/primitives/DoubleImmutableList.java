package org.hzt.utils.collections.primitives;

import org.hzt.utils.arrays.ArraysX;
import org.hzt.utils.iterables.IterableXHelper;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.PrimitiveIterator;
import java.util.function.DoubleConsumer;

final class DoubleImmutableList extends
        PrimitiveAbstractCollection<Double, DoubleConsumer, double[], PrimitiveIterator.OfDouble> implements DoubleList {

    private final double[] elementData;

    DoubleImmutableList() {
        super(0);
        elementData = new double[0];
    }

    DoubleImmutableList(double @NotNull... array) {
        super(array.length);
        elementData = ArraysX.copyOf(array);
    }

    DoubleImmutableList(DoubleCollection collection) {
        super(collection.size());
        elementData = ArraysX.copyOf(collection.toArray());
    }

    @Override
    public double get(int index) {
        Objects.checkIndex(index, elementData.length);
        return elementData[index];
    }

    public int indexOf(double value) {
        return indexOfRange(value, elementData.length);
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
        return lastIndexOfRange(value, elementData.length);
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
        final int result = Objects.hash(elementData.length);
        return  31 * result + Arrays.hashCode(elementData);
    }

    @Override
    protected void appendNextPrimitive(StringBuilder sb, PrimitiveIterator.OfDouble iterator) {
        sb.append(iterator.nextDouble());
    }

    @Override
    public @NotNull PrimitiveIterator.OfDouble iterator() {
        return listIterator();
    }

    @Override
    protected double[] newArray(int length) {
        return new double[length];
    }


    @Override
    public double[] toArray() {
        return ArraysX.copyOf(elementData);
    }

    @Override
    public DoubleMutableList toMutableList() {
        return new DoubleArrayList(elementData);
    }

    @Override
    public @NotNull PrimitiveListIterator.OfDouble listIterator() {
        return listIterator(0);
    }

    @Override
    @SuppressWarnings("squid:S1188")
    public @NotNull PrimitiveListIterator.OfDouble listIterator(int startIndex) {
        return new PrimitiveListIterator.OfDouble() {
            private int index = startIndex;

            @Override
            public boolean hasNext() {
                return index < elementData.length;
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
