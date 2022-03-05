package org.hzt.utils.collections.primitives;

abstract class PrimitiveAbstractCollection<T> implements Iterable<T> {

    @Override
    public String toString() {
        var iterator = iterator();
        if (!iterator.hasNext()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (!iterator.hasNext()) {
                return sb.append(']').toString();
            }
            sb.append(',').append(' ');
        }
        return sb.toString();
    }
}
