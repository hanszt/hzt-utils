package org.hzt.utils.collections.primitives;

import org.hzt.utils.arrays.ArraysX;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.primitive_comparators.LongComparator;
import org.hzt.utils.sequences.primitives.LongSequence;

import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalLong;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.function.LongConsumer;

import static java.util.Objects.checkIndex;

public final class LongArrayList extends PrimitiveAbstractArrayList<Long, LongConsumer, long[], PrimitiveIterator.OfLong>
        implements LongMutableList {

    LongArrayList() {
        super(0, new long[DEFAULT_CAPACITY]);
    }

    LongArrayList(final int initCapacity) {
        super(0, new long[initCapacity]);
    }

    LongArrayList(final LongList longList) {
        super(longList.size(), longList.toArray());
    }

    LongArrayList(final long... array) {
        super(array.length, Arrays.copyOf(array, array.length));
    }

    LongArrayList(final Iterable<Long> iterable) {
        this();
        if (iterable instanceof final PrimitiveIterable.OfLong longIterable) {
            final var iterator = longIterable.iterator();
            while (iterator.hasNext()) {
                add(iterator.nextLong());
            }
            return;
        }
        for (final long value : iterable) {
            add(value);
        }
    }

    public boolean add(final long value) {
        if (size == elementData.length) {
            final var isInitEmptyArray = elementData.length == 0;
            elementData = growArray(size, isInitEmptyArray);
        }
        elementData[size] = value;
        size++;
        return true;
    }

    @Override
    public boolean add(final int index, final long value) {
        Objects.checkIndex(index, size + 1);
        if (size == elementData.length) {
            elementData = growArray(size, elementData.length == 0);
        }
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = value;
        size++;
        return true;
    }

    @Override
    public boolean addAll(final int index, final PrimitiveIterable.OfLong iterable) {
        rangeCheckForAdd(index);
        final var a = iterable instanceof final LongCollection c ? c.toArray() : LongSequence.of(iterable).toArray();
        final var numNew = a.length;
        if (numNew == 0) {
            return false;
        }
        final var s = size;
        if (numNew > elementData.length - s) {
            elementData = growArray(s + numNew, false);
        }
        final var numMoved = s - index;
        if (numMoved > 0) {
            System.arraycopy(elementData, index, elementData, index + numNew, numMoved);
        }
        System.arraycopy(a, 0, elementData, index, numNew);
        size = s + numNew;
        return true;
    }

    @Override
    public long get(final int index) {
        checkIndex(index, size);
        return elementData[index];
    }

    public int indexOf(final long value) {
        return indexOfRange(value, size);
    }

    int indexOfRange(final long value, final int end) {
        for (var i = 0; i < end; i++) {
            if (value == elementData[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(final long value) {
        return lastIndexOfRange(value, size);
    }

    @Override
    public OptionalLong findRandom(final Random random) {
        return isNotEmpty() ? OptionalLong.of(get(random.nextInt(size()))) : OptionalLong.empty();
    }

    @Override
    public LongList shuffled(final Random random) {
        final var mutableList = LongMutableList.of(this);
        PrimitiveListHelper.shuffle(mutableList, random);
        return mutableList;
    }

    private int lastIndexOfRange(final long value, final int end) {
        for (var i = end - 1; i >= 0; i--) {
            if (value == elementData[i]) {
                return i;
            }
        }
        return -1;
    }

    public long removeAt(final int index) {
        final var oldValue = elementData[checkIndex(index, size)];
        size = fastRemoveLong(elementData, size, index);
        return oldValue;
    }

    private static int fastRemoveLong(final long[] array, final int size, final int index) {
        final var newSize = size - 1;
        if (newSize > index) {
            System.arraycopy(array, index + 1, array, index, newSize - index);
        }
        array[newSize] = 0L;
        return newSize;
    }

    @Override
    @SuppressWarnings("squid:S2162")
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof final LongList longList)) {
            return false;
        }

        final var iterator1 = iterator();
        final var iterator2 = longList.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            final var l1 = iterator1.nextLong();
            final var l2 = iterator2.nextLong();
            if (l1 != l2) {
                return false;
            }
        }
        return !(iterator1.hasNext() || iterator2.hasNext());
    }

    @Override
    public int hashCode() {
        final var result = Objects.hash(size);
        return  31 * result + Arrays.hashCode(elementData);
    }

    @Override
    public PrimitiveIterator.OfLong iterator() {
        return listIterator();
    }

    @Override
    protected long[] newArray(final int length) {
        return new long[length];
    }

    @Override
    protected long[] copyElementData(final int newLength) {
        return Arrays.copyOf(elementData, newLength);
    }

    @Override
    public long set(final int index, final long value) {
        checkIndex(index, size);
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
    public PrimitiveListIterator.OfLong listIterator(final int startIndex) {
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
    public void sort(final LongComparator comparator) {
        ArraysX.sort(0, size, comparator, elementData);
    }

    @Override
    public void sort() {
        Arrays.sort(elementData, 0, size);
    }

    @Override
    protected void appendNextPrimitive(final StringBuilder sb, final PrimitiveIterator.OfLong iterator) {
        sb.append(iterator.nextLong());
    }
}
