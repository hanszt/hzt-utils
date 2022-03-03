package org.hzt.utils.collections.primitives;

import java.util.PrimitiveIterator;

abstract class PrimitiveAbstractCollection<T, C, I extends PrimitiveIterator<T, C>> {

    @Override
    public String toString() {
        I iterator = iterator();
        if (! iterator.hasNext()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        while(iterator.hasNext()) {
            sb.append(iterator.next());
            if (!iterator.hasNext()) {
                return sb.append(']').toString();
            }
            sb.append(',').append(' ');
        }
        return sb.toString();
    }

    abstract I iterator();
}
