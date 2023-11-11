package org.hzt.fx.utils.collections;

import org.hzt.fx.utils.function.FloatConsumer;
import org.hzt.fx.utils.function.FloatPredicate;
import org.hzt.fx.utils.iterables.FloatIterable;
import org.hzt.fx.utils.iterators.FloatIterator;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.primitives.PrimitiveAbstractArrayList;
import org.hzt.utils.collections.primitives.PrimitiveMutableCollection;

import java.util.Arrays;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;

public final class FloatArrayList extends PrimitiveAbstractArrayList<Float, FloatConsumer, float[], FloatIterator>
        implements PrimitiveMutableCollection<Float, FloatConsumer, float[], FloatPredicate>, FloatIterable {

    public FloatArrayList() {
        this(0, new float[DEFAULT_CAPACITY]);
    }

    private FloatArrayList(final int initSize, final float[] elementData) {
        super(initSize, elementData);
    }

    public boolean add(final float l) {
        final var size = this.size;
        if (size == elementData.length) {
            final var isInitEmptyArray = elementData.length == 0;
            elementData = growArray(size, isInitEmptyArray);
        }
        elementData[size] = l;
        this.size++;
        return this.size == size + 1;
    }

    @Override
    protected float[] copyElementData(final int newLength) {
        return Arrays.copyOf(elementData, newLength);
    }

    @Override
    public boolean isNotEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(final Iterable<Float> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(final float[] array) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(final Iterable<Float> iterable) {
        var allAdded = true;
        if (iterable instanceof FloatArrayList) {
            final var iterator = ((FloatArrayList) iterable).iterator();
            while (iterator.hasNext()) {
                final var added = add(iterator.nextFloat());
                if (!added) {
                    allAdded = false;
                }
            }
            return allAdded;
        }
        for (final float i : iterable) {
            if (!add(i)) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    @Override
    public boolean addAll(final float... array) {
        var allAdded = true;
        for (final var f : array) {
            if (!add(f)) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    @Override
    public boolean removeAll(final Iterable<Float> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(final float[] array) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(final FloatPredicate predicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FloatArrayList plus(final Iterable<Float> iterable) {
        final var list = new FloatArrayList();
        list.addAll(iterable);
        return list;
    }

    @Override
    public FloatArrayList plus(final float[] array) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FloatArrayList take(final long n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FloatArrayList skip(final long n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FloatArrayList)) {
            return false;
        }

        final var iterator1 = iterator();
        final var iterator2 = ((FloatArrayList) o).iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            final var l1 = iterator1.nextFloat();
            final var l2 = iterator2.nextFloat();
            if (Float.compare(l1, l2) != 0) {
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
    public FloatIterator iterator() {
        return new FloatIterator() {

            private int index = 0;
            @Override
            public float nextFloat() {
                return elementData[index++];
            }

            @Override
            public boolean hasNext() {
                return index < size;
            }
        };
    }

    @Override
    protected float[] newArray(final int length) {
        return new float[length];
    }

    @Override
    protected void appendNextPrimitive(final StringBuilder sb, final FloatIterator iterator) {
        sb.append(iterator.nextFloat());
    }

    @Override
    public Spliterator<Float> spliterator() {
        return Spliterators.spliterator(iterator(), size, Spliterator.ORDERED);
    }

    @Override
    public MutableListX<Float> boxed() {
        return MutableListX.of(this);
    }
}
