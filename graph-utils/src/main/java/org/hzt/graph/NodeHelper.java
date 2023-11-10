package org.hzt.graph;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public final class NodeHelper {

    private NodeHelper() {
    }

    static <T, S extends Node<T, S>, R> void map(final S node,
                                                 final Function<? super S, ? extends R> function,
                                                 final Collection<R> collection) {
        final Collection<S> children = node.getNeighbors();
        collection.add(function.apply(node));
        if (children.isEmpty()) {
            return;
        }
        for (final S child : children) {
            map(child, function, collection);
        }
    }

    public static <S extends Node<T, S>, T> Iterator<S> predecessorIterator(final S node) {
        return new Iterator<S>() {

            private S next = node;
            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public S next() {
                if (next == null) {
                    throw new NoSuchElementException();
                }
                final S current = next;
                next = next.getPredecessor();
                return current;
            }
        };
    }
}
