package org.hzt.fx.utils;

import org.hzt.fx.utils.function.FloatConsumer;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.primitives.PrimitiveAbstractList;
import org.hzt.utils.collections.primitives.PrimitiveMutableCollection;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;

public final class FloatList extends PrimitiveAbstractList<Float, float[], FloatConsumer, FloatIterator>
        implements PrimitiveMutableCollection<Float, FloatConsumer, Predicate<Float>, float[]> {

    FloatList() {
        this(0, new float[DEFAULT_CAPACITY]);
    }

    private FloatList(int initSize, float[] elementData) {
        super(initSize, elementData);
    }

    public boolean add(float l) {
        final int size = this.size;
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
        boolean allAdded = true;
        if (iterable instanceof FloatList) {
            final var iterator = ((FloatList) iterable).iterator();
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
    public boolean addAll(float @NotNull [] array) {
        return false;
    }

    @Override
    public boolean removeAll(@NotNull Iterable<Float> iterable) {
        return false;
    }

    @Override
    public boolean removeAll(float @NotNull [] array) {
        return false;
    }

    @Override
    public boolean removeIf(@NotNull Predicate<Float> predicate) {
        return false;
    }

    @Override
    public FloatList plus(@NotNull Iterable<Float> iterable) {
        FloatList list = new FloatList();
        list.addAll(iterable);
        return list;
    }

    @Override
    public FloatList plus(float @NotNull [] array) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FloatList take(long n) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<Float> spliterator() {
        return Spliterators.spliterator(iterator(), size, Spliterator.ORDERED);
    }

    @Override
    public MutableListX<Float> boxed() {
        throw new UnsupportedOperationException();
    }
}
