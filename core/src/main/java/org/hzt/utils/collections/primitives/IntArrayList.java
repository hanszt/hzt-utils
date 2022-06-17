package org.hzt.utils.collections.primitives;

import org.hzt.utils.arrays.primitves.PrimitiveArrays;
import org.hzt.utils.iterables.IterableXHelper;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.PrimitiveIterator;

final class IntArrayList extends PrimitiveAbstractCollection<Integer> implements IntMutableListX {

    private int size = 0;
    private int[] elementData;

    IntArrayList(int initCapacity) {
        elementData = new int[initCapacity];
    }

    IntArrayList(@NotNull IntListX intListX) {
        size = intListX.size();
        this.elementData = intListX.toArray();
    }

    IntArrayList() {
        elementData = new int[PrimitiveListHelper.DEFAULT_CAPACITY];
    }

    IntArrayList(int @NotNull ... array) {
        size = array.length;
        elementData = Arrays.copyOf(array, size);
    }

    IntArrayList(@NotNull Iterable<Integer> iterable) {
        this();
        if (iterable instanceof PrimitiveIterable.OfInt) {
            final var iterator = ((PrimitiveIterable.OfInt) iterable).iterator();
            while (iterator.hasNext()) {
                add(iterator.nextInt());
            }
            return;
        }
        for (int aInt : iterable) {
            add(aInt);
        }
    }

    public boolean add(int l) {
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
        elementData = new int[PrimitiveListHelper.DEFAULT_CAPACITY];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int get(int index) {
        Objects.checkIndex(index, size);
        return elementData[index];
    }

    public int indexOf(int o) {
        return indexOfRange(o, size);
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
    public int lastIndexOf(int i) {
        return lastIndexOfRange(i, size);
    }

    @Override
    public OptionalInt findRandom() {
        return isNotEmpty() ? OptionalInt.of(get(IterableXHelper.RANDOM.nextInt(size()))) : OptionalInt.empty();
    }

    @Override
    public IntListX shuffled() {
        final var mutableListX = IntMutableListX.of(this);
        PrimitiveListHelper.shuffle(mutableListX);
        return mutableListX;
    }

    private int lastIndexOfRange(int value, int end) {
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

    public int removeAt(int index) {
        Objects.checkIndex(index, size);
        int oldValue = elementData[index];
        size = PrimitiveListHelper.fastRemoveInt(elementData, size, index);
        return oldValue;
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
        PrimitiveIterator.OfInt iterator2 = ((IntListX) o).iterator();
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
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(elementData);
        return result;
    }

    @Override
    public int[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    @Override
    public PrimitiveIterator.@NotNull OfInt iterator() {
        return listIterator();
    }

    @Override
    public int set(int index, int value) {
        Objects.checkIndex(index, size);
        elementData[index] = value;
        return value;
    }

    @Override
    public IntMutableListX toMutableList() {
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
        PrimitiveArrays.sort(elementData, 0, size, comparator);
    }

    @Override
    public void sort() {
        Arrays.sort(elementData, 0, size);
    }
}
