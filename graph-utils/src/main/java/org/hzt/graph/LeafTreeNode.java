package org.hzt.graph;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.Function;

public record LeafTreeNode<T>(T value) implements SimpleTreeNode<T> {

    @Override
    public Iterator<SimpleTreeNode<T>> childrenIterator() {
        return Collections.emptyIterator();
    }
}
