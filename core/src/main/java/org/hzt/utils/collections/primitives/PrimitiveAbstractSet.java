package org.hzt.utils.collections.primitives;

import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public abstract class PrimitiveAbstractSet<T, T_CONST, A, I extends PrimitiveIterator<T, T_CONST>>
        extends PrimitiveAbstractCollection<T, T_CONST, A, I> implements PrimitiveCollection<T, T_CONST, A> {

    static final float MAXIMUM_LOAD_FACTOR = 0.75F;
    static final int INITIAL_CAPACITY = 8;
    PrimitiveNode[] table;

    int mask = INITIAL_CAPACITY - 1;

    PrimitiveAbstractSet(int size, PrimitiveNode[] table) {
        super(size);
        this.table = table;
    }

    @Override
    @SuppressWarnings("squid:S1166")
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        //noinspection unchecked
        var that = (PrimitiveAbstractSet<T, T_CONST, A, I>) o;
        if (size != that.size) {
            return false;
        }
        return containsAll(that::iterator);
    }

    @Override
    public int hashCode() {
        final var i = iterator();
        var hashCode = 0;
        while (i.hasNext()) {
            hashCode += nextHashCode(i);
        }
        return hashCode;
    }

    abstract int nextHashCode(I i);

    void rehash(PrimitiveNode[] table, PrimitiveNode[] newTable) {
        for (var node : table) {
            while (node != null) {
                final var next = node.next;
                final var rehashedIndex = rehash(node, newTable.length);

                node.next = newTable[rehashedIndex];
                newTable[rehashedIndex] = node;
                node = next;
            }
        }
    }

    abstract int rehash(PrimitiveNode node, int newTableLength);

    @SuppressWarnings("squid:S2972")
    class PrimitiveHashIterator {
        private PrimitiveNode next = null;
        private int index = 0;

        PrimitiveHashIterator() {
            while (index < table.length && next == null) {
                next = table[index];
                index++;
            }
        }

        @NotNull PrimitiveNode nextNode() {
            final var current = next;
            if (current == null) {
                throw new NoSuchElementException();
            }
            next = current.next;
            if (next == null) {
                var t = table;
                while (index < t.length) {
                    next = t[index];
                    index++;
                    if (next != null) {
                        break;
                    }
                }
            }
            return current;
        }

        public boolean hasNext() {
            return next != null;
        }
    }

    static class PrimitiveNode {

        PrimitiveNode next;

        PrimitiveNode(PrimitiveNode next) {
            this.next = next;
        }
    }
}
