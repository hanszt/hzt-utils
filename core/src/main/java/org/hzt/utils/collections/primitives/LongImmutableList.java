package org.hzt.utils.collections.primitives;

import org.hzt.utils.PreConditions;
import org.hzt.utils.arrays.ArraysX;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;

import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalLong;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.function.LongConsumer;

final class LongImmutableList extends
        PrimitiveAbstractCollection<Long, LongConsumer, long[], PrimitiveIterator.OfLong> implements LongList {

    private final long[] elementData;

    LongImmutableList() {
        super(0);
        elementData = new long[0];
    }

    LongImmutableList(final long... array) {
        super(array.length);
        elementData = ArraysX.copyOf(array);
    }

    LongImmutableList(final LongCollection collection) {
        super(collection.size());
        elementData = ArraysX.copyOf(collection.toArray());
    }

    @Override
    public long get(final int index) {
        PreConditions.requireOrThrow(index < elementData.length, IndexOutOfBoundsException::new);
        return elementData[index];
    }

    public int indexOf(final long value) {
        return indexOfRange(value, elementData.length);
    }

    int indexOfRange(final long value, final int end) {
        for (int i = 0; i < end; i++) {
            if (value == elementData[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(final long value) {
        return lastIndexOfRange(value, elementData.length);
    }

    @Override
    public OptionalLong findRandom(final Random random) {
        return isNotEmpty() ? OptionalLong.of(get(random.nextInt(size()))) : OptionalLong.empty();
    }

    @Override
    public LongList shuffled(final Random random) {
        final LongMutableList mutableList = LongMutableList.of(this);
        PrimitiveListHelper.shuffle(mutableList, random);
        return mutableList;
    }

    private int lastIndexOfRange(final long value, final int end) {
        for (int i = end - 1; i >= 0; i--) {
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
        if (!(o instanceof LongList)) {
            return false;
        }

        final PrimitiveIterator.OfLong iterator1 = iterator();
        final PrimitiveIterator.OfLong iterator2 = ((LongList) o).iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            final long l1 = iterator1.nextLong();
            final long l2 = iterator2.nextLong();
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
    protected void appendNextPrimitive(final StringBuilder sb, final PrimitiveIterator.OfLong iterator) {
        sb.append(iterator.nextLong());
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
    public long[] toArray() {
        return ArraysX.copyOf(elementData);
    }

    @Override
    public LongMutableList toMutableList() {
        return new LongArrayList(elementData);
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
