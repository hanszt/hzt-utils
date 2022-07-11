package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableSetX;
import org.jetbrains.annotations.NotNull;

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
final class IntHashSet extends PrimitiveAbstractSet<Integer, int[], IntConsumer, PrimitiveIterator.OfInt>
        implements IntMutableSet {

    IntHashSet() {
        this(0);
    }

    public IntHashSet(int[] values) {
        this(values.length > 0 ? 1 : 0);
        for (int value : values) {
            add(value);
        }
    }

    public IntHashSet(int size) {
        super(size, new CollisionChainNode[INITIAL_CAPACITY]);
    }

    public boolean add(int value) {
        if (contains(value)) {
            return false;
        }
        size++;

        if (shouldExpand()) {
            expand();
        }

        final int targetCollisionChainIndex = value & mask;
        final var next = table[targetCollisionChainIndex];
        final PrimitiveNode newNode = new CollisionChainNode(value, next);
        table[targetCollisionChainIndex] = newNode;
        return true;
    }

    public boolean contains(int value) {
        final int collisionChainIndex = value & mask;
        var node = table[collisionChainIndex];

        while (node != null) {
            if (((CollisionChainNode) node).value == value) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    public boolean remove(int value) {
        if (!contains(value)) {
            return false;
        }
        size--;
        if (shouldContract()) {
            contract();
        }
        final int targetCollisionChainIndex = value & mask;

        var current = table[targetCollisionChainIndex];

        PrimitiveNode previous = null;
        while (current != null) {
            var next = current.next;
            if (((CollisionChainNode) current).value == value) {
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
        CollisionChainNode[] newTable = new CollisionChainNode[table.length * 2];

        rehash(table, newTable);
        table = newTable;
        mask = table.length - 1;
    }

    private void contract() {
        CollisionChainNode[] newTable = new CollisionChainNode[table.length / 4];
        rehash(table, newTable);
        table = newTable;
        mask = table.length - 1;
    }

    @Override
    int nextHashCode(PrimitiveIterator.OfInt iterator) {
        return Integer.hashCode(iterator.nextInt());
    }

    @Override
    int rehash(PrimitiveNode node, int newTableLength) {
        return ((CollisionChainNode) node).value & (newTableLength - 1);
    }

    @Override
    int[] newArray(int length) {
        return new int[length];
    }

    @Override
    public PrimitiveIterator.@NotNull OfInt iterator() {
        return new IntHashIterator();
    }

    @SuppressWarnings("squid:S2694")
    private class IntHashIterator extends PrimitiveHashIterator implements PrimitiveIterator.OfInt {

        @Override
        public int nextInt() {
            return ((CollisionChainNode) nextNode()).value;
        }
    }

    private static final class CollisionChainNode extends PrimitiveNode {

        private final int value;

        CollisionChainNode(int value, PrimitiveNode next) {
            super(next);
            this.value = value;
        }

        @Override
        public String toString() {
            return "Chain node, integer = " + value;
        }
    }
    @Override
    void appendNextPrimitive(StringBuilder sb, PrimitiveIterator.OfInt iterator) {
        sb.append(iterator.nextInt());
    }
}
