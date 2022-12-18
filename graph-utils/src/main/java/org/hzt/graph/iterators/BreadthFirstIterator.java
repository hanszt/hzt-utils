package org.hzt.graph.iterators;

import org.hzt.graph.Node;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

/**
 * @param <T> the type of this
 * @param <S> the type of the child
 *
 * @see <a href="https://gist.github.com/Xrayez/e67858723beca83f972f5790aae3a26f">BFS and DFS Iterator for Graph</a>
 */
final class BreadthFirstIterator<T, S extends Node<T, S>> implements Iterator<S> {
    private final Set<S> visited = new HashSet<>();
    private final Queue<S> queue = new LinkedList<>();

    BreadthFirstIterator(S node) {
        queue.add(node);
        visited.add(node);
    }

    @Override
    public boolean hasNext() {
        return !this.queue.isEmpty();
    }

    @Override
    public S next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        S next = queue.remove();
        for (S neighbor : next.getNeighbors()) {
            if (!this.visited.contains(neighbor)) {
                neighbor.withPredecessor(next);
                this.queue.add(neighbor);
                this.visited.add(neighbor);
            }
        }
        return next;
    }
}
