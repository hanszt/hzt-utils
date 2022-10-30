package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableSetX;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.DoubleConsumer;

/**
 * This class implements a simple hash set for {@code double} values.
 * keep track of nodes that are being pointed to by fingers.
 */
final class DoubleHashSet extends PrimitiveAbstractSet<Double, double[], DoubleConsumer, PrimitiveIterator.OfDouble>
        implements DoubleMutableSet {

    DoubleHashSet() {
        this(0);
    }

    DoubleHashSet(double[] values) {
        this(values.length > 0 ? 1 : 0);
        for (double value : values) {
            add(value);
        }
    }

    public DoubleHashSet(int size) {
        super(size, new CollisionChainNode[INITIAL_CAPACITY]);
    }

    public boolean add(double value) {
        if (contains(value)) {
            return false;
        }
        size++;

        if (shouldExpand()) {
            expand();
        }

        final int targetCollisionChainIndex = Double.hashCode(value) & mask;
        final PrimitiveNode next = table[targetCollisionChainIndex];
        final CollisionChainNode newNode = new CollisionChainNode(value, next);
        table[targetCollisionChainIndex] = newNode;
        return true;
    }

    public boolean contains(double value) {
        final int collisionChainIndex = Double.hashCode(value) & mask;
        PrimitiveNode node = table[collisionChainIndex];

        while (node != null) {
            if (Double.compare(((CollisionChainNode) node).value, value) == 0) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    public boolean remove(double value) {
        if (!contains(value)) {
            return false;
        }
        size--;
        if (shouldContract()) {
            contract();
        }
        final int targetCollisionChainIndex = Double.hashCode(value) & mask;

        PrimitiveNode current = table[targetCollisionChainIndex];

        PrimitiveNode previous = null;
        while (current != null) {
            PrimitiveNode next = current.next;
            if (Double.compare(((CollisionChainNode) current).value, value) == 0) {
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
    public MutableSetX<Double> boxed() {
        return MutableSetX.of(this);
    }

    @Override
    public double[] toArray() {
        return asSequence().toArray();
    }

    public void clear() {
        size = 0;
        table = new CollisionChainNode[INITIAL_CAPACITY];
        mask = table.length - 1;
    }

    // Keep add(double) an amortized O(1)

    private boolean shouldExpand() {
        return size > table.length * MAXIMUM_LOAD_FACTOR;
    }
    // Keep remove(double) an amortized O(1)

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
    int nextHashCode(PrimitiveIterator.OfDouble iterator) {
        return Double.hashCode(iterator.nextDouble());
    }

    @Override
    int rehash(PrimitiveNode node, int newTableLength) {
        return Double.hashCode(((CollisionChainNode) node).value) & (newTableLength - 1);
    }

    @Override
    protected double[] newArray(int length) {
        return new double[length];
    }

    @Override
    public PrimitiveIterator.@NotNull OfDouble iterator() {
        return new DoubleHashIterator();
    }

    @SuppressWarnings("squid:S2694")
    private class DoubleHashIterator extends PrimitiveHashIterator implements PrimitiveIterator.OfDouble {

        @Override
        public double nextDouble() {
            return ((CollisionChainNode) nextNode()).value;
        }
    }

    private static final class CollisionChainNode extends PrimitiveNode {

        private final double value;

        CollisionChainNode(double value, PrimitiveNode next) {
            super(next);
            this.value = value;
        }

        @Override
        public String toString() {
            return "Chain node, double = " + value;
        }
    }
    @Override
    protected void appendNextPrimitive(StringBuilder sb, PrimitiveIterator.OfDouble iterator) {
        sb.append(iterator.nextDouble());
    }
}
