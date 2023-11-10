package org.hzt.utils.collections.primitives;

import org.hzt.utils.PreConditions;
import org.hzt.utils.arrays.ArraysX;
import org.hzt.utils.iterables.IterableXHelper;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.primitive_comparators.LongComparator;
import org.hzt.utils.sequences.primitives.LongSequence;

import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalLong;
import java.util.PrimitiveIterator;
import java.util.function.LongConsumer;

public final class LongArrayList extends PrimitiveAbstractArrayList<Long, LongConsumer, long[], PrimitiveIterator.OfLong>
        implements LongMutableList {

    LongArrayList() {
        super(0, new long[DEFAULT_CAPACITY]);
    }

    LongArrayList(int initCapacity) {
        super(0, new long[initCapacity]);
    }

    LongArrayList(LongList longList) {
        super(longList.size(), longList.toArray());
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
        for (long value : iterable) {
            add(value);
        }
    }

    public boolean add(long value) {
        if (size == elementData.length) {
            final boolean isInitEmptyArray = elementData.length == 0;
            elementData = growArray(size, isInitEmptyArray);
        }
        elementData[size] = value;
        size++;
        return true;
    }

    @Override
    public boolean add(int index, long value) {
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
    public boolean addAll(int index, PrimitiveIterable.OfLong iterable) {
        rangeCheckForAdd(index);
        final long[] a = iterable instanceof LongCollection ? ((LongCollection) iterable).toArray() : LongSequence.of(iterable).toArray();
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
    public long get(int index) {
        PreConditions.requireOrThrow(index < size, IndexOutOfBoundsException::new);
        return elementData[index];
    }

    public int indexOf(long value) {
        return indexOfRange(value, size);
    }

    int indexOfRange(long value, int end) {
        for (int i = 0; i < end; i++) {
            if (value == elementData[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(long value) {
        return lastIndexOfRange(value, size);
    }

    @Override
    public OptionalLong findRandom() {
        return isNotEmpty() ? OptionalLong.of(get(IterableXHelper.RANDOM.nextInt(size()))) : OptionalLong.empty();
    }

    @Override
    public LongList shuffled() {
        final LongMutableList mutableList = LongMutableList.of(this);
        PrimitiveListHelper.shuffle(mutableList);
        return mutableList;
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
        long oldValue = elementData[PrimitiveListHelper.checkIndex(index, size)];
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
    @SuppressWarnings("squid:S2162")
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LongList)) {
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
    public PrimitiveIterator.OfLong iterator() {
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
    public long set(int index, long value) {
        PreConditions.requireOrThrow(index < size, IndexOutOfBoundsException::new);
        elementData[index] = value;
        return value;
    }

    @Override
    public LongMutableList toMutableList() {
        return this;
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
    protected void appendNextPrimitive(StringBuilder sb, PrimitiveIterator.OfLong iterator) {
        sb.append(iterator.nextLong());
    }
}
