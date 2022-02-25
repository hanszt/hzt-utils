package hzt.collections.primitives;

import hzt.iterables.primitives.LongIterable;
import hzt.iterators.primitives.PrimitiveListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.LongConsumer;

public final class LongArrayList extends PrimitiveAbstractCollection<Long, LongConsumer, PrimitiveIterator.OfLong>
        implements LongMutableListX {

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
        if (iterable instanceof LongIterable) {
            final PrimitiveIterator.OfLong iterator = ((LongIterable) iterable).iterator();
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
        return elementData[index];
    }

    @Override
    public long set(int index, long value) {
        elementData[index] = value;
        return value;
    }

    @Override
    public int indexOf(long l) {
        return indexOfRange(l, 0, size);
    }

    int indexOfRange(long l, int start, int end) {
        for (int i = start; i < end; i++) {
            if (l == elementData[i]) {
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
        PrimitiveListHelper.checkIndex(index, size);
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
}
