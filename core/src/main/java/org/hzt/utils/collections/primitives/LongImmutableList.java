package org.hzt.utils.collections.primitives;

import org.hzt.utils.arrays.ArraysX;
import org.hzt.utils.iterables.IterableXHelper;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalLong;
import java.util.PrimitiveIterator;
import java.util.function.LongConsumer;

final class LongImmutableList extends
        PrimitiveAbstractCollection<Long, LongConsumer, long[], PrimitiveIterator.OfLong> implements LongList {

    private final long[] elementData;

    LongImmutableList() {
        super(0);
        elementData = new long[0];
    }

    LongImmutableList(long @NotNull... array) {
        super(array.length);
        elementData = ArraysX.copyOf(array);
    }

    LongImmutableList(LongCollection collection) {
        super(collection.size());
        elementData = ArraysX.copyOf(collection.toArray());
    }

    @Override
    public long get(int index) {
        Objects.checkIndex(index, elementData.length);
        return elementData[index];
    }

    public int indexOf(long value) {
        return indexOfRange(value, elementData.length);
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
        return lastIndexOfRange(value, elementData.length);
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
        final int result = Objects.hash(elementData.length);
        return  31 * result + Arrays.hashCode(elementData);
    }

    @Override
    protected void appendNextPrimitive(StringBuilder sb, PrimitiveIterator.OfLong iterator) {
        sb.append(iterator.nextLong());
    }

    @Override
    public @NotNull PrimitiveIterator.OfLong iterator() {
        return listIterator();
    }

    @Override
    protected long[] newArray(int length) {
        return new long[length];
    }


    @Override
    public long[] toArray() {
        return ArraysX.copyOf(elementData);
    }

    @Override
    public LongMutableList toMutableList() {
        return new LongArrayList(elementData);
    }

    @Override
    public @NotNull PrimitiveListIterator.OfLong listIterator() {
        return listIterator(0);
    }

    @Override
    @SuppressWarnings("squid:S1188")
    public @NotNull PrimitiveListIterator.OfLong listIterator(int startIndex) {
        return new PrimitiveListIterator.OfLong() {
            private int index = startIndex;

            @Override
            public boolean hasNext() {
                return index < elementData.length;
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
