package org.hzt.graph;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public final class GraphAlgorithms {

    private GraphAlgorithms() {
    }

    public static <T> WeightedNode<T> dijkstra(WeightedNode<T> start, WeightedNode<T> goal) {
        start.setCost(0);
        Set<WeightedNode<T>> unsettled = new HashSet<>();
        Set<WeightedNode<T>> settled = new HashSet<>();
        unsettled.add(start);
        while (!unsettled.isEmpty()) {
            WeightedNode<T> current = unsettled.stream()
                    .min(Comparator.comparing(WeightedNode::getCost))
                    .orElseThrow(NoSuchElementException::new);

            for (WeightedNode<T> neighbor : current.getNeighbors()) {
                if (!settled.contains(neighbor)) {
                    neighbor.updateCost(current);
                    unsettled.add(neighbor);
                }
            }

            if (current.equals(goal)) {
                return current;
            }
            unsettled.remove(current);
            settled.add(current);
        }
        throw new IllegalStateException("Dit not find a path to goal: " + goal);
    }

    public static <T> NodeImpl<T> breadthFirstSearch(NodeImpl<T> start, List<NodeImpl<T>> graph) {
        throw new UnsupportedOperationException();
    }

    public static <T> NodeImpl<T> depthFirstSearch(NodeImpl<T> start, List<NodeImpl<T>> graph) {
        throw new UnsupportedOperationException();
    }
}
