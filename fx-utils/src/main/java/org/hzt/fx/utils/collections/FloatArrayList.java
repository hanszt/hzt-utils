package org.hzt.fx.utils.collections;

import org.hzt.fx.utils.function.FloatConsumer;
import org.hzt.fx.utils.function.FloatPredicate;
import org.hzt.fx.utils.iterables.FloatIterable;
import org.hzt.fx.utils.iterators.FloatIterator;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.primitives.PrimitiveAbstractList;
import org.hzt.utils.collections.primitives.PrimitiveMutableCollection;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;

public final class FloatArrayList extends PrimitiveAbstractList<Float, FloatConsumer, float[], FloatIterator>
        implements PrimitiveMutableCollection<Float, FloatConsumer, float[], FloatPredicate>, FloatIterable {

    public FloatArrayList() {
        this(0, new float[DEFAULT_CAPACITY]);
    }

    private FloatArrayList(int initSize, float[] elementData) {
        super(initSize, elementData);
    }

    public boolean add(float l) {
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
    protected float[] copyElementData(int newLength) {
        return Arrays.copyOf(elementData, newLength);
    }

    @Override
    public boolean isNotEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(@NotNull Iterable<Float> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(float @NotNull [] array) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(@NotNull Iterable<Float> iterable) {
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
        for (float i : iterable) {
            if (!add(i)) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    @Override
    public boolean addAll(float @NotNull ... array) {
        var allAdded = true;
        for (var f : array) {
            if (!add(f)) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    @Override
    public boolean removeAll(@NotNull Iterable<Float> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(float @NotNull [] array) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(@NotNull FloatPredicate predicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FloatArrayList plus(@NotNull Iterable<Float> iterable) {
        var list = new FloatArrayList();
        list.addAll(iterable);
        return list;
    }

    @Override
    public FloatArrayList plus(float @NotNull [] array) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FloatArrayList take(long n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FloatArrayList skip(long n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FloatArrayList)) {
            return false;
        }

        var iterator1 = iterator();
        var iterator2 = ((FloatArrayList) o).iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            var l1 = iterator1.nextFloat();
            var l2 = iterator2.nextFloat();
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
    public @NotNull FloatIterator iterator() {
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
    protected float[] newArray(int length) {
        return new float[length];
    }

    @Override
    protected void appendNextPrimitive(StringBuilder sb, FloatIterator iterator) {
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
