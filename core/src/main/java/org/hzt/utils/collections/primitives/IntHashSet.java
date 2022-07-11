package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableSetX;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

/**
 * This class implements a simple hash set for {@code int} values.
 * keep track of nodes that are being pointed to by fingers.
 *
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Aug 29, 2021)
 * @since 1.6 (Aug 29, 2021)
 *
 * @see <a href="https://codereview.stackexchange.com/questions/266541/a-simple-java-integer-hash-set">A simple Java integer hash set</a>
 */
final class IntHashSet extends PrimitiveAbstractCollection<Integer, int[], IntConsumer, PrimitiveIterator.OfInt>
        implements IntMutableSetX {

    private static final int INITIAL_CAPACITY = 8;
    private static final float MAXIMUM_LOAD_FACTOR = 0.75F;

    private CollisionChainNode[] table = new CollisionChainNode[INITIAL_CAPACITY];

    private int mask = INITIAL_CAPACITY - 1;

    IntHashSet() {
        super(0);
    }

    public boolean add(int integer) {
        if (contains(integer)) {
            return false;
        }
        size++;

        if (shouldExpand()) {
            expand();
        }

        final int targetCollisionChainIndex = integer & mask;
        final var next = table[targetCollisionChainIndex];
        final CollisionChainNode newNode = new CollisionChainNode(integer, next);
        table[targetCollisionChainIndex] = newNode;
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    public boolean contains(int integer) {
        final int collisionChainIndex = integer & mask;
        CollisionChainNode node = table[collisionChainIndex];

        while (node != null) {
            if (node.integer == integer) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    public boolean remove(int integer) {
        if (!contains(integer)) {
            return false;
        }

        size--;

        if (shouldContract()) {
            contract();
        }
        final int targetCollisionChainIndex = integer & mask;

        CollisionChainNode current = table[targetCollisionChainIndex];

        CollisionChainNode previous = null;

        while (current != null) {
            CollisionChainNode next = current.next;

            if (current.integer == integer) {
                if (previous == null) {
                    table[targetCollisionChainIndex] = next;
                } else {
                    previous.next = next;
                }
                return true;
            }

            previous = current;
            current = next;
        }
        return true;
    }

    @Override
    public MutableSetX<Integer> boxed() {
        return MutableSetX.of(this);
    }

    @Override
    public int[] toArray() {
        return asSequence().toArray();
    }

    public void clear() {
        size = 0;
        table = new CollisionChainNode[INITIAL_CAPACITY];
        mask = table.length - 1;
    }

    // Keep add(int) an amortized O(1)

    private boolean shouldExpand() {
        return size > table.length * MAXIMUM_LOAD_FACTOR;
    }
    // Keep remove(int) an amortized O(1)

    private boolean shouldContract() {
        if (table.length == INITIAL_CAPACITY) {
            return false;
        }

        return MAXIMUM_LOAD_FACTOR * size * 4 < table.length;
    }
    private void expand() {
        CollisionChainNode[] newTable =
                new CollisionChainNode[table.length * 2];

        rehash(newTable);
        table = newTable;
        mask = table.length - 1;
    }

    private void contract() {
        CollisionChainNode[] newTable =
                new CollisionChainNode[table.length / 4];

        rehash(newTable);
        table = newTable;
        mask = table.length - 1;
    }

    private void rehash(CollisionChainNode[] newTable) {
        for (CollisionChainNode node : table) {
            while (node != null) {
                final CollisionChainNode next = node.next;
                final int rehashedIndex = rehash(node.integer, newTable);

                node.next = newTable[rehashedIndex];
                newTable[rehashedIndex] = node;
                node = next;
            }
        }
    }

    private static int rehash(
            int integer,
            CollisionChainNode[] newTable) {
        return integer & (newTable.length - 1);
    }

    @Override
    public PrimitiveIterator.@NotNull OfInt iterator() {
        return new IntHashIterator();
    }

    @Override
    int[] newArray(int length) {
        return new int[length];
    }

    @SuppressWarnings("squid:S2972")
    private class IntHashIterator implements PrimitiveIterator.OfInt {

        private CollisionChainNode next = null;
        private int index = 0;
        public IntHashIterator() {
            while (index < table.length && next == null) {
                next = table[index];
                index++;
            }
        }

        @Override
        public int nextInt() {
            final var current = next;
            if (current == null) {
                throw new NoSuchElementException();
            }
            next = current.next;
            if (next == null) {
                CollisionChainNode[] t = table;
                while (index < t.length) {
                    next = t[index];
                    index++;
                    if (next != null) {
                        break;
                    }
                }
            }
            return current.integer;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

    }
    private static final class CollisionChainNode {

        private CollisionChainNode next;
        private final int integer;
        CollisionChainNode(int integer, CollisionChainNode next) {
            this.integer = integer;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Chain node, integer = " + integer;
        }

    }
    @Override
    void appendNextPrimitive(StringBuilder sb, PrimitiveIterator.OfInt iterator) {
        sb.append(iterator.nextInt());
    }
}
