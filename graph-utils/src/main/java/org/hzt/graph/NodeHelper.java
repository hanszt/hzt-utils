package org.hzt.graph;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

final class NodeHelper {

    private NodeHelper() {
    }

    static <T, S extends Node<T, S>, R> void map(final S node,
                                                 final Function<? super S, ? extends R> function,
                                                 final Collection<R> collection) {
        final Collection<S> children = node.getMutableNeighbors();
        collection.add(function.apply(node));
        if (children.isEmpty()) {
            return;
        }
        for (final S child : children) {
            map(child, function, collection);
        }
    }

    static <S extends Node<T, S>, T> Iterator<S> predecessorIterator(final S initial) {
        return new Iterator<S>() {

            private boolean isThis = true;
            private S next = initial;

            @Override
            public boolean hasNext() {
                if (isThis) {
                    isThis = false;
                    return true;
                }
                final Optional<S> predecessor = next.optionalPredecessor();
                final boolean present = predecessor.isPresent();
                if (present) {
                    next = predecessor.orElseThrow(NoSuchElementException::new);
                }
                return next != null && present;
            }

            @Override
            public S next() {
                if (next == null) {
                    throw new NoSuchElementException();
                }
                return next;
            }
        };
    }
}
