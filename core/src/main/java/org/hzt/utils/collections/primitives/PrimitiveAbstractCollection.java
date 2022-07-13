package org.hzt.utils.collections.primitives;

import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;

/**
 * @param <T> The boxed type
 * @param <A> The primitive array type
 * @param <T_CONS> The primitive consumer
 * @param <I> the primitive iterator
 */
abstract class PrimitiveAbstractCollection<T, A, T_CONS, I extends PrimitiveIterator<T, T_CONS>> {

    int size;

    PrimitiveAbstractCollection(int size) {
        this.size = size;
    }

    abstract @NotNull I iterator();

    abstract A newArray(int length);

    @Override
    public String toString() {
        var iterator = iterator();
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

    abstract void appendNextPrimitive(StringBuilder sb, I iterator);
}
