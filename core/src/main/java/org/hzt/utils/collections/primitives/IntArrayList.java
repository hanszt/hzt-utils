package org.hzt.utils.collections.primitives;

import org.hzt.utils.iterables.primitives.IntIterable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

final class IntArrayList extends PrimitiveAbstractCollection<Integer, IntConsumer, PrimitiveIterator.OfInt>
        implements IntMutableListX {

    private int size = 0;
    private int[] elementData;

    IntArrayList(int initCapacity) {
        elementData = new int[initCapacity];
    }

    IntArrayList(IntListX intListX) {
        size = intListX.size();
        this.elementData = intListX.toArray();
    }

    IntArrayList() {
        elementData = new int[PrimitiveListHelper.DEFAULT_CAPACITY];
    }

    IntArrayList(int... array) {
        size = array.length;
        elementData = Arrays.copyOf(array, size);
    }

    IntArrayList(Iterable<Integer> iterable) {
        this();
        if (iterable instanceof IntIterable) {
            final PrimitiveIterator.OfInt iterator = ((IntIterable) iterable).iterator();
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
            final boolean isInitEmptyArray = elementData.length == 0;
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
        return elementData[index];
    }

    public int indexOf(int o) {
        return indexOfRange(o, 0, size);
    }

    private int indexOfRange(int value, int start, int end) {
        for (int i = start; i < end; i++) {
            if (value == elementData[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(int i) {
        return lastIndexOfRange(i, 0, size);
    }

    private int lastIndexOfRange(int value, int start, int end) {
        for (int i = end - 1; i >= start; i--) {
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
        PrimitiveListHelper.checkIndex(index, size);
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
        elementData[index] = value;
        return value;
    }

    @Override
    public PrimitiveListIterator.OfInt listIterator() {
        return listIterator(0);
    }

    @Override
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
}
