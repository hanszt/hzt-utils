package org.hzt.graph.iterators;

import org.hzt.graph.MutableNode;
import org.hzt.graph.Node;

import java.util.*;

/**
 * @param <S> the type of the child
 * @see <a href="https://gist.github.com/Xrayez/e67858723beca83f972f5790aae3a26f">BFS and DFS Iterator for Graph</a>
 */
final class DepthFirstIterator<S extends Node<S>> implements Iterator<S> {
    private final Set<S> visited = new HashSet<>();
    private final Deque<Iterator<S>> stack = new ArrayDeque<>();
    private final boolean setPredecessor;
    private S next;

    DepthFirstIterator(final S source, final boolean setPredecessor) {
        this.setPredecessor = setPredecessor;
        this.stack.push(source.neighborIterator());
        this.next = source;
    }

    @Override
    public boolean hasNext() {
        return this.next != null;
    }

    @Override
    public S next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            visited.add(next);
            return next;
        } finally {
            this.advance();
        }
    }

    private void advance() {
        var neighbors = stack.getFirst();
        do {
            while (!neighbors.hasNext()) {  // No more nodes -> back out a level
                stack.pop();
                if (stack.isEmpty()) { // All done!
                    next = null;
                    return;
                }
                neighbors = stack.peek();
            }
            final var neighbor = neighbors.next();
            if (!visited.contains(neighbor)) {
                if (setPredecessor)
                    if (neighbor instanceof MutableNode<?> mutN) {
                        //noinspection unchecked
                        ((MutableNode<S>) mutN).withPredecessor(next);
                    } else {
                        throw new IllegalStateException("Can not set predecessor");
                    }
                next = neighbor;
                break;
            }
        } while (true);
        stack.push(next.neighborIterator());
    }
}
