package org.hzt.utils.collections.primitives;

import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;

/**
 * @param <T> The boxed type
 * @param <A> The primitive array type
 * @param <T_CONS> The primitive consumer
 * @param <I> the primitive iterator
 */
abstract class PrimitiveAbstractCollection<T, T_CONS, A, I extends PrimitiveIterator<T, T_CONS>> {

    protected int size;

    PrimitiveAbstractCollection(int size) {
        this.size = size;
    }

    protected abstract @NotNull I iterator();

    protected abstract A newArray(int length);

    @Override
    public String toString() {
        I iterator = iterator();
        if (!iterator.hasNext()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        while (iterator.hasNext()) {
            appendNextPrimitive(sb, iterator);
            if (!iterator.hasNext()) {
                return sb.append(']').toString();
            }
            sb.append(',').append(' ');
        }
        return sb.toString();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    protected abstract void appendNextPrimitive(StringBuilder sb, I iterator);
}
