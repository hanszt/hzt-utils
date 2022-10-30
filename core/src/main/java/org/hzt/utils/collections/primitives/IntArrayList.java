package org.hzt.utils.collections.primitives;

import org.hzt.utils.PreConditions;
import org.hzt.utils.arrays.ArraysX;
import org.hzt.utils.iterables.IterableXHelper;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

final class IntArrayList extends PrimitiveAbstractList<Integer, IntConsumer, int[], PrimitiveIterator.OfInt>
        implements IntMutableList {

    IntArrayList() {
        super(0, new int[DEFAULT_CAPACITY]);
    }

    IntArrayList(int initCapacity) {
        super(0, new int[initCapacity]);
    }

    IntArrayList(@NotNull IntList intList) {
        super(intList.size(), intList.toArray());
    }

    IntArrayList(int @NotNull ... array) {
        super(array.length, Arrays.copyOf(array, array.length));
    }

    IntArrayList(@NotNull Iterable<Integer> iterable) {
        this();
        if (iterable instanceof PrimitiveIterable.OfInt) {
            final PrimitiveIterator.OfInt iterator = ((PrimitiveIterable.OfInt) iterable).iterator();
            while (iterator.hasNext()) {
                add(iterator.nextInt());
            }
            return;
        }
        for (int value : iterable) {
            add(value);
        }
    }

    public boolean add(int value) {
        if (size == elementData.length) {
            final boolean isInitEmptyArray = elementData.length == 0;
            elementData = growArray(size, isInitEmptyArray);
        }
        elementData[size] = value;
        size++;
        return true;
    }

    @Override
    public int get(int index) {
        PreConditions.requireOrThrow(index < size, IndexOutOfBoundsException::new);
        return elementData[index];
    }

    public int indexOf(int value) {
        return indexOfRange(value, size);
    }

    private int indexOfRange(int value, int end) {
        for (int i = 0; i < end; i++) {
            if (value == elementData[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(int value) {
        return lastIndexOfRange(value, size);
    }

    @Override
    public OptionalInt findRandom() {
        return isNotEmpty() ? OptionalInt.of(get(IterableXHelper.RANDOM.nextInt(size()))) : OptionalInt.empty();
    }

    @Override
    public IntList shuffled() {
        final IntMutableList mutableList = IntMutableList.of(this);
        PrimitiveListHelper.shuffle(mutableList);
        return mutableList;
    }

    private int lastIndexOfRange(int value, int end) {
        for (int i = end - 1; i >= 0; i--) {
            if (value == elementData[i]) {
                return i;
            }
        }
        return -1;
    }

    public int removeAt(int index) {
        int oldValue = elementData[PrimitiveListHelper.checkIndex(index, size)];
        size = fastRemoveInt(elementData, size, index);
        return oldValue;
    }

    static int fastRemoveInt(int[] array, int size, int index) {
        final int newSize = size - 1;
        if (newSize > index) {
            System.arraycopy(array, index + 1, array, index, newSize - index);
        }
        array[newSize] = 0;
        return newSize;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof IntArrayList)) {
            return false;
        }

        PrimitiveIterator.OfInt iterator1 = iterator();
        PrimitiveIterator.OfInt iterator2 = ((IntList) o).iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            int l1 = iterator1.nextInt();
            int l2 = iterator2.nextInt();
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
    public PrimitiveIterator.@NotNull OfInt iterator() {
        return listIterator();
    }

    @Override
    protected int[] newArray(int length) {
        return new int[length];
    }

    @Override
    protected int[] copyElementData(int newLength) {
        return Arrays.copyOf(elementData, newLength);
    }

    @Override
    public int set(int index, int value) {
        PreConditions.requireOrThrow(index < size, IndexOutOfBoundsException::new);
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
    public PrimitiveListIterator.OfInt listIterator(int startIndex) {
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
    public void sort(IntComparator comparator) {
        ArraysX.sort(0, size, comparator, elementData);
    }

    @Override
    public void sort() {
        Arrays.sort(elementData, 0, size);
    }

    @Override
    protected void appendNextPrimitive(StringBuilder sb, PrimitiveIterator.OfInt iterator) {
        sb.append(iterator.nextInt());
    }
}
