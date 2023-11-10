package org.hzt.graph;

import java.util.Comparator;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public final class GraphAlgorithms {

    private GraphAlgorithms() {
    }

    public static <T> WeightedNode<T> dijkstra(final WeightedNode<T> start, final WeightedNode<T> goal) {
        start.setCost(0);
        final Set<WeightedNode<T>> unsettled = new HashSet<>();
        final Set<WeightedNode<T>> settled = new HashSet<>();
        unsettled.add(start);
        while (!unsettled.isEmpty()) {
            final WeightedNode<T> current = unsettled.stream()
                    .min(Comparator.comparing(WeightedNode::getCost))
                    .orElseThrow(NoSuchElementException::new);

            for (final WeightedEdge<T> edge : current.getEdges()) {
                final WeightedEdge<T> weightedEdge = edge;
                final WeightedNode<T> neighbor = weightedEdge.getOpposite(current);
                if (!settled.contains(neighbor)) {
                    neighbor.setCost(current.getCost());
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
}
