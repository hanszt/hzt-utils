package org.hzt.utils.collections.primitives;

import org.hzt.utils.PreConditions;
import org.hzt.utils.arrays.ArraysX;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.primitive_comparators.LongComparator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.LongConsumer;

public final class LongArrayList extends PrimitiveAbstractList<Long, long[], LongConsumer, PrimitiveIterator.OfLong>
        implements LongMutableList {

    LongArrayList(int initCapacity) {
        super(0, new long[initCapacity]);
    }

    LongArrayList(LongList longList) {
        super(longList.size(), longList.toArray());
    }

    LongArrayList() {
        super(0, new long[DEFAULT_CAPACITY]);
    }

    LongArrayList(long... array) {
        super(array.length, Arrays.copyOf(array, array.length));
    }

    LongArrayList(Iterable<Long> iterable) {
        this();
        if (iterable instanceof PrimitiveIterable.OfLong) {
            final PrimitiveIterator.OfLong iterator = ((PrimitiveIterable.OfLong) iterable).iterator();
            while (iterator.hasNext()) {
                add(iterator.nextLong());
            }
            return;
        }
        for (long aLong : iterable) {
            add(aLong);
        }
    }

    public boolean add(long l) {
        if (size == elementData.length) {
            final boolean isInitEmptyArray = elementData.length == 0;
            elementData = growArray(size, isInitEmptyArray);
        }
        elementData[size] = l;
        size++;
        return true;
    }

    @Override
    public long get(int index) {
        PreConditions.requireOrThrow(index < size, IndexOutOfBoundsException::new);
        return elementData[index];
    }

    @Override
    public long set(int index, long value) {
        PreConditions.requireOrThrow(index < size, IndexOutOfBoundsException::new);
        elementData[index] = value;
        return value;
    }

    @Override
    public int indexOf(long l) {
        return indexOfRange(l, size);
    }

    int indexOfRange(long l, int end) {
        for (int i = 0; i < end; i++) {
            if (l == elementData[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(long l) {
        return lastIndexOfRange(l, size);
    }

    private int lastIndexOfRange(long value, int end) {
        for (int i = end - 1; i >= 0; i--) {
            if (value == elementData[i]) {
                return i;
            }
        }
        return -1;
    }

    public long removeAt(int index) {
        PrimitiveListHelper.checkIndex(index, size);
        long oldValue = elementData[index];
        size = fastRemoveLong(elementData, size, index);
        return oldValue;
    }

    private static int fastRemoveLong(long[] array, int size, int index) {
        final int newSize = size - 1;
        if (newSize > index) {
            System.arraycopy(array, index + 1, array, index, newSize - index);
        }
        array[newSize] = 0L;
        return newSize;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LongArrayList)) {
            return false;
        }

        PrimitiveIterator.OfLong iterator1 = iterator();
        PrimitiveIterator.OfLong iterator2 = ((LongList) o).iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            long l1 = iterator1.nextLong();
            long l2 = iterator2.nextLong();
            if (l1 != l2) {
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
    public PrimitiveIterator.@NotNull OfLong iterator() {
        return listIterator();
    }

    @Override
    protected long[] newArray(int length) {
        return new long[length];
    }

    @Override
    protected long[] copyElementData(int newLength) {
        return Arrays.copyOf(elementData, newLength);
    }

    @Override
    public PrimitiveListIterator.OfLong listIterator() {
        return listIterator(0);
    }

    @Override
    @SuppressWarnings("squid:S1188")
    public PrimitiveListIterator.OfLong listIterator(int startIndex) {
        return new PrimitiveListIterator.OfLong() {
            private int index = startIndex;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public long nextLong() {
                return elementData[index++];
            }
            @Override
            public boolean hasPrevious() {
                return index > 0;
            }

            @Override
            public long previousLong() {
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
    public void sort(LongComparator comparator) {
        ArraysX.sort(0, size, comparator, elementData);
    }

    @Override
    public void sort() {
        Arrays.sort(elementData, 0, size);
    }

    @Override
    public LongMutableList toMutableList() {
        return this;
    }

    @Override
    protected void appendNextPrimitive(StringBuilder sb, PrimitiveIterator.OfLong iterator) {
        sb.append(iterator.nextLong());
    }
}
