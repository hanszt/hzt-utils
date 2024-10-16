package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableSetX;

import java.util.PrimitiveIterator;
import java.util.function.LongConsumer;

/**
 * This class implements a simple hash set for {@code long} values.
 * keep track of nodes that are being pointed to by fingers.
 */
final class LongHashSet extends PrimitiveAbstractSet<Long, LongConsumer, long[], PrimitiveIterator.OfLong>
        implements LongMutableSet {

    LongHashSet() {
        this(0);
    }

    public LongHashSet(final long[] values) {
        this(values.length > 0 ? 1 : 0);
        for (final long value : values) {
            add(value);
        }
    }

    public LongHashSet(final int size) {
        super(size, new CollisionChainNode[INITIAL_CAPACITY]);
    }

    public boolean add(final long value) {
        if (contains(value)) {
            return false;
        }
        size++;

        if (shouldExpand()) {
            expand();
        }

        final int targetCollisionChainIndex = Long.hashCode(value) & mask;
        final PrimitiveNode next = table[targetCollisionChainIndex];
        final PrimitiveNode newNode = new CollisionChainNode(value, next);
        table[targetCollisionChainIndex] = newNode;
        return true;
    }

    public boolean contains(final long value) {
        final int collisionChainIndex = Long.hashCode(value) & mask;
        PrimitiveNode node = table[collisionChainIndex];

        while (node != null) {
            if (((CollisionChainNode) node).value == value) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    public boolean remove(final long value) {
        if (!contains(value)) {
            return false;
        }
        size--;
        if (shouldContract()) {
            contract();
        }
        final int targetCollisionChainIndex = Long.hashCode(value) & mask;

        PrimitiveNode current = table[targetCollisionChainIndex];

        PrimitiveNode previous = null;
        while (current != null) {
            final PrimitiveNode next = current.next;
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
    public MutableSetX<Long> boxed() {
        return MutableSetX.of(this);
    }

    @Override
    public long[] toArray() {
        return asSequence().toArray();
    }

    public void clear() {
        size = 0;
        table = new CollisionChainNode[INITIAL_CAPACITY];
        mask = table.length - 1;
    }

    // Keep add(long) an amortized O(1)

    private boolean shouldExpand() {
        return size > table.length * MAXIMUM_LOAD_FACTOR;
    }
    // Keep remove(long) an amortized O(1)

    private boolean shouldContract() {
        if (table.length == INITIAL_CAPACITY) {
            return false;
        }

        return MAXIMUM_LOAD_FACTOR * size * 4 < table.length;
    }
    private void expand() {
        final CollisionChainNode[] newTable = new CollisionChainNode[table.length * 2];

        rehash(table, newTable);
        table = newTable;
        mask = table.length - 1;
    }

    private void contract() {
        final CollisionChainNode[] newTable = new CollisionChainNode[table.length / 4];
        rehash(table, newTable);
        table = newTable;
        mask = table.length - 1;
    }

    @Override
    int nextHashCode(final PrimitiveIterator.OfLong iterator) {
        return Long.hashCode(iterator.nextLong());
    }

    @Override
    int rehash(final PrimitiveNode node, final int newTableLength) {
        return Long.hashCode(((CollisionChainNode) node).value) & (newTableLength - 1);
    }

    @Override
    protected long[] newArray(final int length) {
        return new long[length];
    }

    @Override
    public PrimitiveIterator.OfLong iterator() {
        return new LongHashIterator();
    }

    @SuppressWarnings("squid:S2694")
    private class LongHashIterator extends PrimitiveHashIterator implements PrimitiveIterator.OfLong {

        @Override
        public long nextLong() {
            return ((CollisionChainNode) nextNode()).value;
        }
    }

    private static final class CollisionChainNode extends PrimitiveNode {

        private final long value;

        CollisionChainNode(final long value, final PrimitiveNode next) {
            super(next);
            this.value = value;
        }

        @Override
        public String toString() {
            return "Chain node, long = " + value;
        }
    }
    @Override
    protected void appendNextPrimitive(final StringBuilder sb, final PrimitiveIterator.OfLong iterator) {
        sb.append(iterator.nextLong());
    }
}
