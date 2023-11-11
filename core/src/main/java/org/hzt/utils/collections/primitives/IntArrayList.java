package org.hzt.utils.collections.primitives;

import org.hzt.utils.arrays.ArraysX;
import org.hzt.utils.iterables.IterableXHelper;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.hzt.utils.sequences.primitives.IntSequence;

import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

import static java.util.Objects.checkIndex;

final class IntArrayList extends PrimitiveAbstractArrayList<Integer, IntConsumer, int[], PrimitiveIterator.OfInt>
        implements IntMutableList {

    IntArrayList() {
        super(0, new int[DEFAULT_CAPACITY]);
    }

    IntArrayList(final int initCapacity) {
        super(0, new int[initCapacity]);
    }

    IntArrayList(final IntList intList) {
        super(intList.size(), intList.toArray());
    }

    IntArrayList(final int... array) {
        super(array.length, Arrays.copyOf(array, array.length));
    }

    IntArrayList(final Iterable<Integer> iterable) {
        this();
        if (iterable instanceof final PrimitiveIterable.OfInt intIterable) {
            final var iterator = intIterable.iterator();
            while (iterator.hasNext()) {
                add(iterator.nextInt());
            }
            return;
        }
        for (final int value : iterable) {
            add(value);
        }
    }

    public boolean add(final int value) {
        if (size == elementData.length) {
            final var isInitEmptyArray = elementData.length == 0;
            elementData = growArray(size, isInitEmptyArray);
        }
        elementData[size] = value;
        size++;
        return true;
    }

    @Override
    public boolean add(final int index, final int value) {
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
    public boolean addAll(final int index, final PrimitiveIterable.OfInt iterable) {
        rangeCheckForAdd(index);
        final var a = iterable instanceof final IntCollection c ? c.toArray() : IntSequence.of(iterable).toArray();
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
    public int get(final int index) {
        checkIndex(index, size);
        return elementData[index];
    }

    public int indexOf(final int value) {
        return indexOfRange(value, size);
    }

    private int indexOfRange(final int value, final int end) {
        for (var i = 0; i < end; i++) {
            if (value == elementData[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(final int value) {
        return lastIndexOfRange(value, size);
    }

    @Override
    public OptionalInt findRandom() {
        return isNotEmpty() ? OptionalInt.of(get(IterableXHelper.RANDOM.nextInt(size()))) : OptionalInt.empty();
    }

    @Override
    public IntList shuffled() {
        final var mutableListX = IntMutableList.of(this);
        PrimitiveListHelper.shuffle(mutableListX);
        return mutableListX;
    }

    private int lastIndexOfRange(final int value, final int end) {
        for (var i = end - 1; i >= 0; i--) {
            if (value == elementData[i]) {
                return i;
            }
        }
        return -1;
    }

    public int removeAt(final int index) {
        final var oldValue = elementData[checkIndex(index, size)];
        size = fastRemoveInt(elementData, size, index);
        return oldValue;
    }

    static int fastRemoveInt(final int[] array, final int size, final int index) {
        final var newSize = size - 1;
        if (newSize > index) {
            System.arraycopy(array, index + 1, array, index, newSize - index);
        }
        array[newSize] = 0;
        return newSize;
    }

    @Override
    @SuppressWarnings("squid:S2162")
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof final IntList intList)) {
            return false;
        }

        final var iterator1 = iterator();
        final var iterator2 = intList.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            final var l1 = iterator1.nextInt();
            final var l2 = iterator2.nextInt();
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
    public PrimitiveIterator.OfInt iterator() {
        return listIterator();
    }

    @Override
    protected int[] newArray(final int length) {
        return new int[length];
    }

    @Override
    protected int[] copyElementData(final int newLength) {
        return Arrays.copyOf(elementData, newLength);
    }

    @Override
    public int set(final int index, final int value) {
        checkIndex(index, size);
        elementData[index] = value;
        return value;
    }

    @Override
    public IntMutableList toMutableList() {
        return this;
    }

    @Override
    public PrimitiveListIterator.OfInt listIterator() {
        return listIterator(0);
    }

    @Override
    @SuppressWarnings("squid:S1188")
    public PrimitiveListIterator.OfInt listIterator(final int startIndex) {
        return new PrimitiveListIterator.OfInt() {
            private int index = startIndex;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public int nextInt() {
                return elementData[index++];
            }

            @Override
            public boolean hasPrevious() {
                return index > 0;
            }

            @Override
            public int previousInt() {
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
    public void sort(final IntComparator comparator) {
        ArraysX.sort(0, size, comparator, elementData);
    }

    @Override
    public void sort() {
        Arrays.sort(elementData, 0, size);
    }

    @Override
    protected void appendNextPrimitive(final StringBuilder sb, final PrimitiveIterator.OfInt iterator) {
        sb.append(iterator.nextInt());
    }
}
