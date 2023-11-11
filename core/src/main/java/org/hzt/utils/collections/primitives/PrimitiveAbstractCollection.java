package org.hzt.utils.collections.primitives;


import java.util.PrimitiveIterator;

/**
 * @param <T> The boxed type
 * @param <A> The primitive array type
 * @param <T_CONS> The primitive consumer
 * @param <I> the primitive iterator
 */
abstract class PrimitiveAbstractCollection<T, T_CONS, A, I extends PrimitiveIterator<T, T_CONS>> {

    protected int size;

    PrimitiveAbstractCollection(final int size) {
        this.size = size;
    }

    protected abstract I iterator();

    protected abstract A newArray(int length);

    @Override
    public String toString() {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            return "[]";
        }
        final var sb = new StringBuilder();
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
