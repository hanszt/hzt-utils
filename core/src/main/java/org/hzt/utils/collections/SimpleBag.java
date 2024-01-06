package org.hzt.utils.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class SimpleBag<E> implements Bag<E> {
    private Node<E> first = null;
    private int size = 0;

    @Override
    public boolean isEmpty() {
        return first == null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(final E item) {
        first = new Node<>(item, first);
        size++;
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> current = first;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (hasNext()) {
                    final E item = current.item;
                    current = current.next;
                    return item;
                }
                throw new NoSuchElementException();
            }
        };
    }

    private static final class Node<T> {
        private final T item;
        private final Node<T> next;

        public Node(final T item, final Node<T> next) {
            this.item = item;
            this.next = next;
        }
    }
}
