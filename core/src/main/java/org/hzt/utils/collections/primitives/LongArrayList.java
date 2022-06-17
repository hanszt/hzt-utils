package org.hzt.utils.collections.primitives;

import org.hzt.utils.arrays.primitves.PrimitiveArrays;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.primitive_comparators.LongComparator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.PrimitiveIterator;

public final class LongArrayList extends PrimitiveAbstractCollection<Long> implements LongMutableListX {

    private int size = 0;
    private long[] elementData;

    LongArrayList(int initCapacity) {
        elementData = new long[initCapacity];
    }

    LongArrayList(LongListX longListX) {
        size = longListX.size();
        this.elementData = longListX.toArray();
    }

    LongArrayList() {
        elementData = new long[PrimitiveListHelper.DEFAULT_CAPACITY];
    }

    LongArrayList(long... array) {
        size = array.length;
        elementData = Arrays.copyOf(array, size);
    }

    LongArrayList(Iterable<Long> iterable) {
        this();
        if (iterable instanceof PrimitiveIterable.OfLong) {
            final var iterator = ((PrimitiveIterable.OfLong) iterable).iterator();
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
            final var isInitEmptyArray = elementData.length == 0;
            elementData = PrimitiveListHelper.growArray(elementData, size, isInitEmptyArray);
        }
        elementData[size] = l;
        size++;
        return true;
    }

    public void clear() {
        size = 0;
        elementData = new long[PrimitiveListHelper.DEFAULT_CAPACITY];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public long get(int index) {
        Objects.checkIndex(index, size);
        return elementData[index];
    }

    @Override
    public long set(int index, long value) {
        Objects.checkIndex(index, size);
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

    @Override
    public int size() {
        return size;
    }

    public long removeAt(int index) {
        Objects.checkIndex(index, size);
        long oldValue = elementData[index];
        size = PrimitiveListHelper.fastRemoveLong(elementData, size, index);
        return oldValue;
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
        PrimitiveIterator.OfLong iterator2 = ((LongListX) o).iterator();
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
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(elementData);
        return result;
    }

    @Override
    public long[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    @Override
    public PrimitiveIterator.@NotNull OfLong iterator() {
        return listIterator();
    }

    @Override
    public PrimitiveListIterator.OfLong listIterator() {
        return listIterator(0);
    }

    @Override
    @SuppressWarnings("squid:S1188")
    public PrimitiveListIterator.OfLong listIterator(int startIndex) {
        return new PrimitiveListIterator.OfLong() {
            private int index = 0;

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
        PrimitiveArrays.sort(elementData, 0, size, comparator);
    }

    @Override
    public void sort() {
        Arrays.sort(elementData, 0, size);
    }

    @Override
    public LongMutableListX toMutableList() {
        return this;
    }
}
