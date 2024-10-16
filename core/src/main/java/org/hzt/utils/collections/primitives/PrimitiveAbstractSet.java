package org.hzt.utils.collections.primitives;


import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public abstract class PrimitiveAbstractSet<T, T_CONST, A, I extends PrimitiveIterator<T, T_CONST>>
        extends PrimitiveAbstractCollection<T, T_CONST, A, I> implements PrimitiveCollection<T, T_CONST, A> {

    static final float MAXIMUM_LOAD_FACTOR = 0.75F;
    static final int INITIAL_CAPACITY = 8;
    PrimitiveNode[] table;

    int mask = INITIAL_CAPACITY - 1;

    PrimitiveAbstractSet(final int size, final PrimitiveNode[] table) {
        super(size);
        this.table = table;
    }

    @Override
    @SuppressWarnings("squid:S1166")
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        //noinspection unchecked
        final PrimitiveAbstractSet<T, T_CONST, A, I> that = (PrimitiveAbstractSet<T, T_CONST, A, I>) o;
        if (size != that.size) {
            return false;
        }
        return containsAll(that::iterator);
    }

    @Override
    public int hashCode() {
        final I i = iterator();
        int hashCode = 0;
        while (i.hasNext()) {
            hashCode += nextHashCode(i);
        }
        return hashCode;
    }

    abstract int nextHashCode(I i);

    void rehash(final PrimitiveNode[] table, final PrimitiveNode[] newTable) {
        for (PrimitiveNode node : table) {
            while (node != null) {
                final PrimitiveNode next = node.next;
                final int rehashedIndex = rehash(node, newTable.length);

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

        PrimitiveNode nextNode() {
            final PrimitiveNode current = next;
            if (current == null) {
                throw new NoSuchElementException();
            }
            next = current.next;
            if (next == null) {
                final PrimitiveNode[] t = table;
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

        PrimitiveNode(final PrimitiveNode next) {
            this.next = next;
        }
    }
}
