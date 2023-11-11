package org.hzt.utils.collections.primitives;

import org.hzt.utils.arrays.ArraysX;
import org.hzt.utils.iterables.IterableXHelper;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;

import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

final class IntImmutableList extends
        PrimitiveAbstractCollection<Integer, IntConsumer, int[], PrimitiveIterator.OfInt> implements IntList {

    private final int[] elementData;

    IntImmutableList() {
        super(0);
        elementData = new int[0];
    }

    IntImmutableList(final int... array) {
        super(array.length);
        elementData = ArraysX.copyOf(array);
    }

    IntImmutableList(final IntCollection collection) {
        super(collection.size());
        elementData = ArraysX.copyOf(collection.toArray());
    }

    @Override
    public int get(final int index) {
        Objects.checkIndex(index, elementData.length);
        return elementData[index];
    }

    public int indexOf(final int value) {
        return indexOfRange(value, elementData.length);
    }

    int indexOfRange(final int value, final int end) {
        for (var i = 0; i < end; i++) {
            if (value == elementData[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(final int value) {
        return lastIndexOfRange(value, elementData.length);
    }

    @Override
    public OptionalInt findRandom() {
        return isNotEmpty() ? OptionalInt.of(get(IterableXHelper.RANDOM.nextInt(size()))) : OptionalInt.empty();
    }

    @Override
    public IntList shuffled() {
        final var mutableList = IntMutableList.of(this);
        PrimitiveListHelper.shuffle(mutableList);
        return mutableList;
    }

    private int lastIndexOfRange(final int value, final int end) {
        for (var i = end - 1; i >= 0; i--) {
            if (value == elementData[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    @SuppressWarnings("squid:S2162")
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof IntList)) {
            return false;
        }

        final var iterator1 = iterator();
        final var iterator2 = ((IntList) o).iterator();
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
        final var result = Objects.hash(elementData.length);
        return 31 * result + Arrays.hashCode(elementData);
    }

    @Override
    protected void appendNextPrimitive(final StringBuilder sb, final PrimitiveIterator.OfInt iterator) {
        sb.append(iterator.nextInt());
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
    public int[] toArray() {
        return ArraysX.copyOf(elementData);
    }

    @Override
    public IntMutableList toMutableList() {
        return new IntArrayList(elementData);
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
                return index < elementData.length;
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
